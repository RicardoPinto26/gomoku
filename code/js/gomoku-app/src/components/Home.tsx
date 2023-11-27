import React from "react";

import { useNavigate } from "react-router-dom";

function Home() {
    const navigate = useNavigate();

    return (
        <div>
            <h1>Home</h1>

            <button onClick={() => navigate('/login')}>Login</button>
            <button onClick={() => navigate('/register')}>Register</button>
            <button onClick={() => navigate('/play')}>Play</button>
            <button onClick={() => navigate('/rankings')}>Rankings</button>
            <button onClick={() => navigate('/me')}>Profile</button>

        </div>
    );
}

export default Home;