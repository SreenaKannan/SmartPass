import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import logo from "../assets/smartpass-logo.png";

function Login() {

    const navigate = useNavigate();

    const [login, setLogin] = useState({
        email: "",
        password: ""
    });

    const handleChange = (e) => {
        setLogin({
            ...login,
            [e.target.name]: e.target.value
        });
    };

    const loginUser = async () => {

    try {

        const response = await api.post(
            "/api/users/login",
            login
        );

        console.log(response);
console.log(response.data);
console.log(typeof response.data);

alert(JSON.stringify(response.data));

        if (response.data  === "Login Successful") {
            navigate("/dashboard");
        }

    } catch (error) {

        console.log(error);

        if (error.response) {
            alert(error.response.data);
        } else {
            alert("Login Failed");
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
                            User Login
                        </h2>

                        <input
                            type="email"
                            name="email"
                            placeholder="Email"
                            className="form-control mb-3"
                            onChange={handleChange}
                        />

                        <input
                            type="password"
                            name="password"
                            placeholder="Password"
                            className="form-control mb-3"
                            onChange={handleChange}
                        />

                        <button
                            className="btn btn-primary w-100"
                            onClick={loginUser}
                        >
                            Login
                        </button>

                        <p className="text-center mt-3">
                            Don't have an account?{" "}
                            <Link to="/">Register</Link>
                        </p>

                    </div>

                </div>

            </div>

        </div>

    );
}

export default Login;