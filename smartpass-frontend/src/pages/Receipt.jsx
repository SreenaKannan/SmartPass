import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";
import PaymentReceipt from "../components/PaymentReceipt";
import "../styles/payment.css";

export default function Receipt() {
    const { orderId } = useParams();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        api.get(`/api/payments/${orderId}`)
            .then((r) => setOrder(r.data))
            .catch(() => setError("We couldn't find that receipt."));
    }, [orderId]);

    return (
        <>
            <Navbar />
            <main className="page-shell pay-shell">
                {error && <div className="alert alert-danger">{error}</div>}
                {order && <PaymentReceipt order={order} />}
                <div className="receipt mt-3 d-flex gap-2 no-print">
                    <Link className="btn btn-outline-secondary flex-fill" to="/payments">All payments</Link>
                    <button className="btn btn-outline-primary flex-fill" onClick={() => navigate("/dashboard")}>Dashboard</button>
                    {order?.status === "SUCCESS" && <button className="btn btn-primary flex-fill" onClick={() => window.print()}>Print receipt</button>}
                </div>
            </main>
        </>
    );
}
