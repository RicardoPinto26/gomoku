import {Dialog, DialogContent, DialogTitle, Grid} from "@mui/material";
import Button from "@mui/material/Button";
import React from "react";

export interface WinnerDialogProps{
    open: boolean
    onClose: () => void
    onReturnToMenu: () => void
    winner: Boolean
}

export function WinnerDialog ({open, onClose, onReturnToMenu, winner}: WinnerDialogProps) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{winner ? "You won the Game" : "You lost the game"}</DialogTitle>
            <DialogContent>
                <Button onClick={() => onReturnToMenu()}>Return to Menu</Button>
            </DialogContent>
        </Dialog>
    );
}

