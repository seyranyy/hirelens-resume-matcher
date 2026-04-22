package com.seyran.hirelens.dto.request;

public class CreateJobPostingFromUrlRequestDTO {

    private String sourceUrl;

    public CreateJobPostingFromUrlRequestDTO() {
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}