import { formatDate, formatDateTime, formatINR, purposeLabel } from "../utils/format";
import "../styles/payment.css";

const HEADLINE = {
    SUCCESS: { icon: "\u2713", title: "Payment successful", cls: "ok" },
    FAILED: { icon: "\u2715", title: "Payment failed", cls: "bad" },
    CANCELLED: { icon: "\u2715", title: "Payment cancelled", cls: "muted" },
    EXPIRED: { icon: "\u23F1", title: "Payment session expired", cls: "muted" },
};

export default function PaymentReceipt({ order }) {
    const head = HEADLINE[order.status] || { icon: "\u2022", title: order.status, cls: "muted" };
    const success = order.status === "SUCCESS";

    const rows = [
        ["Paid for", `${purposeLabel(order.purpose)} \u2013 ${order.passType || ""} ${order.transportType || ""} pass`],
        ["Route", order.route],
        ["Order ID", order.orderId],
        success && ["Transaction ID", order.transactionId],
        order.paymentMethod && ["Payment method", `${order.paymentMethod}${order.methodDetail ? " \u00B7 " + order.methodDetail : ""}`],
        success && ["Date & time", formatDateTime(order.paidAt)],
        success && order.purpose === "renewal" && ["Pass valid until", formatDate(order.cardExpiryDate)],
    ].filter(Boolean);

    return (
        <div className="receipt card p-4">
            <div className={`receipt-head ${head.cls}`}>
                <div className="receipt-icon">{head.icon}</div>
                <h3 className="mb-1">{head.title}</h3>
                <div className="receipt-amount">{formatINR(order.amount)}</div>
                {!success && order.failureReason && (
                    <p className="mb-0 mt-2 receipt-reason">{order.failureReason}</p>
                )}
            </div>

            <dl className="receipt-rows">
                {rows.map(([k, v]) => (
                    <div className="receipt-row" key={k}>
                        <dt>{k}</dt>
                        <dd>{v}</dd>
                    </div>
                ))}
            </dl>

            <p className="receipt-foot">
                SmartPass sandbox payment &middot; no real money was charged.
            </p>
        </div>
    );
}
