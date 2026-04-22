package com.seyran.hirelens.service;
import com.seyran.hirelens.dto.response.ResumeResponseDTO;
import com.seyran.hirelens.entity.Resume;
import com.seyran.hirelens.repository.ResumeRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeResponseDTO uploadResume(MultipartFile file) {
        try {
            Resume resume = new Resume();

            resume.setFileName(file.getOriginalFilename());
            resume.setOriginalText(extractTextFromFile(file));
            resume.setExtractedData("Raw text extracted from uploaded resume");
            resume.setCreatedAt(LocalDateTime.now());

            Resume savedResume = resumeRepository.save(resume);

            ResumeResponseDTO responseDTO = new ResumeResponseDTO();
            responseDTO.setId(savedResume.getId());
            responseDTO.setFileName(savedResume.getFileName());
            responseDTO.setOriginalText(savedResume.getOriginalText());
            responseDTO.setExtractedData(savedResume.getExtractedData());
            responseDTO.setCreatedAt(savedResume.getCreatedAt());

            return responseDTO;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded resume file.");
        }
    }
    private String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException("File name is missing.");
        }

        String lowerFileName = fileName.toLowerCase();

        if (lowerFileName.endsWith(".txt")) {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        }

        if (lowerFileName.endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                PDFTextStripper pdfTextStripper = new PDFTextStripper();
                return pdfTextStripper.getText(document);
            }
        }

        throw new RuntimeException("Only PDF and TXT files are supported for now.");
    }
    public List<ResumeResponseDTO> getAllResumes() {
        // Retrieves all resume entities from the database

        List<Resume> resumes = resumeRepository.findAll();
        // Gets all Resume entities

        List<ResumeResponseDTO> responseDTOList = new ArrayList<>();
        // Creates an empty response DTO list

        for (Resume resume : resumes) {
            ResumeResponseDTO responseDTO = new ResumeResponseDTO();
            // Creates a response DTO for each resume

            responseDTO.setId(resume.getId());
            responseDTO.setFileName(resume.getFileName());
            responseDTO.setOriginalText(resume.getOriginalText());
            responseDTO.setExtractedData(resume.getExtractedData());
            responseDTO.setCreatedAt(resume.getCreatedAt());
            // Maps entity data to the response DTO

            responseDTOList.add(responseDTO);
            // Adds the DTO to the response list
        }

        return responseDTOList;
        // Returns the final DTO list
    }

    public ResumeResponseDTO getResumeById(Long id) {
        // Finds the resume entity by id

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
        // Throws an exception if the resume does not exist

        ResumeResponseDTO responseDTO = new ResumeResponseDTO();
        // Creates the response DTO

        responseDTO.setId(resume.getId());
        responseDTO.setFileName(resume.getFileName());
        responseDTO.setOriginalText(resume.getOriginalText());
        responseDTO.setExtractedData(resume.getExtractedData());
        responseDTO.setCreatedAt(resume.getCreatedAt());
        // Maps entity data to the response DTO

        return responseDTO;
        // Returns the final response
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}