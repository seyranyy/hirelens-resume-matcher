package com.seyran.hirelens.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Auto-generated ID by DB
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Lob// Can store long text
    private String originalText;

    @Lob
    private String extractedData;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Resume() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getExtractedData() {
        return extractedData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public void setExtractedData(String extractedData) {
        this.extractedData = extractedData;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}