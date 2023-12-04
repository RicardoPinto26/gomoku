import React from 'react';
import './App.css';
import {Route, Routes} from "react-router-dom";
import {Login} from "./components/authentication/Login";
import Register from "./components/authentication/Register";
import Home from "./components/Home";
import Profile from "./components/Profile";
import Footer from "./components/common/Footer";
import {NavBar} from "./components/common/NavBar";
import {Uris} from "./utils/navigation/Uris";
import HOME = Uris.HOME;
import LOGIN = Uris.LOGIN;
import REGISTER = Uris.REGISTER;
import PROFILE = Uris.PROFILE;
import About from './components/About';
import ABOUT = Uris.ABOUT;
import RANKING = Uris.RANKING;
import Ranking from "./components/Ranking";


export default function App() {
    return (
        <div className="App">
            <NavBar/>
            <div className="App-content">
                <Routes>
                    <Route path={HOME} element={<Home/>}/>

                    <Route path={LOGIN} element={<Login/>}/>
                    <Route path={REGISTER} element={<Register/>}/>
                    <Route path={PROFILE} element={<Profile/>}/>

                    <Route path={ABOUT} element={<About/>}/>

                    <Route path={RANKING} element={<Ranking/>}/>
                </Routes>
            </div>
            <Footer></Footer>
        </div>
    )
        ;
}
