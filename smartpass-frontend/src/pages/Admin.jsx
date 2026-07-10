import { useEffect, useState } from "react";
import api from "../services/api";

function Admin() {

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

    const approveCard = async (id) => {

        try {

            const response = await api.put(
                `/api/cards/approve/${id}`
            );

            alert(response.data);

            loadCards();

        } catch (error) {

            console.log(error);

        }

    };

    const rejectCard = async (id) => {

        try {

            const response = await api.put(
                `/api/cards/reject/${id}`
            );

            alert(response.data);

            loadCards();

        } catch (error) {

            console.log(error);

        }

    };

    return (

        <div className="container mt-5">

            <h2 className="text-center mb-4">
                Admin Panel
            </h2>

            <table className="table table-bordered table-striped">

                <thead className="table-dark">

                    <tr>

                        <th>ID</th>
                        <th>User</th>
                        <th>Transport</th>
                        <th>Source</th>
                        <th>Destination</th>
                        <th>Pass Type</th>
                        <th>Fare</th>
                        <th>Expiry</th>
                        <th>Status</th>
                        <th>Actions</th>

                    </tr>

                </thead>

                <tbody>

                    {cards.map((card) => (

                        <tr key={card.cardId}>

                            <td>{card.cardId}</td>
                            <td>{card.userName}</td>
                            <td>{card.transportType}</td>
                            <td>{card.sourceStation}</td>
                            <td>{card.destinationStation}</td>
                            <td>{card.passType}</td>
                            <td>₹{card.fareAmount}</td>
                            <td>{card.expiryDate}</td>
                            <td>{card.status}</td>

                            <td>

                                <button
                                    className="btn btn-success btn-sm me-2"
                                    onClick={() => approveCard(card.cardId)}
                                >
                                    Approve
                                </button>

                                <button
                                    className="btn btn-danger btn-sm"
                                    onClick={() => rejectCard(card.cardId)}
                                >
                                    Reject
                                </button>

                            </td>

                        </tr>

                    ))}

                </tbody>

            </table>

        </div>

    );
}

export default Admin;