import {Dialog, DialogContent, DialogTitle} from "@mui/material";
import Button from "@mui/material/Button";
import React from "react";

export interface NextColorDialogProps{
    open: boolean
    onClose: () => void
    onForfeitSelected: (res : boolean) => void
}

export function ForfeitDialog ({open, onClose, onForfeitSelected}: NextColorDialogProps) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Are you sure you want to forfeit the game?</DialogTitle>
            <DialogContent>
                <Button onClick={() => onForfeitSelected(true)}>Yes, forfeit the game</Button>
                <Button onClick={() => onClose()}>No! Keep playing</Button>
            </DialogContent>
        </Dialog>
    );
}