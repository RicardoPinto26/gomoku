import React from "react";
import "./App.css";
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
import GAMETEST = Uris.GAMETEST
import About from "./components/About";
import ABOUT = Uris.ABOUT;
import RANKING = Uris.RANKING;
import Ranking from "./components/Ranking";
import {PlayMenu} from "./components/game/playMenu/PlayMenu";
import MATCHMAKE = Uris.MATCHMAKE;
import PLAY_MENU = Uris.PLAY_MENU;
import GAMEPLAY = Uris.GAMEPLAY;
import CREATE_GAME = Uris.CREATE_GAME;
import JOIN_LOBBY = Uris.JOIN_LOBBY;
import {Matchmake} from "./components/game/matchmake/Matchmake";
import MATCHMAKE_CONFIG = Uris.MATCHMAKE_CONFIG;
import MatchmakingConfig from "./components/game/matchmake/MatchmakeConfig";
import {CreateGameConfigProvider, MatchmakeConfigProvider} from "./components/game/matchmake/GameSettings";
import {JoinLobby} from "./components/game/joinLobby/JoinLobby";
import {CreateLobby} from "./components/game/createLobby/CreateLobby";
import Gameplay from "./components/game/gameplay/Gameplay";


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

                    <Route path={PLAY_MENU} element={<PlayMenu/>}/>
                    <Route path={CREATE_GAME}
                           element={
                               <CreateGameConfigProvider>
                                   <CreateLobby/>
                               </CreateGameConfigProvider>}
                    />

                    <Route path={JOIN_LOBBY} element={<JoinLobby/>}/>
                    <Route path={MATCHMAKE}
                           element={
                               <MatchmakeConfigProvider>
                                   <Matchmake/>
                               </MatchmakeConfigProvider>}
                    />
                    <Route path={MATCHMAKE_CONFIG}
                           element={
                               <MatchmakeConfigProvider>
                                   <MatchmakingConfig/>
                               </MatchmakeConfigProvider>}
                    />
                    <Route path={GAMETEST} element={<Gameplay/>}/>
                    <Route path={GAMEPLAY} element={<Gameplay/>}/>


                    <Route path={"*"} element={<div>404</div>}/>
                </Routes>
            </div>
        </div>
    );
    //<Footer></Footer>
}
