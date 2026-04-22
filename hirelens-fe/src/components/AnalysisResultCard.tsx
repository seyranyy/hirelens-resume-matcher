import type { MatchAnalysisResponse } from "../types/analysis";
import ScoreCircle from "./ScoreCircle";
import SkillBadgeList from "./SkillBadgeList";

type AnalysisResultCardProps = {
  result: MatchAnalysisResponse | null;
};

const infoCardStyle = {
  padding: "14px",
  borderRadius: "16px",
  backgroundColor: "rgba(255,255,255,0.03)",
  border: "1px solid rgba(255,255,255,0.06)",
  minWidth: 0,
};

const infoValueStyle = {
  display: "block",
  marginTop: "6px",
  wordBreak: "break-word" as const,
  overflowWrap: "anywhere" as const,
  lineHeight: 1.5,
};

const AnalysisResultCard = ({ result }: AnalysisResultCardProps) => {
  if (!result) {
    return null;
  }

  return (
    <div
      style={{
        marginTop: "32px",
        padding: "28px",
        border: "1px solid #262626",
        borderRadius: "24px",
        background:
          "linear-gradient(180deg, rgba(18,18,22,1) 0%, rgba(10,10,14,1) 100%)",
        boxShadow: "0 20px 50px rgba(0, 0, 0, 0.25)",
      }}
    >
      <h2 style={{ marginTop: 0, marginBottom: "24px", fontSize: "36px" }}>
        Analysis Result
      </h2>

      <div
        style={{
          display: "flex",
          gap: "32px",
          alignItems: "flex-start",
          flexWrap: "wrap",
        }}
      >
        <ScoreCircle score={result.score} />

        <div style={{ flex: 1, minWidth: "280px" }}>
          <div style={{ marginBottom: "20px" }}>
            <strong style={{ display: "block", marginBottom: "6px" }}>
              Matched Skills
            </strong>
            <SkillBadgeList value={result.matchedSkills} variant="matched" />
          </div>

          <div style={{ marginBottom: "20px" }}>
            <strong style={{ display: "block", marginBottom: "6px" }}>
              Missing Skills
            </strong>
            <SkillBadgeList value={result.missingSkills} variant="missing" />
          </div>

          <div
            style={{
              marginBottom: "20px",
              padding: "16px",
              borderRadius: "16px",
              backgroundColor: "rgba(255,255,255,0.03)",
              border: "1px solid rgba(255,255,255,0.06)",
              lineHeight: 1.7,
            }}
          >
            <strong>Summary</strong>
            <p
              style={{
                margin: "10px 0 0 0",
                opacity: 0.92,
                wordBreak: "break-word",
                overflowWrap: "anywhere",
              }}
            >
              {result.analysisSummary}
            </p>
          </div>

          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
              gap: "14px",
            }}
          >
            <div style={infoCardStyle}>
              <strong style={{ display: "block", marginBottom: "6px" }}>
                Resume File
              </strong>
              <span style={infoValueStyle}>{result.resumeFileName}</span>
            </div>

            <div style={infoCardStyle}>
              <strong style={{ display: "block", marginBottom: "6px" }}>
                Job Title
              </strong>
              <span style={infoValueStyle}>{result.jobPostingTitle}</span>
            </div>

            <div style={infoCardStyle}>
              <strong style={{ display: "block", marginBottom: "6px" }}>
                Company
              </strong>
              <span style={infoValueStyle}>{result.company}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnalysisResultCard;