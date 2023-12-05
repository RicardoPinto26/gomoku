import * as React from 'react';
import {Navigate, useLocation} from 'react-router-dom';
import {useSetUser} from '../Authn';
import {login} from "../../services/LoginService";
import reduce from "./utils/Reduce";

export async function authenticate(username: string, password: string): Promise<string | undefined> {
    const user = await login(username, password);
    console.log(`authenticate(${username}, ${password}) => ${user}`);
    return user;
}

export function Login() {
    console.log('Login');
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
        authenticate(username, password)
            .then(res => {
                if (res) {
                    console.log(`setUser(${res})`);
                    setUser(res);
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
    );
}