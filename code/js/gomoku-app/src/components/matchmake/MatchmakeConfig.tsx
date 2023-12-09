import React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import {Box, Typography} from "@mui/material";
import {useMatchmakingSettings} from "./MatchmaingSettings";
import {useNavigate} from "react-router-dom";

function MatchmakingConfig() {
    const navigate = useNavigate();
    const { settings, setSettings } = useMatchmakingSettings();
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        navigate("/play")
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
    return (
        <Box sx={{mt: 1}}>
            <Typography variant="h6" gutterBottom>
                {"Choose the game configuration"}
            </Typography>
            <form onSubmit={handleSubmit}>
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
                                onChange={(e) => setSettings({ ...settings, overflow: e.target.checked })}
                            />
                        }
                        label="Overflow"
                    />
                </Box>

                <Button type="submit" variant="contained">Save</Button>
            </form>
        </Box>
    )
}


export default MatchmakingConfig;
