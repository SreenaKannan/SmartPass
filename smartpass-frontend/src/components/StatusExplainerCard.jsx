import { useEffect, useState } from "react";
import api from "../services/api";

export default function StatusExplainerCard() {
  const [cards, setCards] = useState([]);
  const [selectedCardId, setSelectedCardId] = useState(null);
  const [explanation, setExplanation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Load user's cards
  useEffect(() => {
    const loadCards = async () => {
      try {
        const response = await api.get("/api/cards");
        setCards(response.data);
        if (response.data.length > 0) {
          setSelectedCardId(response.data[0].cardId);
        }
      } catch (err) {
        setError("Failed to load your cards");
      }
    };

    loadCards();
  }, []);

  // Load explanation when card is selected
  useEffect(() => {
    if (selectedCardId) {
      loadExplanation(selectedCardId);
    }
  }, [selectedCardId]);

  const loadExplanation = async (cardId) => {
    setLoading(true);
    setError(null);
    setExplanation(null);

    try {
      const response = await api.get(`/api/ai-hub/application-status/${cardId}`);
      setExplanation(response.data);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load explanation");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-hub-card card h-100">
      <div className="card-body">
        <h5 className="card-title">😊 Explain My Status</h5>
        <p className="card-text text-secondary small">
          Understand your application status in simple terms.
        </p>

        {cards.length === 0 ? (
          <div className="alert alert-info alert-sm mb-0">
            <small>You don't have any applications yet. Apply for a pass to get started!</small>
          </div>
        ) : (
          <>
            <div className="mb-3">
              <label className="form-label small">Select Application</label>
              <select
                className="form-select form-select-sm"
                value={selectedCardId || ""}
                onChange={(e) => setSelectedCardId(Number(e.target.value))}
                disabled={loading}
              >
                {cards.map((card) => (
                  <option key={card.cardId} value={card.cardId}>
                    {card.sourceStation} → {card.destinationStation} ({card.status})
                  </option>
                ))}
              </select>
            </div>

            {error && (
              <div className="alert alert-danger alert-sm mb-3" role="alert">
                {error}
              </div>
            )}

            {loading && (
              <div className="text-center">
                <small className="text-secondary">Loading explanation...</small>
              </div>
            )}

            {explanation && !loading && (
              <div className="status-explanation">
                <div className="mb-3 pb-3 border-bottom">
                  <p className="small fw-bold mb-1">Status: <span className="badge bg-info">{explanation.status}</span></p>
                </div>

                <div className="mb-3 pb-3 border-bottom">
                  <p className="small mb-0">
                    <strong>What this means:</strong>
                  </p>
                  <p className="small text-secondary mb-0">{explanation.explanation}</p>
                </div>

                {explanation.reasons && explanation.reasons.length > 0 && (
                  <div className="mb-3 pb-3 border-bottom">
                    <p className="small fw-bold mb-1">Why:</p>
                    <ul className="small mb-0 ps-3">
                      {explanation.reasons.map((reason, idx) => (
                        <li key={idx} className="text-secondary">{reason}</li>
                      ))}
                    </ul>
                  </div>
                )}

                {typeof explanation.queuePosition === "number" && explanation.queuePosition > 0 && (
                  <div className="mb-3 pb-3 border-bottom">
                    <p className="small mb-0">
                      <strong>Queue position:</strong>{" "}
                      <span className="text-secondary">
                        {explanation.queuePosition} application(s) ahead of yours
                      </span>
                    </p>
                  </div>
                )}

                {explanation.actionItems && explanation.actionItems.length > 0 && (
                  <div>
                    <p className="small mb-1">
                      <strong>What you can do:</strong>
                    </p>
                    <ul className="small text-secondary mb-0 ps-3">
                      {explanation.actionItems.map((action, idx) => (
                        <li key={idx}>{action}</li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
