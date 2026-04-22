package com.seyran.hirelens.repository;

import com.seyran.hirelens.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}