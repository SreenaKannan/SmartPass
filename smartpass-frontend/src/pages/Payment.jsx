import { useCallback, useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";
import PaymentReceipt from "../components/PaymentReceipt";
import { formatINR, purposeLabel } from "../utils/format";
import "../styles/payment.css";

const BANKS = [
    "State Bank of India", "HDFC Bank", "ICICI Bank", "Axis Bank",
    "Kotak Mahindra Bank", "Punjab National Bank", "Bank of Baroda", "Canara Bank",
];
const METHODS = [
    { id: "UPI", icon: "\u21C4", label: "UPI" },
    { id: "CARD", icon: "\u25AD", label: "Card" },
    { id: "NETBANKING", icon: "\u2302", label: "Net Banking" },
];
const REDIRECT_SECONDS = 10;

const errorText = (e, fallback) => e.response?.data?.message || fallback;

const luhn = (digits) => {
    let sum = 0;
    let dbl = false;
    for (let i = digits.length - 1; i >= 0; i--) {
        let d = Number(digits[i]);
        if (dbl) { d *= 2; if (d > 9) d -= 9; }
        sum += d;
        dbl = !dbl;
    }
    return sum % 10 === 0;
};

const cardBrand = (digits) => {
    if (digits.startsWith("4")) return "Visa";
    const p2 = Number(digits.slice(0, 2));
    const p4 = Number(digits.slice(0, 4));
    if ((p2 >= 51 && p2 <= 55) || (p4 >= 2221 && p4 <= 2720)) return "Mastercard";
    if (p2 === 34 || p2 === 37) return "Amex";
    if (digits.startsWith("508") || [60, 65, 81, 82].includes(p2)) return "RuPay";
    return "";
};

const mmss = (s) => `${String(Math.floor(s / 60)).padStart(2, "0")}:${String(s % 60).padStart(2, "0")}`;

export default function Payment() {
    const location = useLocation();
    const navigate = useNavigate();
    const { cardId, purpose } = location.state || {};

    const [order, setOrder] = useState(null);
    const [loadError, setLoadError] = useState("");
    const [step, setStep] = useState("form"); // form | otp | processing | result
    const [method, setMethod] = useState("UPI");
    const [form, setForm] = useState({ upiId: "", cardNumber: "", cardHolder: "", expiry: "", cvv: "", bank: "", otp: "" });
    const [error, setError] = useState("");
    const [deadline, setDeadline] = useState(null);
    const [secondsLeft, setSecondsLeft] = useState(null);
    const [autoRedirect, setAutoRedirect] = useState(false);
    const [redirectIn, setRedirectIn] = useState(REDIRECT_SECONDS);
    const started = useRef(false);

    const set = (name) => (e) => setForm((f) => ({ ...f, [name]: e.target.value }));

    // ----- open (or resume) the checkout session -----
    const start = useCallback(async () => {
        setLoadError("");
        setOrder(null);
        setError("");
        try {
            const res = await api.post("/api/payments/initiate", { cardId, purpose });
            setOrder(res.data);
            setDeadline(Date.now() + (res.data.secondsRemaining ?? 600) * 1000);
            setSecondsLeft(res.data.secondsRemaining ?? 600);
            setStep("form");
        } catch (e) {
            setLoadError(errorText(e, "We could not start the payment. Please try again."));
        }
    }, [cardId, purpose]);

    useEffect(() => {
        if (!cardId || !purpose) {
            navigate("/payments", { replace: true });
            return;
        }
        if (started.current) return;
        started.current = true;
        start();
    }, [cardId, purpose, navigate, start]);

    // ----- session countdown -----
    useEffect(() => {
        if (!deadline || (step !== "form" && step !== "otp")) return;
        const tick = () => setSecondsLeft(Math.max(0, Math.round((deadline - Date.now()) / 1000)));
        const t = setInterval(tick, 1000);
        return () => clearInterval(t);
    }, [deadline, step]);

    // ----- auto redirect to dashboard after success -----
    const succeeded = step === "result" && order?.status === "SUCCESS";
    useEffect(() => {
        if (!succeeded || !autoRedirect) return;
        const t = setInterval(() => setRedirectIn((s) => s - 1), 1000);
        return () => clearInterval(t);
    }, [succeeded, autoRedirect]);

    useEffect(() => {
        if (succeeded && autoRedirect && redirectIn <= 0) {
            navigate("/dashboard", { replace: true });
        }
    }, [succeeded, autoRedirect, redirectIn, navigate]);

    const expired = secondsLeft === 0 && (step === "form" || step === "otp");
    const needsOtp = method !== "UPI";
    const cardDigits = form.cardNumber.replace(/\D/g, "");

    // ----- client-side checks (the server re-validates everything) -----
    const validate = () => {
        if (method === "UPI") {
            if (!/^[a-zA-Z0-9._-]{2,}@[a-zA-Z][a-zA-Z0-9.-]{1,}$/.test(form.upiId.trim()))
                return "Enter a valid UPI ID, for example name@okaxis.";
        } else if (method === "CARD") {
            if (cardDigits.length < 13 || !luhn(cardDigits)) return "Enter a valid card number.";
            if (!/^[A-Za-z .'-]{2,}$/.test(form.cardHolder.trim())) return "Enter the name printed on the card.";
            const m = /^(0[1-9]|1[0-2])\/(\d{2})$/.exec(form.expiry);
            if (!m) return "Enter the card expiry as MM/YY.";
            const now = new Date();
            const expiry = new Date(2000 + Number(m[2]), Number(m[1]), 1); // first day of the month AFTER expiry
            if (expiry <= new Date(now.getFullYear(), now.getMonth(), 1)) return "This card has expired.";
            if (!/^\d{3,4}$/.test(form.cvv)) return "Enter a valid CVV.";
        } else if (!form.bank) {
            return "Select your bank.";
        }
        return "";
    };

    const submitDetails = (e) => {
        e.preventDefault();
        const problem = validate();
        if (problem) { setError(problem); return; }
        setError("");
        if (needsOtp) {
            setForm((f) => ({ ...f, otp: "" }));
            setStep("otp");
        } else {
            pay("");
        }
    };

    const pay = async (otp) => {
        setStep("processing");
        setError("");
        const body = {
            method, otp,
            upiId: form.upiId.trim(),
            cardNumber: cardDigits,
            cardHolder: form.cardHolder.trim(),
            expiry: form.expiry,
            cvv: form.cvv,
            bank: form.bank,
        };
        try {
            // a short minimum wait so the "processing" screen behaves like a real gateway
            const [res] = await Promise.all([
                api.post(`/api/payments/${order.orderId}/pay`, body),
                new Promise((r) => setTimeout(r, method === "UPI" ? 2800 : 1800)),
            ]);
            setOrder(res.data);
            setForm((f) => ({ ...f, cvv: "", otp: "" }));
            if (res.data.status === "SUCCESS") {
                setRedirectIn(REDIRECT_SECONDS);
                setAutoRedirect(true);
            }
            setStep("result");
        } catch (e) {
            if (e.response?.status === 409) {
                // already processed (e.g. double click / second tab) - show the real outcome
                try {
                    const res = await api.get(`/api/payments/${order.orderId}`);
                    setOrder(res.data);
                    setStep("result");
                    return;
                } catch { /* fall through */ }
            }
            const msg = errorText(e, "The payment could not be completed. Please try again.");
            setError(msg);
            // wrong OTP -> stay on the OTP screen; any other problem (e.g. bad card details) -> back to the form
            setStep(needsOtp && /otp/i.test(msg) ? "otp" : "form");
        }
    };

    const submitOtp = (e) => {
        e.preventDefault();
        if (!/^\d{6}$/.test(form.otp)) { setError("Enter the 6-digit OTP."); return; }
        pay(form.otp);
    };

    const cancelPayment = async () => {
        try { if (order) await api.post(`/api/payments/${order.orderId}/cancel`); } catch { /* leaving anyway */ }
        navigate("/dashboard", { replace: true });
    };

    // =================== render ===================
    if (loadError) {
        return (
            <>
                <Navbar />
                <main className="page-shell pay-shell">
                    <div className="card p-5 text-center" style={{ maxWidth: 520, margin: "0 auto" }}>
                        <h4 className="fw-bold">We can&apos;t start this payment</h4>
                        <p className="text-secondary">{loadError}</p>
                        <div className="d-flex gap-2 justify-content-center">
                            <button className="btn btn-primary" onClick={() => navigate("/dashboard", { replace: true })}>Go to dashboard</button>
                            <button className="btn btn-outline-secondary" onClick={() => navigate("/payments")}>View payments</button>
                        </div>
                    </div>
                </main>
            </>
        );
    }

    if (!order) {
        return (
            <>
                <Navbar />
                <main className="page-shell pay-shell"><div className="processing"><div className="spinner-ring" /><p className="text-secondary">Preparing secure checkout&hellip;</p></div></main>
            </>
        );
    }

    if (step === "result") {
        const ok = order.status === "SUCCESS";
        return (
            <>
                <Navbar />
                <main className="page-shell pay-shell">
                    <PaymentReceipt order={order} />
                    <div className="receipt mt-3">
                        {ok && (
                            <p className="text-center text-secondary small mb-2">
                                {order.purpose === "renewal"
                                    ? "Your pass has been renewed and is active."
                                    : "Your application is now with the administrator for review."}
                            </p>
                        )}
                        {ok && autoRedirect && (
                            <div className="no-print mb-3">
                                <div className="redirect-bar"><div style={{ width: `${(redirectIn / REDIRECT_SECONDS) * 100}%` }} /></div>
                                <div className="d-flex justify-content-between small text-secondary mt-1">
                                    <span>Taking you to your dashboard in {Math.max(redirectIn, 0)}s</span>
                                    <button className="btn btn-link btn-sm p-0" onClick={() => setAutoRedirect(false)}>Stay on this page</button>
                                </div>
                            </div>
                        )}
                        <div className="d-grid gap-2 no-print">
                            {ok ? (
                                <>
                                    <button className="btn btn-primary" onClick={() => navigate("/dashboard", { replace: true })}>Go to dashboard</button>
                                    <div className="d-flex gap-2">
                                        <button className="btn btn-outline-primary flex-fill" onClick={() => navigate("/cards", { replace: true })}>View my cards</button>
                                        <button className="btn btn-outline-secondary flex-fill" onClick={() => window.print()}>Print receipt</button>
                                    </div>
                                </>
                            ) : (
                                <>
                                    {order.status !== "CANCELLED" && (
                                        <button className="btn btn-primary" onClick={() => { started.current = true; start(); }}>Try again</button>
                                    )}
                                    <button className="btn btn-outline-secondary" onClick={() => navigate("/dashboard", { replace: true })}>Back to dashboard</button>
                                </>
                            )}
                        </div>
                    </div>
                </main>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <main className="page-shell pay-shell">
                <div className="mb-4">
                    <div className="section-eyebrow">Secure checkout</div>
                    <h1 className="h2 fw-bold mb-0">Complete your payment</h1>
                </div>

                <div className="row g-4">
                    {/* ---------------- order summary ---------------- */}
                    <div className="col-lg-5 order-lg-2">
                        <aside className="card p-4 order-summary">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="fw-bold mb-0">Order summary</h5>
                                {secondsLeft !== null && (
                                    <span className={`session-timer ${secondsLeft < 60 ? "low" : ""}`} title="Time left to complete this payment">
                                        {"\u23F1"} {mmss(secondsLeft)}
                                    </span>
                                )}
                            </div>
                            <div className="mb-3">
                                <div className="fw-semibold">{purposeLabel(order.purpose)}</div>
                                <div className="text-secondary small">{order.passType} {order.transportType} pass</div>
                                <div className="text-secondary small">{order.route}</div>
                            </div>
                            <div className="amount-line"><span>Pass fare</span><span>{formatINR(order.amount)}</span></div>
                            <div className="amount-line"><span>Convenience fee</span><span>{formatINR(0)}</span></div>
                            <div className="total-line"><span>Total payable</span><span>{formatINR(order.amount)}</span></div>
                            <hr />
                            <div className="secure-note">Order ID: <strong>{order.orderId}</strong></div>
                            <div className="secure-note mt-1">{"\uD83D\uDD12"} Card numbers and CVV are never stored. The amount is set by SmartPass and cannot be changed.</div>
                        </aside>
                    </div>

                    {/* ---------------- payment panel ---------------- */}
                    <div className="col-lg-7 order-lg-1">
                        <div className="card p-4">
                            {expired ? (
                                <div className="text-center py-4">
                                    <h4 className="fw-bold">Session expired</h4>
                                    <p className="text-secondary">For your security, checkout closes after 10 minutes. Nothing was charged.</p>
                                    <button className="btn btn-primary" onClick={() => { started.current = true; start(); }}>Start again</button>
                                </div>
                            ) : step === "processing" ? (
                                <div className="processing">
                                    <div className="spinner-ring" />
                                    <h5 className="fw-bold">{method === "UPI" ? "Waiting for UPI approval\u2026" : "Processing your payment\u2026"}</h5>
                                    <p className="text-secondary mb-0">Please don&apos;t refresh or press the back button.</p>
                                </div>
                            ) : step === "otp" ? (
                                <form onSubmit={submitOtp}>
                                    <h5 className="fw-bold">Authorise payment</h5>
                                    <p className="text-secondary small">
                                        Enter the 6-digit OTP sent by {method === "CARD" ? "your card issuer" : form.bank} to your registered mobile number.
                                    </p>
                                    <input
                                        className="form-control otp-input mb-2"
                                        inputMode="numeric" autoComplete="one-time-code" maxLength={6} autoFocus
                                        value={form.otp}
                                        onChange={(e) => setForm((f) => ({ ...f, otp: e.target.value.replace(/\D/g, "") }))}
                                        placeholder="••••••"
                                    />
                                    <div className="sandbox-note mb-3">Sandbox: the OTP is <code>123456</code>. Three wrong attempts stop the payment.</div>
                                    {error && <div className="alert alert-danger py-2">{error}</div>}
                                    <div className="d-flex gap-2">
                                        <button type="button" className="btn btn-outline-secondary" onClick={() => { setError(""); setStep("form"); }}>Back</button>
                                        <button className="btn btn-success flex-fill">Verify &amp; pay {formatINR(order.amount)}</button>
                                    </div>
                                </form>
                            ) : (
                                <form onSubmit={submitDetails} noValidate>
                                    <div className="pay-tabs">
                                        {METHODS.map((m) => (
                                            <button type="button" key={m.id}
                                                className={`pay-tab ${method === m.id ? "active" : ""}`}
                                                onClick={() => { setMethod(m.id); setError(""); }}>
                                                <span>{m.icon}</span>{m.label}
                                            </button>
                                        ))}
                                    </div>

                                    {method === "UPI" && (
                                        <>
                                            <label className="form-label">UPI ID</label>
                                            <input className="form-control mb-1" placeholder="name@okaxis" autoComplete="off"
                                                value={form.upiId} onChange={set("upiId")} />
                                            <div className="form-text mb-3">You&apos;ll get a request in your UPI app to approve.</div>
                                        </>
                                    )}

                                    {method === "CARD" && (
                                        <>
                                            <label className="form-label d-flex justify-content-between">
                                                <span>Card number</span>
                                                {cardBrand(cardDigits) && <span className="brand-pill">{cardBrand(cardDigits)}</span>}
                                            </label>
                                            <input className="form-control mb-3" inputMode="numeric" autoComplete="cc-number"
                                                placeholder="1234 5678 9012 3456" value={form.cardNumber}
                                                onChange={(e) => {
                                                    const d = e.target.value.replace(/\D/g, "").slice(0, 19);
                                                    setForm((f) => ({ ...f, cardNumber: d.replace(/(.{4})/g, "$1 ").trim() }));
                                                }} />
                                            <label className="form-label">Name on card</label>
                                            <input className="form-control mb-3" autoComplete="cc-name" placeholder="As printed on the card"
                                                value={form.cardHolder} onChange={set("cardHolder")} />
                                            <div className="row g-3 mb-3">
                                                <div className="col-6">
                                                    <label className="form-label">Expiry</label>
                                                    <input className="form-control" inputMode="numeric" autoComplete="cc-exp" placeholder="MM/YY"
                                                        maxLength={5} value={form.expiry}
                                                        onChange={(e) => {
                                                            let d = e.target.value.replace(/\D/g, "").slice(0, 4);
                                                            if (d.length > 2) d = d.slice(0, 2) + "/" + d.slice(2);
                                                            setForm((f) => ({ ...f, expiry: d }));
                                                        }} />
                                                </div>
                                                <div className="col-6">
                                                    <label className="form-label">CVV</label>
                                                    <input className="form-control" type="password" inputMode="numeric" autoComplete="cc-csc"
                                                        placeholder="•••" maxLength={4} value={form.cvv}
                                                        onChange={(e) => setForm((f) => ({ ...f, cvv: e.target.value.replace(/\D/g, "") }))} />
                                                </div>
                                            </div>
                                        </>
                                    )}

                                    {method === "NETBANKING" && (
                                        <>
                                            <label className="form-label">Choose your bank</label>
                                            <div className="bank-grid mb-3">
                                                {BANKS.map((b) => (
                                                    <label key={b} className={`bank-option ${form.bank === b ? "active" : ""}`}>
                                                        <input type="radio" name="bank" checked={form.bank === b} onChange={() => setForm((f) => ({ ...f, bank: b }))} />
                                                        {b}
                                                    </label>
                                                ))}
                                            </div>
                                        </>
                                    )}

                                    <div className="sandbox-note mb-3">
                                        <strong>Sandbox mode &ndash; no real money is charged.</strong>{" "}
                                        UPI: any valid ID (an ID containing <code>fail</code> is declined). Card: <code>4111 1111 1111 1111</code>,
                                        any future expiry and CVV; <code>4000 0000 0000 0002</code> is declined. OTP: <code>123456</code>.
                                    </div>

                                    {error && <div className="alert alert-danger py-2">{error}</div>}

                                    <button className="btn btn-success btn-lg w-100">Pay {formatINR(order.amount)}</button>
                                    <div className="text-center mt-3">
                                        <button type="button" className="btn btn-link text-secondary" onClick={cancelPayment}>Cancel payment</button>
                                    </div>
                                </form>
                            )}
                        </div>
                    </div>
                </div>
            </main>
        </>
    );
}
