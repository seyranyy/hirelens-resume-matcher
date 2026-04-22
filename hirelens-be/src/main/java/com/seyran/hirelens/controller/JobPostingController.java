package com.seyran.hirelens.controller;
import com.seyran.hirelens.dto.request.CreateJobPostingFromUrlRequestDTO;
import com.seyran.hirelens.dto.request.CreateJobPostingRequestDTO;
import com.seyran.hirelens.dto.response.JobPostingResponseDTO;
import com.seyran.hirelens.entity.JobPosting;
import com.seyran.hirelens.service.JobPostingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;


@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping
    public List<JobPostingResponseDTO> getAllJobPostings() {
        return jobPostingService.getAllJobPostings();
    }

    @GetMapping("/{id}")
    public JobPostingResponseDTO getJobPostingById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id);
    }

    @PostMapping
    public JobPostingResponseDTO createJobPosting(@RequestBody CreateJobPostingRequestDTO requestDTO) {
        return jobPostingService.createJobPosting(requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
    }
}

