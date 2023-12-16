import React, {useEffect} from 'react'
import {Box, Typography} from "@mui/material";
import TextField from "@mui/material/TextField";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Button from "@mui/material/Button";
import { useCreateLobbyConfig} from "../matchmake/GameSettings";
import {useNavigate} from "react-router-dom";
import { createLobbyServices, getLobbyState} from "../../../services/lobby/LobbyServices";
import LoadingSpinner from "../../common/LoadingSpinner";
import {useCurrentUser} from "../../../utils/Authn";
import {CreateLobbyInputModel} from "../../../services/lobby/models/CreateLobbyInputModel";


export function CreateLobby() {
    const user=useCurrentUser()
    const {settings, setSettings} = useCreateLobbyConfig()
    const [lobbyName, setLobbyName] = React.useState(user + " Lobby")
    const navigate = useNavigate();
    const [isLobbyCreated, setIsLobbyCreated] = React.useState(false);
    const [lobbyId, setLobbyId] = React.useState(-1);
    const [isWaitingForOpponent, setIsWaitingForOpponent] = React.useState(false);


    useEffect(() => {
        const interval = setInterval(() => {
            checkIfOpponentJoined().then(r => console.log(r));
        }, 2000);
        return () => clearInterval(interval);
    }, [lobbyId, isWaitingForOpponent]);


    async function createLobby() {
        const res =
            await createLobbyServices(new CreateLobbyInputModel(lobbyName, settings));
        if (res.status == 201) {
            console.log("Lobby created");
            console.log(res.response)
            setIsLobbyCreated(true);
            setIsWaitingForOpponent(true)
            setLobbyId(res.response.properties.id)
        } else {
            //hammer debug
            console.log("Error creating lobby");
        }
    }

    async function checkIfOpponentJoined() {
        if (!isWaitingForOpponent || lobbyId == -1) return;

        try {
            const lobby = await getLobbyState(lobbyId);
            if (lobby.properties.user2 != null) {
                setIsWaitingForOpponent(false);
                const gameId = lobby.entities[0].links[0].href.replace("/api", "");
                console.log("Opponent joined, navigating to game");
                console.log(gameId);
                navigate(`${gameId}`);
            }
        } catch (error) {
            console.log("Error in checkIfOpponentJoined:", error);
        }
    }

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        createLobby()
    }

    const handleChange = (name: keyof typeof settings) => (
        event: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<{ value: unknown }> | SelectChangeEvent<string>
    ) => {
        setSettings({
            ...settings,
            [name]: event.target.value,
        })
    }

    const fixedWidth = {width: "250px"};

    if (isWaitingForOpponent) {
        return (
            <Box sx={{mt: 1}}>
                <LoadingSpinner text={"Waiting for opponent to join..."}/>
            </Box>
        )
    } else {

        return (
            <Box sx={{mt: 1}}>
                <Typography variant="h6" gutterBottom>
                    {"Choose the game configuration"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <Box sx={{mb: 2}}>
                        <TextField
                            label="Name"
                            defaultValue={"Lobby Unnamed"}
                            type="string"
                            value={lobbyName}
                            onChange={handleChange('name')}
                            sx={fixedWidth}
                        />
                    </Box>

                    <Box sx={{mb: 2}}>
                        <TextField
                            label="Grid Size"
                            type="number"
                            value={settings.gridSize}
                            onChange={handleChange('gridSize')}
                            sx={fixedWidth}
                        />
                    </Box>

                    <Box sx={{mb: 2}}>
                        <TextField
                            label="Winning Length"
                            type="number"
                            value={settings.winningLength}
                            onChange={handleChange('winningLength')}
                            sx={fixedWidth}
                        />
                    </Box>

                    <Box sx={{mb: 2}}>
                        <FormControl fullWidth sx={fixedWidth}>
                            <InputLabel>Opening</InputLabel>
                            <Select
                                value={settings.opening}
                                label="Opening"
                                onChange={handleChange('opening')}
                            >
                                <MenuItem value="FREESTYLE">Freestyle</MenuItem>
                                <MenuItem value="SWAP2">SWAP2</MenuItem>
                                <MenuItem value="SWAP">SWAP</MenuItem>
                                <MenuItem value="LONG_PRO">LONG PRO</MenuItem>
                                <MenuItem value="PRO">PRO</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>

                    <Box sx={{mb: 2}}>
                        <TextField
                            label="Points Margin"
                            type="number"
                            value={settings.pointsMargin}
                            onChange={handleChange('pointsMargin')}
                            sx={fixedWidth}
                        />
                    </Box>

                    <Box sx={{mb: 2}}>
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={settings.overflow}
                                    onChange={(e) => setSettings({...settings, overflow: e.target.checked})}
                                />
                            }
                            label="Overflow"
                        />
                    </Box>

                    <Button type="submit" variant="contained">Create Game</Button>
                </form>
            </Box>
        )
    }
}

