import {Grid, Paper } from "@mui/material"
import Page from "./common/Page"
import EmailIcon from '@mui/icons-material/Email';
import GitHubIcon from '@mui/icons-material/GitHub';

interface AuthorInfo {
    number: string
    name: string
    githubLink: string
    email: string
    image: string
}

const authors: AuthorInfo[] = [
    {
        number: "46631",
        name: "Francisco Medeiros",
        githubLink: "https://github.com/Francisco-Medeiros00",
        email: "a46631@alunos.isel.pt",
        image: "https://avatars.githubusercontent.com/u/91284180?v=4"
    },
    {
        number: "47671",
        name: "Luís Macário",
        githubLink: "https://github.com/Luis-Macario",
        email: "a47671@alunos.isel.pt",
        image: "https://avatars.githubusercontent.com/u/93235497?v=4"
    },
    {
        number: "47673",
        name: "Ricardo Pinto",
        githubLink: "https://github.com/RicardoPinto26",
        email: "a47673@alunos.isel.pt",
        image: "https://avatars.githubusercontent.com/u/40843306?v=4"
    }
]
export default function About(){


    return(
        <Page
            title={"Authors"}
        >
            <Grid container spacing={2}>
                {
                    authors.map((author) => (
                        <Grid item xs={4} key={author.number}>
                            <Paper elevation={3}>
                                <Grid item xs={12}>
                                    <h3>{author.name} - {author.number}</h3>
                                </Grid>

                                <Grid>
                                    <img
                                        src = {author.image} alt=""
                                        width={200}
                                        height={200}
                                    />
                                </Grid>

                                <Grid>
                                    <EmailIcon
                                        href={"mailto:" + author.email}
                                        sx={{cursor: 'pointer'}}
                                    />
                                    <GitHubIcon
                                        sx={{cursor: 'pointer'}}
                                        onClick={event =>  window.open(author.githubLink,'_blank')}
                                        focusable={true}
                                    />
                                </Grid>
                            </Paper>
                        </Grid>
                    ))
                }
            </Grid>
        </Page>
    )
}