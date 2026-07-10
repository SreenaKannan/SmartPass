import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";
import bus from "../assets/bus.png";
import Navbar from "../components/Navbar";

function Dashboard() {

    const [stats, setStats] = useState({
        totalUsers: 0,
        totalCards: 0,
        activeCards: 0,
        renewedCards: 0,
        blockedCards: 0,
        totalPayments: 0
    });

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {

        try {

            const response = await api.get("/api/dashboard");

            setStats(response.data);

        } catch (error) {

            console.log(error);

        }

    };

    return (

        <>
            <Navbar />

            <div className="container mt-4">

                <div className="text-center mb-4">
                    <img
                        src={bus}
                        alt="Bus"
                        width="300"
                    />
                </div>

                <div className="card shadow mb-4">
                    <div className="card-body text-center">
                        <h2>Welcome to SmartPass 🚍</h2>
                        <p>
                            Manage Bus and Metro smart cards, payments,
                            renewals and travel history from one place.
                        </p>
                    </div>
                </div>

                <div className="row g-3">

                    <div className="col-md-4">
                        <div className="card text-center shadow">
                            <div className="card-body">
                                <h5>Total Users</h5>
                                <h2>{stats.totalUsers}</h2>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card text-center shadow">
                            <div className="card-body">
                                <h5>Total Cards</h5>
                                <h2>{stats.totalCards}</h2>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card text-center shadow">
                            <div className="card-body">
                                <h5>Total Payments</h5>
                                <h2>{stats.totalPayments}</h2>
                            </div>
                        </div>
                    </div>

                </div>

                <div className="row g-4 mt-4">

                    <div className="col-md-4">
                        <Link to="/apply" className="btn btn-primary w-100 p-4">
                            🚌<br />
                            Apply Card
                        </Link>
                    </div>

                    <div className="col-md-4">
                        <Link to="/cards" className="btn btn-success w-100 p-4">
                            💳<br />
                            My Cards
                        </Link>
                    </div>

                    <div className="col-md-4">
                        <Link to="/payment" className="btn btn-warning w-100 p-4">
                            💰<br />
                            Payment
                        </Link>
                    </div>

                    <div className="col-md-4">
                        <Link to="/search" className="btn btn-info w-100 p-4">
                            🔍<br />
                            Search
                        </Link>
                    </div>

                    <div className="col-md-4">
                        <Link to="/renewals" className="btn btn-secondary w-100 p-4">
                            📜<br />
                            Renewal History
                        </Link>
                    </div>

                    <div className="col-md-4">
                        <Link to="/admin" className="btn btn-dark w-100 p-4">
                            👨‍💼<br />
                            Admin
                        </Link>
                    </div>

                </div>

            </div>

        </>

    );

}

export default Dashboard;