type SkillBadgeListProps = {
    value: string;
    variant?: "matched" | "missing";
  };
  
  const SkillBadgeList = ({
    value,
    variant = "matched",
  }: SkillBadgeListProps) => {
    const skills = value
      .split(",")
      .map((skill) => skill.trim())
      .filter(Boolean)
      .filter((skill) => skill.toLowerCase() !== "none");
  
    if (skills.length === 0) {
      return <span style={{ opacity: 0.7 }}>None</span>;
    }
  
    const styles =
      variant === "matched"
        ? {
            backgroundColor: "rgba(34, 197, 94, 0.12)",
            border: "1px solid rgba(34, 197, 94, 0.35)",
            color: "#86efac",
          }
        : {
            backgroundColor: "rgba(239, 68, 68, 0.12)",
            border: "1px solid rgba(239, 68, 68, 0.35)",
            color: "#fca5a5",
          };
  
    return (
      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          gap: "10px",
          marginTop: "8px",
        }}
      >
        {skills.map((skill) => (
          <span
            key={skill}
            style={{
              padding: "8px 12px",
              borderRadius: "999px",
              fontSize: "14px",
              fontWeight: 600,
              ...styles,
            }}
          >
            {skill}
          </span>
        ))}
      </div>
    );
  };
  
  export default SkillBadgeList;