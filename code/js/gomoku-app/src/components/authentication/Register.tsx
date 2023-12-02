import * as React from "react";
import {useSetUser} from "../Authn";
import {Navigate, useLocation} from "react-router-dom";
import reduce from "./utils/Reduce"
import {register} from "../../services/LoginService";

async function registerUser(email:String, username: string, password: string): Promise<string | undefined> {
    console.log(`registerUser(${username}, ${password})`)
    const user = await register(email, username, password);
    console.log(`registerUser(${username}, ${password}) => ${user}`);
    return user;

}

export default function Register() {
    console.log('Register');
    const [state, dispatch] = React.useReducer(reduce, { tag: 'editing', inputs: { username: '', password: '' } });
    const setUser = useSetUser();
    const location = useLocation();
    if (state.tag === 'redirect') {
        return <Navigate to={location.state?.source?.pathname || '/me'} replace={true} />;
    }
    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        dispatch({ type: 'edit', inputName: ev.currentTarget.name, inputValue: ev.currentTarget.value });
    }
    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault();
        if (state.tag !== 'editing') {
            return;
        }
        dispatch({ type: 'submit' });

        const email = "test1111@gmail.com"//state.inputs.email;
        const username = state.inputs.username;
        const password = state.inputs.password;
        registerUser(email, username, password)
            .then(res => {
                if (res) {
                    console.log(`setUser(${res})`);
                    setUser(res);
                    dispatch({ type: 'success' });
                } else {
                    dispatch({ type: 'error', message: 'Invalid username or password' });
                }
            })
            .catch(error => {
                dispatch({ type: 'error', message: error.message });
            });
    }

    const username = state.tag === 'submitting' ? state.username : state.inputs.username
    const password = state.tag === 'submitting' ? "" : state.inputs.password
    return (
        <form onSubmit={handleSubmit}>
            <fieldset disabled={state.tag !== 'editing'}>
                <div>
                    <label htmlFor="email">Username</label>
                    <input id="email" type="text" name="email" value={username} onChange={handleChange}/>
                </div>
                <div>
                    <label htmlFor="username">Username</label>
                    <input id="username" type="text" name="username" value={username} onChange={handleChange}/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input id="password" type="text" name="password" value={password} onChange={handleChange}/>
                </div>
                <div>
                    <button type="submit">Register</button>
                </div>
            </fieldset>
            {state.tag === 'editing' && state.error}
        </form>
    );
}