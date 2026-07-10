import { useState } from "react";
import api from "../services/api";

function RenewalHistory() {

    const [cardId, setCardId] = useState("");

    const [history, setHistory] = useState([]);

    const loadHistory = async () => {

        try {

            const response = await api.get(
                `/api/renewals/${cardId}`
            );

            setHistory(response.data);

        } catch (error) {

            console.log(error);

            alert("No Renewal History Found");

        }

    };

    return (

        <div className="container mt-5">

            <div className="card shadow p-4">

                <h2 className="text-center mb-4">
                    Renewal History
                </h2>

                <input
                    type="number"
                    className="form-control mb-3"
                    placeholder="Enter Card ID"
                    value={cardId}
                    onChange={(e) =>
                        setCardId(e.target.value)
                    }
                />

                <button
                    className="btn btn-primary mb-4"
                    onClick={loadHistory}
                >
                    Search
                </button>

                <table className="table table-bordered">

                    <thead className="table-dark">

                        <tr>

                            <th>ID</th>
                            <th>Card ID</th>
                            <th>User</th>
                            <th>Renewal Date</th>
                            <th>Old Expiry</th>
                            <th>New Expiry</th>
                            <th>Amount</th>

                        </tr>

                    </thead>

                    <tbody>

                        {history.map((item) => (

                            <tr key={item.renewalId}>

                                <td>{item.renewalId}</td>
                                <td>{item.cardId}</td>
                                <td>{item.userName}</td>
                                <td>{item.renewalDate}</td>
                                <td>{item.oldExpiryDate}</td>
                                <td>{item.newExpiryDate}</td>
                                <td>₹{item.amountPaid}</td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </div>

    );

}

export default RenewalHistory;