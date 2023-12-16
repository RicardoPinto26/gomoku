import {Dialog, DialogContent, DialogTitle, Grid} from "@mui/material";
import Button from "@mui/material/Button";
import React from "react";

export interface NextColorDialogProps{
    open: boolean
    onClose: () => void
    onColorSelected: (color : string) => void
}

export function ChooseColor ({open, onClose, onColorSelected}: NextColorDialogProps) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Choose your pieces color for the rest of the Game</DialogTitle>
            <DialogContent>
                <Button onClick={() => onColorSelected("BLACK")}>BLACK</Button>
                <Button onClick={() => onColorSelected("WHITE")}>WHITE</Button>
            </DialogContent>
        </Dialog>
    );
}

