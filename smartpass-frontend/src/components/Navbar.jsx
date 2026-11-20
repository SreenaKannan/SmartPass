import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/smartpass-logo.png";

function Navbar() {

    const navigate = useNavigate();

    const logout = () => {

        localStorage.clear();

        navigate("/login");

    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark smart-navbar">

            <div className="container">

                <Link
                    className="navbar-brand d-flex align-items-center"
                    to="/dashboard"
                >

                    <img
                        src={logo}
                        alt="SmartPass"
                        width="45"
                        className="me-2"
                    />

                    <strong>SmartPass</strong>

                </Link>

                <div className="d-flex align-items-center gap-1 flex-wrap justify-content-end">

                    <Link
                        className="nav-link"
                        to="/dashboard"
                    >
                        Dashboard
                    </Link>

                    <Link
                        className="nav-link"
                        to="/cards"
                    >
                        My Cards
                    </Link>

                    <Link className="nav-link" to="/payments">Payments</Link>

                    <Link
                        className="nav-link"
                        to="/ai-hub"
                    >
                        🤖 AI Hub
                    </Link>

                    <button
                        className="btn btn-light btn-sm ms-2 px-3"
                        onClick={logout}
                    >
                        Logout
                    </button>

                </div>

            </div>

        </nav>

    );

}

export default Navbar;
