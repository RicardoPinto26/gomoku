import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import Page from "../common/Page";

import {Uris} from "../../utils/navigation/Uris";
import {MatchmakingButtons} from "./MatchmakingButtons";
import MenuButton from "./MenuButtons";
import Box from "@mui/material/Box";
import {Typography} from "@mui/material";
import MATCHMAKE = Uris.MATCHMAKE;
import CREATE_GAME = Uris.CREATE_GAME;
import JOIN_LOBBY = Uris.JOIN_LOBBY;
import MATCHMAKE_CONFIG = Uris.MATCHMAKE_CONFIG;

export function PlayMenu() {
    const navigate = useNavigate();
    return (
        <Page title={"Gameplay Menu"}>
            <Box sx={{mt: 1}}>
                <Typography variant="h6" gutterBottom>
                    {"Choose a game mode"}
                </Typography>
                <MatchmakingButtons title={"Quick Play"} onClickMatchmaking={() => navigate(MATCHMAKE)} onClickSettings={() => navigate(MATCHMAKE_CONFIG)}/>
                <MenuButton title={"New Game"} onClick={() => navigate(CREATE_GAME)}/>
                <MenuButton title={"Join Game"} onClick={() => navigate(JOIN_LOBBY)}/>
            </Box>
        </Page>
    );
}





