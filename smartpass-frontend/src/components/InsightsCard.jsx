import { useEffect, useState } from "react";
import api from "../services/api";

export default function InsightsCard() {
  const [insights, setInsights] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadInsights = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await api.get("/api/ai-hub/insights");
        setInsights(response.data);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to load insights");
      } finally {
        setLoading(false);
      }
    };

    loadInsights();
  }, []);

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  return (
    <div className="ai-hub-card card h-100">
      <div className="card-body">
        <h5 className="card-title">📊 My SmartPass Insights</h5>
        <p className="card-text text-secondary small">
          Your personal SmartPass activity and statistics.
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

        {!loading && insights && (
          <div className="insights-grid">
            <div className="insight-stat mb-3">
              <div className="stat-icon">💳</div>
              <div className="stat-content">
                <div className="stat-label">Active Cards</div>
                <div className="stat-value">{insights.activeCards}</div>
                <small className="text-muted">of {insights.totalCards} total</small>
              </div>
            </div>

            <div className="insight-stat mb-3">
              <div className="stat-icon">↻</div>
              <div className="stat-content">
                <div className="stat-label">Completed Renewals</div>
                <div className="stat-value">{insights.completedRenewals}</div>
              </div>
            </div>

            <div className="insight-stat mb-3">
              <div className="stat-icon">⏳</div>
              <div className="stat-content">
                <div className="stat-label">Pending Applications</div>
                <div className="stat-value">{insights.pendingApplications}</div>
              </div>
            </div>

            <div className="insight-stat mb-3">
              <div className="stat-icon">✓</div>
              <div className="stat-content">
                <div className="stat-label">Successful Payments</div>
                <div className="stat-value">{insights.totalSuccessfulPayments}</div>
              </div>
            </div>

            <div className="insight-stat mb-3">
              <div className="stat-icon">📅</div>
              <div className="stat-content">
                <div className="stat-label">Next Expiry</div>
                <div className="stat-value text-info">
                  {formatDate(insights.nextExpiryDate)}
                </div>
              </div>
            </div>

            <div className="insight-stat mb-3">
              <div className="stat-icon">🎯</div>
              <div className="stat-content">
                <div className="stat-label">Current Status</div>
                <div className="stat-value text-info">{insights.applicationStatus}</div>
              </div>
            </div>

            <div className="alert alert-info alert-sm mt-3 mb-0">
              <strong>Next Action:</strong> {insights.nextImportantAction}
            </div>
          </div>
        )}

        {!loading && !insights && !error && (
          <div className="alert alert-warning alert-sm mb-0">
            <small>No data available yet. Create your first application!</small>
          </div>
        )}
      </div>
    </div>
  );
}
