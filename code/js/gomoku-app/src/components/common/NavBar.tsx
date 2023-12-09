import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import {useNavigate} from "react-router-dom";
import {ReactComponent as GomokuLogo} from '../../logo.svg';
import {useCurrentUser, useUserManager} from "../Authn";
import {Avatar, ListItemIcon, Tooltip} from "@mui/material";
import VideogameAssetIcon from '@mui/icons-material/VideogameAsset';
import LeaderboardIcon from '@mui/icons-material/Leaderboard';
import InfoIcon from '@mui/icons-material/Info';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';

const pages = [
    {name: 'Play', href: '/play', auth: true, icon: <VideogameAssetIcon/>},
    {name: 'Ranking', href: '/ranking', icon: <LeaderboardIcon/>},
    {name: 'About', href: '/about', icon: <InfoIcon/>},
]

export function NavBar() {
    const navigate = useNavigate()
    const user = useCurrentUser()
    const loggedIn = !!(user)

    const userManager = useUserManager();
    console.log("Current user:" + user)

    const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
    const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

    const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };
    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const settings = [
        {
            name:'Profile',
            icon: <AccountBoxOutlinedIcon/>,
            callback: () => navigate('/me')

        },
        {
            name:'Logout',
            icon: <LogoutOutlinedIcon/>,
            callback: async () => {
                if(!user){
                    navigate('/')
                    return
                }

                //await logout(user)
                userManager.clearUser()
                navigate('/')
            }
        }
    ];


    return (
        <AppBar position="static">
            <Container maxWidth="xl">
                <Toolbar disableGutters>

                    <GomokuLogo
                        onClick={() => navigate('/')}
                        style={{
                            width: 50,
                            height: 'auto',
                            cursor: 'pointer',
                        }}
                    />

                    <Typography
                        variant="h6"
                        noWrap
                        component="a"
                        sx={{
                            mr: 2,
                            display: {xs: 'none', md: 'flex'},
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.3rem',
                            color: 'inherit',
                            textDecoration: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => navigate('/')}
                    >
                        GOMOKU ROYALE
                    </Typography>

                    <Box sx={{flexGrow: 1, display: {xs: 'flex', md: 'none'}}}>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            sx={{
                                display: {xs: 'block', md: 'none'},
                            }}
                        >
                            {pages.map((page) => (
                                (page.auth && loggedIn || !page.auth && !loggedIn || page.auth === undefined) &&
                                <MenuItem key={page.name} onClick={() => {
                                    handleCloseNavMenu()
                                    navigate(page.href)
                                }}>
                                    <ListItemIcon>
                                        {page.icon}
                                    </ListItemIcon>
                                    <Typography textAlign="center">{page.name}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    <Typography
                        variant="h5"
                        noWrap
                        component="a"
                        sx={{
                            mr: 2,
                            display: {xs: 'flex', md: 'none'},
                            flexGrow: 1,
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.3rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        Gomoku Royale
                    </Typography>
                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        {pages.map((page) => (
                            (page.auth && loggedIn || !page.auth && !loggedIn || page.auth === undefined) &&
                            <Button
                                key={page.name}
                                onClick={() => {
                                    handleCloseNavMenu()
                                    navigate(page.href)
                                }}
                                endIcon={page.icon}
                                sx={{my: 2, color: 'white', display: 'box'}}
                            >
                                {page.name}
                            </Button>
                        ))}
                    </Box>


                    {loggedIn
                        ?
                        <Box sx={{flexGrow: 0}}>
                            <Tooltip title="Open settings">
                                <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                                    <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg"/>
                                </IconButton>
                            </Tooltip>
                            <Menu
                                sx={{mt: '45px'}}
                                id="menu-appbar"
                                anchorEl={anchorElUser}
                                anchorOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                keepMounted
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                open={Boolean(anchorElUser)}
                                onClose={handleCloseUserMenu}
                            >
                                {settings.map((setting) => (
                                    <MenuItem key={setting.name}
                                              onClick={()=>{
                                                  handleCloseUserMenu()
                                                  setting.callback()
                                              }
                                    }>
                                        <ListItemIcon>
                                            {setting.icon}
                                        </ListItemIcon>
                                        <Typography textAlign="center">{setting.name}</Typography>
                                    </MenuItem>
                                ))}
                            </Menu>
                        </Box>

                        :

                        <Box>
                            <Button
                                key="Sign In"
                                startIcon=<AccountCircleIcon/>
                                onClick={() => {
                                    handleCloseNavMenu()
                                    navigate("/register")
                                }}
                                sx={{my: 2, color: 'white', display: 'box'}}
                            >
                                Sign In
                            </Button>
                        </Box>
                    }
                </Toolbar>
            </Container>
        </AppBar>
    )
}