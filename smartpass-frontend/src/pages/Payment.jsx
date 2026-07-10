import { useState } from "react";
import api from "../services/api";

function Payment() {

    const [payment, setPayment] = useState({
        cardId: "",
        amount: "",
        paymentMethod: "",
        paymentDate: ""
    });

    const handleChange = (e) => {
        setPayment({
            ...payment,
            [e.target.name]: e.target.value
        });
    };

    const makePayment = async () => {

        try {

            const response = await api.post(
                "/api/payments/pay",
                payment
            );

            alert(response.data);

            setPayment({
                cardId: "",
                amount: "",
                paymentMethod: "",
                paymentDate: ""
            });

        } catch (error) {

            console.log(error);

            alert("Payment Failed");

        }

    };

    return (

        <div className="container mt-5">

            <div className="card p-4">

                <h2 className="text-center mb-4">
                    Make Payment
                </h2>

                <input
                    className="form-control mb-3"
                    placeholder="Card ID"
                    name="cardId"
                    value={payment.cardId}
                    onChange={handleChange}
                />

                <input
                    className="form-control mb-3"
                    placeholder="Amount"
                    name="amount"
                    value={payment.amount}
                    onChange={handleChange}
                />

                <select
                    className="form-control mb-3"
                    name="paymentMethod"
                    value={payment.paymentMethod}
                    onChange={handleChange}
                >
                    <option value="">Select Payment Method</option>
                    <option>UPI</option>
                    <option>Credit Card</option>
                    <option>Debit Card</option>
                    <option>Net Banking</option>
                </select>

                <input
                    type="date"
                    className="form-control mb-3"
                    name="paymentDate"
                    value={payment.paymentDate}
                    onChange={handleChange}
                />

                <button
                    className="btn btn-success"
                    onClick={makePayment}
                >
                    Pay Now
                </button>

            </div>

        </div>

    );
}

export default Payment;