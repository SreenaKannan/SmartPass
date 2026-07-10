import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/smartpass-logo.png";

function Navbar() {

    const navigate = useNavigate();

    const logout = () => {

        localStorage.clear();

        navigate("/login");

    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark bg-primary shadow">

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

                <div>

                    <Link
                        className="btn btn-light me-2"
                        to="/dashboard"
                    >
                        Dashboard
                    </Link>

                    <Link
                        className="btn btn-light me-2"
                        to="/cards"
                    >
                        My Cards
                    </Link>

                    <Link
                        className="btn btn-light me-2"
                        to="/payment"
                    >
                        Payment
                    </Link>

                    <button
                        className="btn btn-danger"
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