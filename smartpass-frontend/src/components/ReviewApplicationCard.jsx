import { useState } from "react";
import api from "../services/api";

export default function ReviewApplicationCard() {
  const [formData, setFormData] = useState({
    sourceStation: "",
    destinationStation: "",
    passType: "",
    transportType: "",
  });
  const [review, setReview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleReview = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setReview(null);

    try {
      const response = await api.post("/api/ai-hub/review-application", formData);
      setReview(response.data);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to review application");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-hub-card card h-100">
      <div className="card-body">
        <h5 className="card-title">📝 Review My Application</h5>
        <p className="card-text text-secondary small">
          Get helpful suggestions before submitting your application.
        </p>

        <form onSubmit={handleReview}>
          <div className="mb-3">
            <label className="form-label small">Transport Type</label>
            <select
              className="form-select form-select-sm"
              name="transportType"
              value={formData.transportType}
              onChange={handleChange}
              disabled={loading}
            >
              <option value="">Select...</option>
              <option value="Bus">Bus</option>
              <option value="Metro">Metro</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label small">From Station</label>
            <input
              type="text"
              className="form-control form-control-sm"
              name="sourceStation"
              placeholder="e.g. Central Station"
              value={formData.sourceStation}
              onChange={handleChange}
              disabled={loading}
            />
          </div>

          <div className="mb-3">
            <label className="form-label small">To Station</label>
            <input
              type="text"
              className="form-control form-control-sm"
              name="destinationStation"
              placeholder="e.g. City Square"
              value={formData.destinationStation}
              onChange={handleChange}
              disabled={loading}
            />
          </div>

          <div className="mb-3">
            <label className="form-label small">Pass Duration</label>
            <select
              className="form-select form-select-sm"
              name="passType"
              value={formData.passType}
              onChange={handleChange}
              disabled={loading}
            >
              <option value="">Select...</option>
              <option value="Monthly">Monthly</option>
              <option value="Quarterly">Quarterly</option>
              <option value="Half-Yearly">Half-Yearly</option>
              <option value="Yearly">Yearly</option>
            </select>
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-sm w-100"
            disabled={loading}
          >
            {loading ? "Reviewing..." : "🤖 Review Application"}
          </button>
        </form>

        {error && (
          <div className="alert alert-danger alert-sm mt-3 mb-0" role="alert">
            {error}
          </div>
        )}

        {review && (
          <div className="mt-3 pt-3 border-top">
            <h6 className="mb-2">🤖 Application Review</h6>

            {review.issues && review.issues.length > 0 && (
              <div className="mb-2">
                <p className="small fw-bold text-danger mb-1">Issues to fix:</p>
                <ul className="small mb-2 ps-3">
                  {review.issues.map((issue, idx) => (
                    <li key={idx} className="text-danger">
                      ⚠️ {issue}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {review.suggestions && review.suggestions.length > 0 && (
              <div className="mb-2">
                <p className="small fw-bold text-info mb-1">Suggestions:</p>
                <ul className="small mb-2 ps-3">
                  {review.suggestions.map((suggestion, idx) => (
                    <li key={idx} className="text-info">
                      💡 {suggestion}
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {review.canProceed && (
              <div className="alert alert-success alert-sm mb-0">
                ✅ Your application looks good! You can proceed.
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
