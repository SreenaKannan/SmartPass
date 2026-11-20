import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";

function MyCards() {

    const navigate = useNavigate();

    const [cards, setCards] = useState([]);
    const [payments, setPayments] = useState([]);

    const fetchAll = () => Promise.all([
        api.get("/api/cards"),
        api.get("/api/payments"),
    ]);

    const applyData = ([cardRes, payRes]) => {
        setCards(cardRes.data);
        setPayments(payRes.data);
    };

    const onLoadError = (error) => {
        console.log(error);
        alert("Unable to load cards");
    };

    const loadCards = () => fetchAll().then(applyData).catch(onLoadError);

    useEffect(() => {
        fetchAll().then(applyData).catch(onLoadError);
    }, []);

    // An application is paid once a successful application-fee payment exists
    // (older payments without a purpose count as the application fee).
    const isApplicationPaid = (cardId) =>
        payments.some((p) =>
            p.cardId === cardId &&
            p.status === "SUCCESS" &&
            (!p.purpose || p.purpose === "application"));

    const goToPayment = (card, purpose) =>
        navigate("/payment", { state: { cardId: card.cardId, purpose } });

    const blockCard = async (id) => {

        try {

            const response = await api.put(`/api/cards/block/${id}`);

            alert(response.data);

            loadCards();

        } catch (error) {

            console.log(error);

            if (error.response) {
                alert(error.response.data);
            } else {
                alert("Block failed");
            }

        }

    };

    const deleteCard = async (id) => {

        if (!window.confirm("Are you sure you want to delete this card?")) {
            return;
        }

        try {

            const response = await api.delete(`/api/cards/${id}`);

            alert(response.data);

            loadCards();

        } catch (error) {

            console.log(error);

            if (error.response) {
                alert(error.response.data);
            } else {
                alert("Delete failed");
            }

        }

    };

    return (

        <>
            <Navbar />

            <main className="page-shell">

                <div className="mb-4">
                    <div className="section-eyebrow">My cards</div>
                    <h1 className="h2 fw-bold mb-0">My commuter cards</h1>
                </div>

                <div className="table-responsive">

                    <table className="table table-hover align-middle">

                        <thead>

                            <tr>

                                <th>ID</th>
                                <th>User Name</th>
                                <th>Transport</th>
                                <th>Source</th>
                                <th>Destination</th>
                                <th>Pass Type</th>
                                <th>Fare</th>
                                <th>Issue Date</th>
                                <th>Expiry Date</th>
                                <th>Status</th>
                                <th>Actions</th>

                            </tr>

                        </thead>

                        <tbody>

                            {cards.length === 0 ? (

                                <tr>

                                    <td colSpan="11" className="text-center">
                                        No Cards Available
                                    </td>

                                </tr>

                            ) : (

                                cards.map((card) => {

                                    const needsPayment = card.status === "Pending" && !isApplicationPaid(card.cardId);

                                    return (

                                        <tr key={card.cardId}>

                                            <td>{card.cardId}</td>
                                            <td>{card.userName}</td>
                                            <td>{card.transportType}</td>
                                            <td>{card.sourceStation}</td>
                                            <td>{card.destinationStation}</td>
                                            <td>{card.passType}</td>
                                            <td>₹{card.fareAmount}</td>
                                            <td>{card.issueDate}</td>
                                            <td>{card.expiryDate}</td>
                                            <td>
                                                {card.status}
                                                {needsPayment && (
                                                    <div><span className="badge bg-warning text-dark mt-1">Payment due</span></div>
                                                )}
                                                {card.status === "Pending" && !needsPayment && (
                                                    <div><span className="badge bg-info text-dark mt-1">Fee paid &middot; awaiting review</span></div>
                                                )}
                                            </td>

                                            <td className="text-nowrap">

                                                {needsPayment && (
                                                    <button
                                                        className="btn btn-primary btn-sm me-2 mb-1"
                                                        onClick={() => goToPayment(card, "application")}
                                                    >
                                                        Pay now
                                                    </button>
                                                )}

                                                {card.status === "Approved" && (
                                                    <button
                                                        className="btn btn-success btn-sm me-2 mb-1"
                                                        onClick={() => goToPayment(card, "renewal")}
                                                    >
                                                        Renew
                                                    </button>
                                                )}

                                                <button
                                                    className="btn btn-warning btn-sm me-2 mb-1"
                                                    onClick={() => blockCard(card.cardId)}
                                                >
                                                    Block
                                                </button>

                                                <button
                                                    className="btn btn-danger btn-sm mb-1"
                                                    onClick={() => deleteCard(card.cardId)}
                                                >
                                                    Delete
                                                </button>

                                            </td>

                                        </tr>

                                    );
                                })

                            )}

                        </tbody>

                    </table>

                </div>

            </main>
        </>

    );

}

export default MyCards;
