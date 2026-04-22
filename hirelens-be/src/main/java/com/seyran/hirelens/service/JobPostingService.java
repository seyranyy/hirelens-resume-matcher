package com.seyran.hirelens.service;
import com.seyran.hirelens.dto.request.CreateJobPostingFromUrlRequestDTO;
import com.seyran.hirelens.dto.request.CreateJobPostingRequestDTO;
import com.seyran.hirelens.dto.response.JobPostingResponseDTO;

import java.time.LocalDateTime;
import com.seyran.hirelens.entity.JobPosting;
import com.seyran.hirelens.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;// dependency

    public JobPostingService(JobPostingRepository jobPostingRepository) {// injected by Spring
        this.jobPostingRepository = jobPostingRepository;
    }
    //This helps reveal how entity-to-DTO conversion works step by step.
    public JobPostingResponseDTO createJobPosting(CreateJobPostingRequestDTO requestDTO) {
        JobPosting jobPosting = new JobPosting();

        jobPosting.setTitle(requestDTO.getTitle());
        jobPosting.setCompany(requestDTO.getCompany());
        jobPosting.setDescription(requestDTO.getDescription());
        jobPosting.setCreatedAt(LocalDateTime.now());

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

        JobPostingResponseDTO responseDTO = new JobPostingResponseDTO();
        responseDTO.setId(savedJobPosting.getId());
        responseDTO.setTitle(savedJobPosting.getTitle());
        responseDTO.setCompany(savedJobPosting.getCompany());
        responseDTO.setDescription(savedJobPosting.getDescription());
        responseDTO.setSourceUrl(savedJobPosting.getSourceUrl());
        responseDTO.setExtractedData(savedJobPosting.getExtractedData());
        responseDTO.setCreatedAt(savedJobPosting.getCreatedAt());

        return responseDTO;
    }

    public List<JobPostingResponseDTO> getAllJobPostings() {
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        List<JobPostingResponseDTO> responseDTOList = new ArrayList<>();

        for (JobPosting jobPosting : jobPostings) {
            responseDTOList.add(mapToResponseDTO(jobPosting));
        }

        return responseDTOList;
    }

    public JobPostingResponseDTO getJobPostingById(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job posting not found with id: " + id));

        return mapToResponseDTO(jobPosting);
    }

    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }
 // This method:
 //takes a JobPosting entity
 //returns a JobPostingResponseDTO
    private JobPostingResponseDTO mapToResponseDTO(JobPosting jobPosting) {
        JobPostingResponseDTO responseDTO = new JobPostingResponseDTO();
        responseDTO.setId(jobPosting.getId());
        responseDTO.setTitle(jobPosting.getTitle());
        responseDTO.setCompany(jobPosting.getCompany());
        responseDTO.setDescription(jobPosting.getDescription());
        responseDTO.setSourceUrl(jobPosting.getSourceUrl());
        responseDTO.setExtractedData(jobPosting.getExtractedData());
        responseDTO.setCreatedAt(jobPosting.getCreatedAt());

        return responseDTO;
    }





}



