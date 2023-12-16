import * as React from 'react';
import {Navigate, useLocation, useNavigate} from 'react-router-dom';
import {useSetUser} from '../../utils/Authn';
import {login} from "../../services/users/UserServices";
import reduce from "./utils/Reduce";
import Page from '../common/Page';
import Button from "@mui/material/Button";

export function Login() {
    const navigate = useNavigate()
    const [state, dispatch] = React.useReducer(reduce, {tag: 'editing', inputs: {username: '', email: '', password: ''}});
    const setUser = useSetUser();
    const location = useLocation();
    if (state.tag === 'redirect') {
        return <Navigate to={location.state?.source?.pathname || '/me'} replace={true}/>;
    }

    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        dispatch({type: 'edit', inputName: ev.currentTarget.name, inputValue: ev.currentTarget.value});
    }

    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }
        dispatch({type: 'submit'});
        const username = state.inputs.username;
        const password = state.inputs.password;
        login(username, password)
            .then(res => {
                if (res) {
                    console.log(`setUser(${res.properties?.token})`);
                    setUser(username);
                    dispatch({type: 'success'});
                } else {
                    dispatch({type: 'error', message: 'Invalid username or password'});
                }
            })
            .catch(error => {
                dispatch({type: 'error', message: error.message});
            });
    }

    const username = state.tag === 'submitting' ? state.username : state.inputs.username
    const password = state.tag === 'submitting' ? "" : state.inputs.password
    return (
        <Page title={"Login"}>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={state.tag !== 'editing'}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input id="username" type="text" name="username" value={username} onChange={handleChange}/>
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input id="password" type="text" name="password" value={password} onChange={handleChange}/>
                    </div>
                    <div>
                        <button type="submit">Login</button>
                    </div>
                </fieldset>
                {state.tag === 'editing' && state.error}
            </form>
            <Button>
                <Button
                    size="small"
                    variant="contained"
                    sx={{mt: 3, mb: 2}}
                    color="primary"
                    onClick={() => {
                        navigate('/register')
                    }}
                >
                    Don't have an account yet? Register!
                </Button>
            </Button>
        </Page>
    );
}