package com.seyran.hirelens.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_analyses")
public class MatchAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Auto-generated ID by DB
    private Long id;

    @ManyToOne(optional = false) // Many analyses can belong to one resume
    @JoinColumn(name = "resume_id") // Foreign key column for Resume
    private Resume resume;

    @ManyToOne(optional = false) // Many analyses can belong to one job posting
    @JoinColumn(name = "job_posting_id") // Foreign key column for JobPosting
    private JobPosting jobPosting;

    @Column(nullable = false)
    private Double score;

    @Lob // Can store long text
    private String matchedSkills;

    @Lob
    private String missingSkills;

    @Lob
    private String analysisSummary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public MatchAnalysis() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Resume getResume() {
        return resume;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public Double getScore() {
        return score;
    }

    public String getMatchedSkills() {
        return matchedSkills;
    }

    public String getMissingSkills() {
        return missingSkills;
    }

    public String getAnalysisSummary() {
        return analysisSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setMatchedSkills(String matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }

    public void setAnalysisSummary(String analysisSummary) {
        this.analysisSummary = analysisSummary;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}