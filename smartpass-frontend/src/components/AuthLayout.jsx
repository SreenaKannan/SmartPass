import "./../styles/auth.css";

function AuthLayout({ title, children, image }) {

    return (

        <div className="auth-container">

            <div className="auth-left">

                <img
                    src={image}
                    alt="SmartPass"
                    className="auth-image"
                />

            </div>

            <div className="auth-right">

                <div className="auth-card">

                    <h2>{title}</h2>

                    {children}

                </div>

            </div>

        </div>

    );

}

export default AuthLayout;