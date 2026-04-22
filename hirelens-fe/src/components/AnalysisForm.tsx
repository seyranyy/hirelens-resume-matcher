import { useState } from "react";
import {
  createJobPosting,
  createMatchAnalysis,
  uploadResume,
} from "../api/hirelensApi";
import type { MatchAnalysisResponse } from "../types/analysis";
import AnalysisResultCard from "./AnalysisResultCard";

const fieldStyle = {
  width: "100%",
  padding: "16px 18px",
  borderRadius: "16px",
  border: "1px solid rgba(255,255,255,0.12)",
  background: "rgba(255,255,255,0.06)",
  color: "white",
  outline: "none",
  boxShadow: "inset 0 1px 0 rgba(255,255,255,0.03)",
};

const AnalysisForm = () => {
  const [resumeFile, setResumeFile] = useState<File | null>(null);
  const [title, setTitle] = useState("");
  const [company, setCompany] = useState("");
  const [description, setDescription] = useState("");
  const [result, setResult] = useState<MatchAnalysisResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [statusMessage, setStatusMessage] = useState("");

  const handleAnalyze = async () => {
    if (loading) return;

    if (!resumeFile) {
      setError("Please select a resume file.");
      return;
    }

    if (!title.trim() || !company.trim() || !description.trim()) {
      setError("Please fill in all job posting fields.");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setStatusMessage("Uploading resume...");

      const uploadedResume = await uploadResume(resumeFile);

      setStatusMessage("Creating job posting...");

      const createdJobPosting = await createJobPosting({
        title: title.trim(),
        company: company.trim(),
        description: description.trim(),
      });

      setStatusMessage("Running AI analysis...");

      const analysisResult = await createMatchAnalysis({
        resumeId: uploadedResume.id,
        jobPostingId: createdJobPosting.id,
      });

      setResult(analysisResult);
      setStatusMessage("Analysis completed.");

      setTimeout(() => {
        setStatusMessage("");
      }, 2000);
    } catch (err) {
      console.error(err);
      setError("Analysis could not be completed this time. Please try again.");
      setStatusMessage("");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: "100%", margin: "0 auto", textAlign: "left" }}>
      <h1
        style={{
          marginBottom: "12px",
          fontSize: "56px",
          lineHeight: 1,
          fontWeight: 800,
          letterSpacing: "-1.5px",
          color: "white",
        }}
      >
        HireLens Analysis
      </h1>

      <p
        style={{
          marginBottom: "32px",
          fontSize: "22px",
          lineHeight: 1.5,
          color: "rgba(255,255,255,0.72)",
        }}
      >
        Upload a resume and compare it with a job posting.
      </p>

      <div style={{ display: "grid", gap: "22px" }}>
        <div style={{ display: "grid", gap: "10px" }}>
          <label
            style={{
              fontSize: "18px",
              fontWeight: 600,
              color: "rgba(255,255,255,0.92)",
            }}
          >
            Resume File
          </label>

          <div
            style={{
              display: "flex",
              alignItems: "center",
              gap: "14px",
              flexWrap: "wrap",
            }}
          >
            <label
              style={{
                padding: "12px 18px",
                borderRadius: "14px",
                border: "1px solid rgba(255,255,255,0.12)",
                background:
                  "linear-gradient(135deg, rgba(59,130,246,0.2), rgba(139,92,246,0.16))",
                color: "white",
                fontWeight: 600,
                cursor: loading ? "not-allowed" : "pointer",
              }}
            >
              Dosya Seç
              <input
                type="file"
                accept=".pdf,.doc,.docx,.txt"
                disabled={loading}
                style={{ display: "none" }}
                onChange={(event) =>
                  setResumeFile(
                    event.target.files ? event.target.files[0] : null
                  )
                }
              />
            </label>

            <span style={{ color: "rgba(255,255,255,0.72)" }}>
              {resumeFile ? resumeFile.name : "Dosya seçilmedi"}
            </span>
          </div>
        </div>

        <div style={{ display: "grid", gap: "10px" }}>
          <label
            style={{
              fontSize: "18px",
              fontWeight: 600,
              color: "rgba(255,255,255,0.92)",
            }}
          >
            Job Title
          </label>
          <input
            type="text"
            value={title}
            disabled={loading}
            onChange={(event) => setTitle(event.target.value)}
            style={fieldStyle}
            placeholder="Enter job title"
          />
        </div>

        <div style={{ display: "grid", gap: "10px" }}>
          <label
            style={{
              fontSize: "18px",
              fontWeight: 600,
              color: "rgba(255,255,255,0.92)",
            }}
          >
            Company
          </label>
          <input
            type="text"
            value={company}
            disabled={loading}
            onChange={(event) => setCompany(event.target.value)}
            style={fieldStyle}
            placeholder="Enter company name"
          />
        </div>

        <div style={{ display: "grid", gap: "10px" }}>
          <label
            style={{
              fontSize: "18px",
              fontWeight: 600,
              color: "rgba(255,255,255,0.92)",
            }}
          >
            Job Description
          </label>
          <textarea
            value={description}
            disabled={loading}
            onChange={(event) => setDescription(event.target.value)}
            rows={8}
            style={{
              ...fieldStyle,
              minHeight: "180px",
              resize: "vertical",
            }}
            placeholder="Paste the job description here"
          />
        </div>

        <button
          onClick={handleAnalyze}
          disabled={loading}
          style={{
            width: "fit-content",
            marginTop: "8px",
            padding: "16px 24px",
            border: "none",
            borderRadius: "16px",
            background: "linear-gradient(135deg, #3b82f6, #8b5cf6)",
            color: "white",
            fontSize: "16px",
            fontWeight: 700,
            cursor: loading ? "not-allowed" : "pointer",
            opacity: loading ? 0.72 : 1,
            boxShadow: "0 12px 30px rgba(59, 130, 246, 0.28)",
          }}
        >
          {loading ? "Analyzing..." : "Analyze Resume"}
        </button>
      </div>

      {statusMessage && (
        <p style={{ marginTop: "16px", opacity: 0.85 }}>{statusMessage}</p>
      )}

      {error && (
        <p style={{ color: "tomato", marginTop: "20px", fontWeight: 600 }}>
          {error}
        </p>
      )}

      <AnalysisResultCard result={result} />
    </div>
  );
};

export default AnalysisForm;