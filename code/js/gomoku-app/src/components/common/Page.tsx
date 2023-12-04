import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import {ReactNode} from "react";

/**
 * Properties of a page
 *
 * @property title Title of the page, optional
 * @property children ReactNodes that compose the page
 */
interface PageProps {
    title?: string,
    children: ReactNode
}

export default function Page({title, children}: PageProps) {
    return (
        <Container maxWidth="xs">
            <h1>{title}</h1>
            <Box sx={{
                marginTop: 8,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}>
                {children}
            </Box>
        </Container>
    )
}