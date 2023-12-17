import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import {handleRequest} from "../../../../services/utils/fetchSiren";
import {GameServices} from "../../../../services/game/GameServices";
import {handleError} from "../../../../services/utils/errorUtils";
import Button from "@mui/material/Button";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";
import {ForfeitDialog} from "./ForfeitButtonDialog";

export interface ForfeitGameProps {
    gameId: number
    lobbyId: number
    isYourTurn: boolean
}

export function ForfeitButton(props : ForfeitGameProps) {
    const navigate = useNavigate()
    const [error, setError] = useState<string | null>(null)
    const [isForfeitDialogOpen, setIsForfeiDialogOpen] = useState(false);

    async function forfeit() {
        const [error, res] = await handleRequest(GameServices.forfeit(props.lobbyId, props.gameId))
        if (error) {
            handleError(error, setError, navigate)
            return
        }
        console.log(res)
        navigate("/play")
    }

    const handleForfeitDialog = () => {
        setIsForfeiDialogOpen(true);
    };
    const handleCloseForfeitDialog = () => {
        setIsForfeiDialogOpen(false);
    };


    return (
        <div>
            <Button onClick={handleForfeitDialog} disabled={!props.isYourTurn}> Leave Game
                <ExitToAppIcon/>
            </Button>

            <ForfeitDialog onForfeitSelected={forfeit} open={isForfeitDialogOpen} onClose={handleCloseForfeitDialog}/>
        </div>
    )
}