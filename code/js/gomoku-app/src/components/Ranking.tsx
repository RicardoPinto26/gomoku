import React, {useEffect, useState} from "react";
import Page from "./common/Page";
import {User} from "../domain/User";
import {EmbeddedSubEntity} from "../http/media/siren/SubEntity";
import {Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography} from "@mui/material";
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import {amber, brown, grey} from "@mui/material/colors";
import LoadingSpinner from "./common/LoadingSpinner";
import {getUsers} from "../services/users/UserServices";
import {handleRequest} from "../services/utils/fetchSiren";
import {handleError} from "../services/utils/errorUtils";
import {useNavigate} from "react-router-dom";

const prizes = [
    <EmojiEventsIcon style={{color: amber[500], fontSize: '25'}}/>,
    <EmojiEventsIcon style={{color: grey[500], fontSize: '25'}}/>,
    <EmojiEventsIcon style={{color: brown[500], fontSize: '25'}}/>
];
export default function Ranking() {

    const navigate = useNavigate()
    const [ranking, setRanking] = useState<User[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)


    useEffect(() => {
        fetchRanking()
    }, [])

    async function fetchRanking() {
        const [error, res] = await handleRequest(getUsers())

        if (error) {
            handleError(error, setError, navigate)
            return
        }

        if (res.entities === undefined) {
            throw new Error("Entities are undefined")
        }

        const users = res.entities.map(entity => (entity as EmbeddedSubEntity<User>).properties as User)
        setRanking(users)
        setIsLoading(false)

    }

    if (error) {
        return (
            <Typography color="error">
                {error}
            </Typography>
        )
    } else if (isLoading) {
        return <Box sx={{mt: 1}}>
            <LoadingSpinner text={"Loading rankings..."}/>
        </Box>
    } else {
        return (
            <Page title={"Ranking"}>
                <TableContainer component={Paper} sx={{width: '600px'}}>
                    <Table stickyHeader aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell size={'small'} padding={'none'}></TableCell>
                                <TableCell>Position</TableCell>
                                <TableCell>Username</TableCell>
                                <TableCell>Games Played</TableCell>
                                <TableCell>Rating</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                ranking.map((user, index) => (
                                    <TableRow key={user.username}
                                              sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                        <TableCell size={'small'} padding={'none'}
                                                   align={'center'}> {prizes[index]} </TableCell>
                                        <TableCell component="th" scope="row">{index + 1}</TableCell>
                                        <TableCell>{user.username}</TableCell>
                                        <TableCell>{user.gamesPlayed}</TableCell>
                                        <TableCell>{user.rating}</TableCell>
                                    </TableRow>
                                ))
                            }
                        </TableBody>
                    </Table>
                </TableContainer>
            </Page>
        )
    }
}