package com.seyran.hirelens.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyran.hirelens.dto.response.AnalysisResultDTO;
import com.seyran.hirelens.entity.JobPosting;
import com.seyran.hirelens.entity.Resume;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIAnalysisService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIAnalysisService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AnalysisResultDTO analyzeResumeAgainstJobPosting(Resume resume, JobPosting jobPosting) {
        try {
            return callOpenRouter(resume, jobPosting);
        } catch (Exception e) {
            return fallbackKeywordAnalysis(resume, jobPosting);
        }
    }

    private AnalysisResultDTO callOpenRouter(Resume resume, JobPosting jobPosting) throws Exception {
        String apiKey = System.getenv("OPENROUTER_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("OPENROUTER_API_KEY is missing");
        }

        String prompt = buildPrompt(resume, jobPosting);

        String requestBody = """
                {
                  "model": "openrouter/free",
                  "messages": [
                    {
                      "role": "user",
                      "content": %s
                    }
                  ],
                  "response_format": {
                    "type": "json_object"
                  }
                }
                """.formatted(objectMapper.writeValueAsString(prompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("HTTP-Referer", "http://localhost:8080");
        headers.set("X-OpenRouter-Title", "HireLens");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://openrouter.ai/api/v1/chat/completions",
                HttpMethod.POST,
                entity,
                String.class
        );

        return parseOpenRouterResponse(response.getBody());
    }

    private String buildPrompt(Resume resume, JobPosting jobPosting) {
        return """
                Compare this resume and job posting.

                The resume and the job posting may be in Turkish, English, or a mix of both.
                You must understand and evaluate both Turkish and English content correctly.
                Match equivalent technical or professional skills even if they appear in different languages or formats.

                You must return ONLY valid JSON.
                Do not use markdown.
                Do not use code blocks.
                Do not add explanations.
                Do not add extra text.

                Return exactly this JSON structure:
                {
                  "score": 0,
                  "matchedSkills": "skill1, skill2",
                  "missingSkills": "skill3, skill4",
                  "analysisSummary": "short summary"
                }

                Rules:
                - Compare the candidate's skills with the job requirements.
                - Recognize Turkish and English skill terms.
                - Treat equivalent terms as matches when appropriate.
                - Keep the response concise and structured.
                - The summary can be in English.

                Resume Text:
                %s

                Job Posting Title:
                %s

                Company:
                %s

                Job Description:
                %s
                """.formatted(
                safe(resume.getOriginalText()),
                safe(jobPosting.getTitle()),
                safe(jobPosting.getCompany()),
                safe(jobPosting.getDescription())
        );
    }

    private AnalysisResultDTO parseOpenRouterResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);

        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new RuntimeException("No choices returned from OpenRouter");
        }

        String content = choices.get(0).path("message").path("content").asText();
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Empty content returned from OpenRouter");
        }

        content = cleanJsonContent(content);

        JsonNode aiJson = objectMapper.readTree(content);

        if (!aiJson.has("score")
                || !aiJson.has("matchedSkills")
                || !aiJson.has("missingSkills")
                || !aiJson.has("analysisSummary")) {
            throw new RuntimeException("Incomplete AI JSON response");
        }

        AnalysisResultDTO resultDTO = new AnalysisResultDTO();
        resultDTO.setScore(aiJson.get("score").asDouble());
        resultDTO.setMatchedSkills(aiJson.get("matchedSkills").asText());
        resultDTO.setMissingSkills(aiJson.get("missingSkills").asText());
        resultDTO.setAnalysisSummary(aiJson.get("analysisSummary").asText());

        return resultDTO;
    }

    private String cleanJsonContent(String content) {
        String cleaned = content.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
        }

        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3).trim();
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }

        return cleaned;
    }

    private AnalysisResultDTO fallbackKeywordAnalysis(Resume resume, JobPosting jobPosting) {
        String resumeText = safe(resume.getOriginalText()).toLowerCase();
        String jobDescription = safe(jobPosting.getDescription()).toLowerCase();

        Map<String, List<String>> skillAliases = buildSkillAliases();

        List<String> requiredSkills = new ArrayList<>();
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : skillAliases.entrySet()) {
            String canonicalSkill = entry.getKey();
            List<String> aliases = entry.getValue();

            boolean requiredInJob = containsAnyTerm(jobDescription, aliases);
            if (!requiredInJob) {
                continue;
            }

            requiredSkills.add(canonicalSkill);

            boolean foundInResume = containsAnyTerm(resumeText, aliases);
            if (foundInResume) {
                matchedSkills.add(canonicalSkill);
            } else {
                missingSkills.add(canonicalSkill);
            }
        }

        double score =
                requiredSkills.isEmpty()
                        ? 0.0
                        : ((double) matchedSkills.size() / requiredSkills.size()) * 100.0;

        AnalysisResultDTO resultDTO = new AnalysisResultDTO();
        resultDTO.setScore((double) Math.round(score));
        resultDTO.setMatchedSkills(matchedSkills.isEmpty() ? "None" : String.join(", ", matchedSkills));
        resultDTO.setMissingSkills(missingSkills.isEmpty() ? "None" : String.join(", ", missingSkills));

        if (requiredSkills.isEmpty()) {
            resultDTO.setAnalysisSummary(
                    "No clear job requirements were detected from the job description."
            );
        } else if (matchedSkills.isEmpty()) {
            resultDTO.setAnalysisSummary(
                    "The candidate does not appear to match the detected job requirements based on keyword analysis."
            );
        } else if (missingSkills.isEmpty()) {
            resultDTO.setAnalysisSummary(
                    "The candidate appears to match all detected job requirements based on keyword analysis."
            );
        } else {
            resultDTO.setAnalysisSummary(
                    "The candidate matches: " + String.join(", ", matchedSkills)
                            + ", but is still missing: " + String.join(", ", missingSkills) + "."
            );
        }
        return resultDTO;
    }

    private boolean containsAnyTerm(String text, List<String> terms) {
        for (String term : terms) {
            if (text.contains(term.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private Map<String, List<String>> buildSkillAliases() {
        Map<String, List<String>> skillAliases = new LinkedHashMap<>();

        skillAliases.put("java", List.of("java"));
        skillAliases.put("spring boot", List.of("spring boot", "springboot", "spring"));
        skillAliases.put("postgresql", List.of("postgresql", "postgres", "postgre"));
        skillAliases.put("sql", List.of("sql", "structured query language", "veritabanı", "veri tabanı"));
        skillAliases.put("rest api", List.of("rest api", "restful api", "rest", "api development"));
        skillAliases.put("docker", List.of("docker", "containerization", "containers"));
        skillAliases.put("aws", List.of("aws", "amazon web services"));
        skillAliases.put("react", List.of("react", "react.js", "reactjs"));
        skillAliases.put("typescript", List.of("typescript", "type script", "ts"));
        skillAliases.put("javascript", List.of("javascript", "java script", "js"));
        skillAliases.put("git", List.of("git", "github", "gitlab", "version control"));
        skillAliases.put("hibernate", List.of("hibernate", "jpa"));

        skillAliases.put("figma", List.of("figma"));
        skillAliases.put("agile", List.of("agile", "scrum", "kanban", "çevik"));
        skillAliases.put("leadership", List.of("leadership", "liderlik", "team lead", "leading teams"));
        skillAliases.put("team development", List.of("team development", "team building", "mentoring", "koçluk"));
        skillAliases.put("budget management", List.of("budget management", "budgeting", "bütçe yönetimi"));
        skillAliases.put("investment planning", List.of("investment planning", "yatırım planlama", "financial planning"));
        skillAliases.put("communication", List.of("communication", "communicator", "iletişim", "effective communication", "stakeholder management"));
        skillAliases.put("decision making", List.of("decision making", "karar verme", "strategic decisions"));
        skillAliases.put("digital transformation", List.of("digital transformation", "dijital dönüşüm"));
        skillAliases.put("technology strategy", List.of("technology strategy", "technology planning", "teknoloji stratejisi"));
        skillAliases.put("system integrations", List.of("system integrations", "system integration", "entegrasyon"));
        skillAliases.put("cloud infrastructures", List.of("cloud infrastructures", "cloud infrastructure", "bulut altyapısı"));
        skillAliases.put("enterprise applications", List.of("enterprise applications", "kurumsal uygulamalar"));
        skillAliases.put("software architecture", List.of("software architecture", "yazılım mimarisi"));
        skillAliases.put("english", List.of("english", "fluent in english", "spoken english", "written english", "ingilizce"));

        skillAliases.put("manufacturing", List.of("manufacturing", "üretim"));
        skillAliases.put("automotive", List.of("automotive", "otomotiv"));
        skillAliases.put("industrial organizations", List.of("industrial organizations", "industrial organization", "endüstriyel"));

        return skillAliases;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}