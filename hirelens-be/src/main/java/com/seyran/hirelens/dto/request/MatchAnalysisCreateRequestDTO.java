package com.seyran.hirelens.dto.request;

public class MatchAnalysisCreateRequestDTO {

    private Long resumeId;
    private Long jobPostingId;

    public MatchAnalysisCreateRequestDTO() {
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public Long getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(Long jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
}