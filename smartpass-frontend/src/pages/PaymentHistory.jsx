import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";
import { STATUS_STYLE, formatDateTime, formatINR, purposeLabel } from "../utils/format";
import "../styles/payment.css";

export default function PaymentHistory() {
    const navigate = useNavigate();
    const [payments, setPayments] = useState(null);
    const [cards, setCards] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        Promise.all([api.get("/api/payments"), api.get("/api/cards")])
            .then(([p, c]) => { setPayments(p.data); setCards(c.data); })
            .catch(() => setError("We couldn't load your payments. Please try again."));
    }, []);

    // an application is "paid" once any successful application-fee payment exists
    // (older payments without a purpose count as the application fee)
    const paidCardIds = new Set(
        (payments || [])
            .filter((p) => p.status === "SUCCESS" && (!p.purpose || p.purpose === "application"))
            .map((p) => p.cardId)
    );
    const due = cards.filter((c) => c.status === "Pending" && !paidCardIds.has(c.cardId));
    const history = (payments || []).filter((p) => p.status !== "INITIATED");

    return (
        <>
            <Navbar />
            <main className="page-shell">
                <div className="mb-4">
                    <div className="section-eyebrow">Payments</div>
                    <h1 className="h2 fw-bold mb-1">Payments &amp; receipts</h1>
                    <p className="text-secondary mb-0">Pay what&apos;s due and keep a record of everything you&apos;ve paid.</p>
                </div>

                {error && <div className="alert alert-danger">{error}</div>}
                {!payments && !error && <p className="text-secondary">Loading&hellip;</p>}

                {due.length > 0 && (
                    <section className="mb-4">
                        <h5 className="fw-bold mb-3">Payment due</h5>
                        <div className="row g-3">
                            {due.map((c) => (
                                <div className="col-md-6" key={c.cardId}>
                                    <div className="card due-card p-3 d-flex flex-row justify-content-between align-items-center gap-3">
                                        <div>
                                            <div className="fw-semibold">{c.passType} {c.transportType} pass &middot; {formatINR(c.fareAmount)}</div>
                                            <div className="text-secondary small">{c.sourceStation} &rarr; {c.destinationStation}</div>
                                            <div className="text-secondary small">Your application is submitted. Pay the fee so the administrator can review it.</div>
                                        </div>
                                        <button className="btn btn-success text-nowrap"
                                            onClick={() => navigate("/payment", { state: { cardId: c.cardId, purpose: "application" } })}>
                                            Pay now
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>
                )}

                {payments && (
                    <section>
                        <h5 className="fw-bold mb-3">Payment history</h5>
                        {history.length === 0 ? (
                            <div className="card p-4 text-center text-secondary">
                                No payments yet. Apply for a pass and your payments will show up here.
                                <div className="mt-3"><Link className="btn btn-primary btn-sm" to="/apply">Apply for a pass</Link></div>
                            </div>
                        ) : (
                            <div className="table-responsive">
                                <table className="table align-middle">
                                    <thead>
                                        <tr><th>Date</th><th>For</th><th>Method</th><th>Amount</th><th>Status</th><th></th></tr>
                                    </thead>
                                    <tbody>
                                        {history.map((p) => {
                                            const st = STATUS_STYLE[p.status] || { label: p.status, cls: "bg-secondary" };
                                            return (
                                                <tr key={p.paymentId}>
                                                    <td className="small">{p.paidAt || p.createdAt ? formatDateTime(p.paidAt || p.createdAt) : (p.paymentDate || "-")}</td>
                                                    <td>
                                                        <div className="fw-semibold small">{purposeLabel(p.purpose)}</div>
                                                        <div className="text-secondary small">{p.route}</div>
                                                    </td>
                                                    <td className="small">{p.paymentMethod ? `${p.paymentMethod}${p.methodDetail ? " \u00B7 " + p.methodDetail : ""}` : "-"}</td>
                                                    <td className="fw-semibold">{formatINR(p.amount)}</td>
                                                    <td><span className={`badge ${st.cls}`}>{st.label}</span></td>
                                                    <td className="text-end">
                                                        {p.orderId
                                                            ? <Link className="btn btn-outline-primary btn-sm" to={`/receipt/${p.orderId}`}>Receipt</Link>
                                                            : <span className="text-secondary small">&mdash;</span>}
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </section>
                )}
            </main>
        </>
    );
}
