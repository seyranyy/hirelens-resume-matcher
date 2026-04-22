package com.seyran.hirelens.service;

import com.seyran.hirelens.dto.request.MatchAnalysisCreateRequestDTO;
import com.seyran.hirelens.dto.response.AnalysisResultDTO;
import com.seyran.hirelens.dto.response.MatchAnalysisResponseDTO;
import com.seyran.hirelens.entity.JobPosting;
import com.seyran.hirelens.entity.MatchAnalysis;
import com.seyran.hirelens.entity.Resume;
import com.seyran.hirelens.repository.JobPostingRepository;
import com.seyran.hirelens.repository.MatchAnalysisRepository;
import com.seyran.hirelens.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchAnalysisService {

    private final MatchAnalysisRepository matchAnalysisRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;
    private final AIAnalysisService aiAnalysisService;

    public MatchAnalysisService(MatchAnalysisRepository matchAnalysisRepository,
                                ResumeRepository resumeRepository,
                                JobPostingRepository jobPostingRepository,
                                AIAnalysisService aiAnalysisService) {
        this.matchAnalysisRepository = matchAnalysisRepository;
        this.resumeRepository = resumeRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.aiAnalysisService = aiAnalysisService;
    }

    public MatchAnalysisResponseDTO createMatchAnalysis(MatchAnalysisCreateRequestDTO requestDTO) {
        Resume resume = resumeRepository.findById(requestDTO.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + requestDTO.getResumeId()));

        JobPosting jobPosting = jobPostingRepository.findById(requestDTO.getJobPostingId())
                .orElseThrow(() -> new RuntimeException("Job posting not found with id: " + requestDTO.getJobPostingId()));

        AnalysisResultDTO analysisResult = aiAnalysisService.analyzeResumeAgainstJobPosting(resume, jobPosting);

        MatchAnalysis matchAnalysis = new MatchAnalysis();
        matchAnalysis.setResume(resume);
        matchAnalysis.setJobPosting(jobPosting);
        matchAnalysis.setScore(analysisResult.getScore());
        matchAnalysis.setMatchedSkills(analysisResult.getMatchedSkills());
        matchAnalysis.setMissingSkills(analysisResult.getMissingSkills());
        matchAnalysis.setAnalysisSummary(analysisResult.getAnalysisSummary());

        MatchAnalysis savedMatchAnalysis = matchAnalysisRepository.save(matchAnalysis);

        return mapToResponseDTO(savedMatchAnalysis);
    }

    public List<MatchAnalysisResponseDTO> getAllMatchAnalyses() {
        return matchAnalysisRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MatchAnalysisResponseDTO getMatchAnalysisById(Long id) {
        MatchAnalysis matchAnalysis = matchAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match analysis not found with id: " + id));

        return mapToResponseDTO(matchAnalysis);
    }

    public void deleteMatchAnalysis(Long id) {
        MatchAnalysis matchAnalysis = matchAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match analysis not found with id: " + id));

        matchAnalysisRepository.delete(matchAnalysis);
    }

    private MatchAnalysisResponseDTO mapToResponseDTO(MatchAnalysis matchAnalysis) {
        MatchAnalysisResponseDTO responseDTO = new MatchAnalysisResponseDTO();

        responseDTO.setId(matchAnalysis.getId());
        responseDTO.setScore(matchAnalysis.getScore());
        responseDTO.setMatchedSkills(matchAnalysis.getMatchedSkills());
        responseDTO.setMissingSkills(matchAnalysis.getMissingSkills());
        responseDTO.setAnalysisSummary(matchAnalysis.getAnalysisSummary());
        responseDTO.setCreatedAt(matchAnalysis.getCreatedAt());

        responseDTO.setResumeId(matchAnalysis.getResume().getId());
        responseDTO.setResumeFileName(matchAnalysis.getResume().getFileName());

        responseDTO.setJobPostingId(matchAnalysis.getJobPosting().getId());
        responseDTO.setJobPostingTitle(matchAnalysis.getJobPosting().getTitle());
        responseDTO.setCompany(matchAnalysis.getJobPosting().getCompany());

        return responseDTO;
    }
}