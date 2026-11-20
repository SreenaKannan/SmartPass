import { useEffect, useState } from "react";
import api from "../services/api";

export default function RecommendationsCard() {
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadRecommendations = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await api.get("/api/ai-hub/recommendations");
        setRecommendations(response.data.recommendations || []);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to load recommendations");
      } finally {
        setLoading(false);
      }
    };

    loadRecommendations();
  }, []);

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 1:
        return "danger";
      case 2:
        return "warning";
      case 3:
        return "info";
      default:
        return "secondary";
    }
  };

  const getPriorityLabel = (priority) => {
    switch (priority) {
      case 1:
        return "High Priority";
      case 2:
        return "Important";
      case 3:
        return "General";
      default:
        return "Info";
    }
  };

  return (
    <div className="ai-hub-card card h-100">
      <div className="card-body">
        <h5 className="card-title">💡 Smart Recommendations</h5>
        <p className="card-text text-secondary small">
          Personalized recommendations based on your SmartPass data.
        </p>

        {loading && (
          <div className="text-center py-3">
            <div className="spinner-border spinner-border-sm" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}

        {error && (
          <div className="alert alert-danger alert-sm mb-0" role="alert">
            {error}
          </div>
        )}

        {!loading && recommendations.length === 0 && (
          <div className="alert alert-info alert-sm mb-0">
            <small>No recommendations at this time. Keep using SmartPass!</small>
          </div>
        )}

        {!loading && recommendations.length > 0 && (
          <div className="recommendations-list">
            {recommendations.map((rec, idx) => (
              <div
                key={idx}
                className="recommendation-item mb-3 p-3 border rounded"
                style={{
                  borderLeft: `4px solid var(--bs-${getPriorityColor(rec.priority)})`,
                }}
              >
                <div className="d-flex justify-content-between align-items-start mb-2">
                  <h6 className="mb-0">{rec.title}</h6>
                  <span className={`badge bg-${getPriorityColor(rec.priority)}`}>
                    {getPriorityLabel(rec.priority)}
                  </span>
                </div>
                <p className="small text-secondary mb-2">{rec.description}</p>
                <div className="d-flex gap-2 align-items-center">
                  <small className="text-muted">
                    {rec.type === "RULE_BASED"
                      ? "🔧 Smart System"
                      : "🤖 AI-Assisted"}
                  </small>
                  {rec.actionUrl && (
                    <a
                      href={rec.actionUrl}
                      className="btn btn-sm btn-outline-primary"
                    >
                      View
                    </a>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
