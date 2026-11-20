export default function RiskBadge({ level = "UNAVAILABLE", score }) {
  const color = level === "HIGH" ? "danger" : level === "MEDIUM" ? "warning" : level === "LOW" ? "success" : "secondary";
  return <span className={`badge text-bg-${color}`}>{level}{score !== undefined ? ` · ${score}/100` : ""}</span>;
}
