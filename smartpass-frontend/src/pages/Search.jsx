import { useState } from "react";
import api from "../services/api";

function Search() {

    const [userName, setUserName] = useState("");

    const [cards, setCards] = useState([]);

    const searchCard = async () => {

        try {

            const response = await api.get(
                `/api/cards/search/${userName}`
            );

            setCards(response.data);

        } catch (error) {

            console.log(error);

            alert("No card found");

        }

    };

    return (

        <div className="container mt-5">

            <div className="card p-4">

                <h2 className="text-center mb-4">
                    Search Card
                </h2>

                <input
                    type="text"
                    className="form-control mb-3"
                    placeholder="Enter User Name"
                    value={userName}
                    onChange={(e) =>
                        setUserName(e.target.value)
                    }
                />

                <button
                    className="btn btn-primary mb-4"
                    onClick={searchCard}
                >
                    Search
                </button>

                {cards.length > 0 && (

                    <table className="table table-bordered">

                        <thead>

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

                                </tr>

                            ))}

                        </tbody>

                    </table>

                )}

            </div>

        </div>

    );
}

export default Search;