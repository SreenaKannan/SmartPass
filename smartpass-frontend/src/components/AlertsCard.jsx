import { useEffect, useState } from "react";
import api from "../services/api";

export default function AlertsCard() {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadAlerts = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await api.get("/api/ai-hub/alerts");
        setAlerts(response.data.alerts || []);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to load alerts");
      } finally {
        setLoading(false);
      }
    };

    loadAlerts();
  }, []);

  const getPriorityIcon = (priority) => {
    switch (priority) {
      case "HIGH":
        return "🔴";
      case "MEDIUM":
        return "🟡";
      case "LOW":
        return "🟢";
      default:
        return "⚪";
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case "HIGH":
        return "danger";
      case "MEDIUM":
        return "warning";
      case "LOW":
        return "success";
      default:
        return "secondary";
    }
  };

  const sortedAlerts = [...alerts].sort((a, b) => {
    const priorityOrder = { HIGH: 0, MEDIUM: 1, LOW: 2 };
    return (priorityOrder[a.priority] || 3) - (priorityOrder[b.priority] || 3);
  });

  return (
    <div className="ai-hub-card card">
      <div className="card-body">
        <h5 className="card-title">🚨 Smart Alerts</h5>
        <p className="card-text text-secondary small">
          Important actions requiring your attention.
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

        {!loading && alerts.length === 0 && (
          <div className="alert alert-success alert-sm mb-0">
            <small>✨ All clear! No urgent alerts.</small>
          </div>
        )}

        {!loading && sortedAlerts.length > 0 && (
          <div className="alerts-list">
            {sortedAlerts.map((alert, idx) => (
              <div
                key={idx}
                className={`alert alert-${getPriorityColor(alert.priority)} alert-sm mb-2`}
                role="alert"
              >
                <div className="d-flex justify-content-between align-items-start">
                  <div>
                    <strong>
                      {getPriorityIcon(alert.priority)} {alert.priority} PRIORITY
                    </strong>
                    <p className="small mb-0 mt-1">{alert.message}</p>
                  </div>
                  {alert.actionUrl && (
                    <a
                      href={alert.actionUrl}
                      className={`btn btn-sm btn-outline-${getPriorityColor(alert.priority)}`}
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
