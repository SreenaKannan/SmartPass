import { BrowserRouter, Routes, Route } from "react-router-dom";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ApplyCard from "./pages/ApplyCard";
import MyCards from "./pages/MyCards";
import Payment from "./pages/Payment";
import Search from "./pages/Search";
import Admin from "./pages/Admin";
import RenewalHistory from "./pages/RenewalHistory";
function App() {

    return (

        <BrowserRouter>

            <Routes>

                <Route path="/" element={<Register />} />

                <Route path="/login" element={<Login />} />

                <Route path="/dashboard" element={<Dashboard />} />

                <Route path="/apply" element={<ApplyCard />} />

                <Route path="/cards" element={<MyCards />} />

                <Route path="/payment" element={<Payment />} />

                <Route path="/search" element={<Search />} />

                <Route path="/admin" element={<Admin />} />

                <Route
    path="/renewals"
    element={<RenewalHistory />}
/>

            </Routes>

        </BrowserRouter>

    );

}

export default App;