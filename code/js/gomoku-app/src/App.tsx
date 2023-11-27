import React from 'react';
import './App.css';
import {createBrowserRouter, Route, RouterProvider, Routes} from "react-router-dom";
import {Login, Register} from "./components/Login";
import Home from "./components/Home";
import Profile from "./components/Profile";


function App() {
    return (
        <div className="App">
            <div className="App-content">
                <RouterProvider router={router} />
            </div>
            <footer>
                <p> Francisco Medeiros </p>
                <p> Ricardo Pinto</p>
                <p> Luis Macario </p>
            </footer>
        </div>
    )
        ;
}

export default App;


const router = createBrowserRouter([
    {
        "path": "/",
        "children": [
            {
                "path": "/",
                "element": <Home/>,
                "children": [
                    {
                        "path": "/rankings",
                        //"element": < />,
                    }
                ]

            },
            {
                "path": "/login",
                "element": <Login/>
            },
            {
                "path": "/register",
                "element": <Register/>
            },
            {
                "path": "/profile",
                "element": <Profile/>
            }
        ]
    }
])
