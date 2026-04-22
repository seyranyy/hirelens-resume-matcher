export type MatchAnalysisResponse = {
  id: number;
  score: number;
  matchedSkills: string;
  missingSkills: string;
  analysisSummary: string;
  createdAt: string;
  resumeId: number;
  resumeFileName: string;
  jobPostingId: number;
  jobPostingTitle: string;
  company: string;
};

export type JobPostingCreateRequest = {
  title: string;
  company: string;
  description: string;
};

export type MatchAnalysisCreateRequest = {
  resumeId: number;
  jobPostingId: number;
};