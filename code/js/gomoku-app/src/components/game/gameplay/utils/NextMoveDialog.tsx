import {Opening} from "../../../../domain/game/Opening";
import {Dialog, DialogContent, DialogTitle, Grid} from "@mui/material";
import Button from "@mui/material/Button";
import React from "react";

export interface NextMoveDialogProps{
    opening: Opening
    open: boolean
    onClose: () => void
    onMoveSelect: (move: Opening) => void
}

export function NextMoveDialog ({opening, open, onClose, onMoveSelect}: NextMoveDialogProps) {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Choose Your Next Move</DialogTitle>
            <DialogContent>
                <Grid container spacing={2}>
                    {opening.variantList.map((move, index) => (
                        <Grid item xs={4} key={index}>
                            <Button variant="outlined" onClick={() => onMoveSelect(move)}>
                                {Opening.giveOpeningNames(move)}
                            </Button>
                        </Grid>
                    ))}
                </Grid>
            </DialogContent>
        </Dialog>
    );
}

