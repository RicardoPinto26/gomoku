import { SlWrench } from "react-icons/sl";
import React from "react";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";

interface MatchmakingButtonsProps {
    title: string;
    onClickMatchmaking: () => void;
    onClickSettings: () => void;
}

export function MatchmakingButtons({ title, onClickMatchmaking, onClickSettings }: MatchmakingButtonsProps) {
    return (
        <Box sx={{ mt: 1, display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
            <Button
                size="large"
                variant="contained"
                sx={{ mt: 3, mb: 2, mr: 1, flexGrow: 1 }}
                color="primary"
                onClick={onClickMatchmaking}
            >
                {title}
            </Button>
            <Button
                size="large"
                variant="contained"
                sx={{ mt: 3, mb: 2, ml: 1, flexGrow: 1 }}
                startIcon= {<SlWrench/>}
                color="primary"
                onClick={onClickSettings}
            >
            </Button>
        </Box>
    );
}