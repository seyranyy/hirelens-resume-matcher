import type {
  JobPostingCreateRequest,
  MatchAnalysisCreateRequest,
  MatchAnalysisResponse,
} from "../types/analysis";

const API_BASE_URL = "http://localhost:8080/api";

export const uploadResume = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(`${API_BASE_URL}/resumes`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error("Failed to upload resume.");
  }

  return response.json();
};

export const createJobPosting = async (payload: JobPostingCreateRequest) => {
  const response = await fetch(`${API_BASE_URL}/job-postings`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("Failed to create job posting.");
  }

  return response.json();
};

export const createMatchAnalysis = async (
  payload: MatchAnalysisCreateRequest
): Promise<MatchAnalysisResponse> => {
  const response = await fetch(`${API_BASE_URL}/match-analyses`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("Failed to create match analysis.");
  }

  return response.json();
};