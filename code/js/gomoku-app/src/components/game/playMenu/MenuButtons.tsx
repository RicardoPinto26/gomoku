import Button from "@mui/material/Button";
import React from "react";

interface MenuButtonProps {
    title: string;
    //icon?: React.ReactNode
    onClick?: () => void;
}

export default function MenuButton({title, onClick}: MenuButtonProps) {
    return (
        <Button
            fullWidth
            size="large"
            variant="contained"
            sx={{mt: 3, mb: 2}}
            color="primary"
            onClick={onClick}
        >
            {title}
        </Button>
    );
}