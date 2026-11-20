import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ApplyCard from "./pages/ApplyCard";
import MyCards from "./pages/MyCards";
import Payment from "./pages/Payment";
import Search from "./pages/Search";
import Admin from "./pages/Admin";
import RenewalHistory from "./pages/RenewalHistory";
import AIHub from "./pages/AIHub";
import PaymentHistory from "./pages/PaymentHistory";
import Receipt from "./pages/Receipt";
function Guard({ role, children }) {
    const user = JSON.parse(localStorage.getItem("smartpassUser") || "null");
    return user && (!role || user.role === role) ? children : <Navigate to="/login" replace />;
}
function App() {
    return (

        <BrowserRouter>

            <Routes>

                <Route path="/" element={<Register />} />

                <Route path="/login" element={<Login />} />

                <Route path="/dashboard" element={<Guard role="USER"><Dashboard /></Guard>} />

                <Route path="/apply" element={<Guard role="USER"><ApplyCard /></Guard>} />

                <Route path="/cards" element={<Guard role="USER"><MyCards /></Guard>} />

                <Route path="/payment" element={<Guard role="USER"><Payment /></Guard>} />

                <Route path="/payments" element={<Guard role="USER"><PaymentHistory /></Guard>} />

                <Route path="/receipt/:orderId" element={<Guard role="USER"><Receipt /></Guard>} />

                <Route path="/search" element={<Guard role="USER"><Search /></Guard>} />

                <Route path="/admin" element={<Guard role="ADMIN"><Admin /></Guard>} />

                <Route
    path="/renewals"
    element={<Guard role="USER"><RenewalHistory /></Guard>}
/>

                <Route
    path="/ai-hub"
    element={<Guard role="USER"><AIHub /></Guard>}
/>

                <Route path="*" element={<Navigate to="/dashboard" replace />} />

            </Routes>

        </BrowserRouter>

    );

}

export default App;
