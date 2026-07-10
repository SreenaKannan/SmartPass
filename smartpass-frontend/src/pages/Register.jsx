import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import logo from "../assets/smartpass-logo.png";

function Register() {

    const navigate = useNavigate();

    const [user, setUser] = useState({
        name: "",
        email: "",
        password: "",
        phone: ""
    });

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

    const register = async () => {

        try {

            const response = await api.post(
                "/api/users/register",
                user
            );

            alert(response.data);

            // Redirect to Login Page
            navigate("/login");

        } catch (error) {

            console.log(error);

            if (error.response) {
                alert(error.response.data);
            } else {
                alert("Registration Failed");
            }

        }

    };

    return (

        <div className="container mt-5">

            <div className="row justify-content-center">

                <div className="col-md-5">

                    <div className="card shadow p-4">

                        <div className="text-center mb-3">

                            <img
                                src={logo}
                                alt="SmartPass"
                                width="90"
                            />

                        </div>

                        <h2 className="text-center mb-4">
                            User Registration
                        </h2>

                        <input
                            className="form-control mb-3"
                            type="text"
                            name="name"
                            placeholder="Full Name"
                            onChange={handleChange}
                        />

                        <input
                            className="form-control mb-3"
                            type="email"
                            name="email"
                            placeholder="Email"
                            onChange={handleChange}
                        />

                        <input
                            className="form-control mb-3"
                            type="password"
                            name="password"
                            placeholder="Password"
                            onChange={handleChange}
                        />

                        <input
                            className="form-control mb-3"
                            type="text"
                            name="phone"
                            placeholder="Phone Number"
                            onChange={handleChange}
                        />

                        <button
                            className="btn btn-success w-100"
                            onClick={register}
                        >
                            Register
                        </button>

                        <p className="text-center mt-3">

                            Already have an account?{" "}

                            <Link to="/login">

                                Login

                            </Link>

                        </p>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default Register;