package com.seyran.hirelens.controller;

import com.seyran.hirelens.dto.request.MatchAnalysisCreateRequestDTO;
import com.seyran.hirelens.dto.response.MatchAnalysisResponseDTO;
import com.seyran.hirelens.service.MatchAnalysisService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/match-analyses")
public class MatchAnalysisController {

    private final MatchAnalysisService matchAnalysisService;

    public MatchAnalysisController(MatchAnalysisService matchAnalysisService) {
        this.matchAnalysisService = matchAnalysisService;
    }

    @GetMapping
    public List<MatchAnalysisResponseDTO> getAllMatchAnalyses() {
        return matchAnalysisService.getAllMatchAnalyses();
    }

    @GetMapping("/{id}")
    public MatchAnalysisResponseDTO getMatchAnalysisById(@PathVariable Long id) {
        return matchAnalysisService.getMatchAnalysisById(id);
    }

    @PostMapping
    public MatchAnalysisResponseDTO createMatchAnalysis(@RequestBody MatchAnalysisCreateRequestDTO requestDTO) {
        return matchAnalysisService.createMatchAnalysis(requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMatchAnalysis(@PathVariable Long id) {
        matchAnalysisService.deleteMatchAnalysis(id);
    }
}