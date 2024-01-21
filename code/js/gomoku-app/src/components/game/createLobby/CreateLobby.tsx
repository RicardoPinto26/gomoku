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
import {useCreateLobbyConfig} from "../matchmake/GameSettings";
import {useNavigate} from "react-router-dom";
import LoadingSpinner from "../../common/LoadingSpinner";
import {useCurrentUser} from "../../../utils/Authn";
import {CreateLobbyInputModel} from "../../../services/lobby/models/CreateLobbyInputModel";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {handleError} from "../../../services/utils/errorUtils";
import {LobbyServices} from "../../../services/lobby/LobbyServices";

export function CreateLobby() {
    const user = useCurrentUser()
    const nav = useNavigate();
    const [error, setError] = React.useState<string | null>(null);
    const {settings, setSettings} = useCreateLobbyConfig()
    const [lobbyName, setLobbyName] = React.useState(user + " Lobby")
    const navigate = useNavigate();
    const [lobbyId, setLobbyId] = React.useState(-1);
    const [isWaitingForOpponent, setIsWaitingForOpponent] = React.useState(false);


    useEffect(() => {
        const interval = setInterval(() => {
            checkIfOpponentJoined()
        }, 2000);
        return () => clearInterval(interval);
    }, [lobbyId, isWaitingForOpponent]);


    async function createLobby() {

        const [error, res] = await handleRequest(LobbyServices.createLobby(new CreateLobbyInputModel(lobbyName, settings)))

        if (error) {
            handleError(error, setError, nav)
            return
        }

        if (res == undefined) {
            throw new Error("Response is undefined")
        }

        setIsWaitingForOpponent(true)
        setLobbyId(res.properties?.id as number)
    }

    async function checkIfOpponentJoined() {
        if (!isWaitingForOpponent || lobbyId == -1) return;

        const [error, res] = await handleRequest(LobbyServices.getLobby(lobbyId))
        if (error) {
            handleError(error, setError, nav)
            return
        }

        if (res === undefined || res === null) {
            throw new Error("Response is undefined")
        }
        console.log(`create -${(res.properties?.user2?.username)}  ${(res.entities)}`)

        if (res.properties?.user2 != null) {
            setIsWaitingForOpponent(false);
            // @ts-ignore TODO() - siren link
            const gameId = res.entities[0].links[0].href.replace("/api", "");
            navigate(`${gameId}`);
        }
    }


    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        createLobby()
    }

    const handleChange = (name: keyof typeof settings) => (
        event: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<{
            value: unknown
        }> | SelectChangeEvent<string>
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

