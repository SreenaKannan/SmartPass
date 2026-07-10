import { useEffect, useState } from "react";
import api from "../services/api";

function MyCards() {

    const [cards, setCards] = useState([]);

    useEffect(() => {
        loadCards();
    }, []);

    const loadCards = async () => {

        try {

            const response = await api.get("/api/cards");

            setCards(response.data);

        } catch (error) {

            console.log(error);
            alert("Unable to load cards");

        }

    };

    const renewCard = async (id) => {

        try {

            const response = await api.put(`/api/cards/renew/${id}`);

            alert(response.data);

            loadCards();

        } catch (error) {

            console.log(error);

            if (error.response) {
                alert(error.response.data);
            } else {
                alert("Renew failed");
            }

        }

    };

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

        <div className="container mt-5">

            <h2 className="text-center mb-4">
                My Commuter Cards
            </h2>

            <table className="table table-bordered table-striped table-hover">

                <thead className="table-dark">

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

                            <td colSpan="10" className="text-center">
                                No Cards Available
                            </td>

                        </tr>

                    ) : (

                        cards.map((card) => (

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
                                <td>{card.status}</td>

                                <td>

                                    <button
                                        className="btn btn-success btn-sm me-2 mb-1"
                                        onClick={() => renewCard(card.cardId)}
                                    >
                                        Renew
                                    </button>

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

                        ))

                    )}

                </tbody>

            </table>

        </div>

    );

}

export default MyCards;