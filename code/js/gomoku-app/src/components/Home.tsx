import React from "react";
import Typography from "@mui/material/Typography";
import GomokuLogo from '../resources/GomokuLogo.png';
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";
import Page from "./common/Page";
import {useCurrentUser} from "../utils/Authn";
import {getAndStoreHome} from "../services/home/HomeServices";

/**
 * Home page component
 */
function Home() {

    const user = useCurrentUser()
    const navigate = useNavigate()

    getAndStoreHome()

    return (
        <>
            <Page title={"Gomoku Royale"}>
                <Typography variant="h6" gutterBottom>
                    Welcome {user ? ", " + user! : ""}!
                </Typography>

                <img src={GomokuLogo} alt="logo" width="200" height="200"/>

                <Box sx={{mt: 1}}>
                    <Typography variant="h6" gutterBottom>
                        {user
                            ? "Start playing now!"
                            : "Login or sign up to start playing!"}
                    </Typography>

                    <Button
                        fullWidth
                        size="large"
                        variant="contained"
                        sx={{mt: 3, mb: 2}}
                        color="primary"
                        onClick={() => navigate(user ? '/play' : '/login')}
                    >
                        {user ? "Play!" : "Login!"}
                    </Button>
                </Box>
            </Page>
        </>
    );
}

export default Home;