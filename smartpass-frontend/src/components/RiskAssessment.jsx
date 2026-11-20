import RiskBadge from "./RiskBadge";
export default function RiskAssessment({ card }) { return <div className="small"><RiskBadge level={card.riskLevel} score={card.riskScore}/><div className="mt-2"><strong>Flags:</strong> {card.riskFlags || "None"}</div><div className="text-muted mt-1">{card.riskExplanation}</div></div>; }
