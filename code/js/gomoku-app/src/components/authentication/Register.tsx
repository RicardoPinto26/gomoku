import * as React from "react";
import {useSetUser} from "../../utils/Authn";
import {Navigate, useLocation, useNavigate} from "react-router-dom";
import reduce from "./utils/Reduce"
import { register, storeLoginUrls} from "../../services/users/UserServices";
import Page from "../common/Page";
import Button from "@mui/material/Button";
import {handleRequest} from "../../services/utils/fetchSiren";
import {handleError} from "../../services/utils/errorUtils";

export default function Register() {
    const navigate = useNavigate()
    const [error, setError] = React.useState<string | null>(null);
    const [showPassword, setShowPassword] = React.useState(false)

    const [state, dispatch] =
        React.useReducer(reduce, {tag: 'editing', inputs: {username: '', email: '', password: ''}});
    const setUser = useSetUser();
    const location = useLocation();
    if (state.tag === 'redirect') {
        return <Navigate to={location.state?.source?.pathname || '/me'} replace={true}/>;
    }

    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        dispatch({type: 'edit', inputName: ev.currentTarget.name, inputValue: ev.currentTarget.value});
    }

    async function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }
        dispatch({type: 'submit'});

        const email = state.inputs.email;
        const username = state.inputs.username;
        const password = state.inputs.password;

        const [error, res] = await handleRequest(register(email, username, password))

        if (error) {
            handleError(error, setError, navigate)
            dispatch({type: 'error', message: `${error}`});
            return
        }

        if (res) {
            storeLoginUrls(res)
            setUser(username);
            dispatch({type: 'success'});
        }
    }

    const username = state.tag === 'submitting' ? state.username : state.inputs.username
    const email = state.tag === 'submitting' ? '' : state.inputs.email
    const password = state.tag === 'submitting' ? "" : state.inputs.password
    return (
        <Page title={"Register"}>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={state.tag !== 'editing'} className="input-field">
                    <div>
                        <label htmlFor="email">Email</label>
                        <input id="email" type="text" name="email" value={email} onChange={handleChange}/>
                    </div>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input id="username" type="text" name="username" value={username} onChange={handleChange}/>
                        <div>
                            <label htmlFor="password">Password</label>
                            <input id="password" type={showPassword ? "text" : "password"} name="password"
                                   value={password}
                                   onChange={handleChange}/>
                            <div>
                                <label htmlFor="showPassword">Show password</label>
                                <input id="showPassword" type="checkbox" checked={showPassword}
                                       onChange={() => setShowPassword(!showPassword)}/>
                            </div>
                        </div>
                        <button type="submit">Register</button>
                    </div>
                </fieldset>
            </form>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            <Button
                size="small"
                variant="contained"
                sx={{mt: 3, mb: 2}}
                color="primary"
                onClick={() => {
                    navigate('/login')
                }}
            >
                Already have an account? Login!
            </Button>
        </Page>
    );
}