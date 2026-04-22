type ScoreCircleProps = {
  score: number;
};

const ScoreCircle = ({ score }: ScoreCircleProps) => {
  const normalizedScore = Math.max(0, Math.min(100, score));
  const radius = 62;
  const strokeWidth = 12;
  const circumference = 2 * Math.PI * radius;
  const progress = (normalizedScore / 100) * circumference;

  return (
    <div
      style={{
        width: "170px",
        minWidth: "170px",
        position: "relative",
        flexShrink: 0,
      }}
    >
      <svg width="170" height="170" viewBox="0 0 170 170">
        <circle
          cx="85"
          cy="85"
          r={radius}
          stroke="rgba(255,255,255,0.12)"
          strokeWidth={strokeWidth}
          fill="none"
        />
        <circle
          cx="85"
          cy="85"
          r={radius}
          stroke="#60a5fa"
          strokeWidth={strokeWidth}
          fill="none"
          strokeDasharray={circumference}
          strokeDashoffset={circumference - progress}
          strokeLinecap="round"
          transform="rotate(-90 85 85)"
        />
      </svg>

      <div
        style={{
          position: "absolute",
          inset: 0,
          display: "grid",
          placeItems: "center",
          textAlign: "center",
          paddingTop: "2px",
        }}
      >
        <div>
          <div
            style={{
              fontSize: "30px",
              fontWeight: 700,
              lineHeight: 1,
              letterSpacing: "-0.5px",
            }}
          >
            {normalizedScore}
          </div>
          <div
            style={{
              marginTop: "6px",
              fontSize: "12px",
              opacity: 0.75,
              lineHeight: 1.2,
            }}
          >
            Match Score
          </div>
        </div>
      </div>
    </div>
  );
};

export default ScoreCircle;