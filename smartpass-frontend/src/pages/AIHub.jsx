import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import api from "../services/api";
import ReviewApplicationCard from "../components/ReviewApplicationCard";
import StatusExplainerCard from "../components/StatusExplainerCard";
import RecommendationsCard from "../components/RecommendationsCard";
import InsightsCard from "../components/InsightsCard";
import AlertsCard from "../components/AlertsCard";
import "../styles/ai-hub.css";

export default function AIHub() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Load initial data
    setLoading(false);
  }, []);

  if (error) {
    return (
      <>
        <Navbar />
        <main className="page-shell">
          <div className="alert alert-danger">
            <strong>Error:</strong> {error}
          </div>
        </main>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <main className="page-shell">
        {/* AI Hub Header */}
        <section className="ai-hub-header mb-5">
          <div className="ai-hub-header-content">
            <h1 className="ai-hub-title">🤖 SmartPass AI Hub</h1>
            <p className="ai-hub-subtitle">Your Intelligent Travel Assistant</p>
            <p className="ai-hub-description">
              Get personalized insights, smart recommendations, and real-time alerts for your SmartPass journey.
            </p>
          </div>
        </section>

        {loading && (
          <div className="text-center py-5">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-3">Loading your AI Hub...</p>
          </div>
        )}

        {!loading && (
          <>
            {/* Feature Grid */}
            <div className="row g-4">
              {/* Review Application Card */}
              <div className="col-lg-6">
                <ReviewApplicationCard />
              </div>

              {/* Status Explainer Card */}
              <div className="col-lg-6">
                <StatusExplainerCard />
              </div>

              {/* Alerts Card - Full Width */}
              <div className="col-12">
                <AlertsCard />
              </div>

              {/* Recommendations Card */}
              <div className="col-lg-6">
                <RecommendationsCard />
              </div>

              {/* Insights Card */}
              <div className="col-lg-6">
                <InsightsCard />
              </div>
            </div>
          </>
        )}
      </main>
    </>
  );
}
