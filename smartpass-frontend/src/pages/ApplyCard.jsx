import { useState } from "react";
import api from "../services/api";
import metro from "../assets/metro.png";

function ApplyCard() {
    
    const [card, setCard] = useState({
        userName: "",
        transportType: "",
        sourceStation: "",
        destinationStation: "",
        passType: ""
    });
    

    const handleChange = (e) => {
        setCard({
            ...card,
            [e.target.name]: e.target.value
        });
    };

    const applyCard = async () => {

        try {

            const response = await api.post(
                "/api/cards/create",
                card
            );

            alert(response.data);

        } catch (error) {

            console.log(error);

            if (error.response) {
                alert(error.response.data);
            } else {
                alert("Application Failed");
            }

        }

    };

    return (

        <div className="container mt-5">

            <div className="row justify-content-center">

                <div className="col-md-7">

                    <div className="card shadow p-4">
                        <div className="text-center mb-3">
    <img
        src={metro}
        width="300"
        alt="Metro"
    />
</div>
                        <h2 className="text-center mb-4">
                            Apply New Card
                        </h2>

                        <input
                            className="form-control mb-3"
                            placeholder="User Name"
                            name="userName"
                            onChange={handleChange}
                        />

                        <select
                            className="form-control mb-3"
                            name="transportType"
                            value={card.transportType}
                            onChange={handleChange}
                        >
                            <option value="">Select Transport</option>
                            <option value="Bus">🚌 Bus</option>
                            <option value="Metro">🚇 Metro</option>
                        </select>

                        <input
                            className="form-control mb-3"
                            placeholder="Source Station"
                            name="sourceStation"
                            onChange={handleChange}
                        />

                        <input
                            className="form-control mb-3"
                            placeholder="Destination Station"
                            name="destinationStation"
                            onChange={handleChange}
                        />

                        <select
                            className="form-control mb-3"
                            name="passType"
                            onChange={handleChange}
                        >
                            <option value="">Select Pass Type</option>
                            <option value="Monthly">Monthly</option>
                            <option value="Quarterly">Quarterly</option>
                            <option value="Half-Yearly">Half-Yearly</option>
                            <option value="Yearly">Yearly</option>
                        </select>


                        <button
                            className="btn btn-primary w-100"
                            onClick={applyCard}
                        >
                            Apply Card
                        </button>

                    </div>

                </div>

            </div>

        </div>

    );
}

export default ApplyCard;