package com.seyran.hirelens.controller;

import com.seyran.hirelens.dto.response.ResumeResponseDTO;
import com.seyran.hirelens.entity.Resume;
import com.seyran.hirelens.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public List<ResumeResponseDTO> getAllResumes() {
        return resumeService.getAllResumes();
    }

    @GetMapping("/{id}")
    public ResumeResponseDTO getResumeById(@PathVariable Long id) {
        return resumeService.getResumeById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeResponseDTO uploadResume(@RequestParam("file") MultipartFile file) {
        return resumeService.uploadResume(file);
    }

    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
    }
}