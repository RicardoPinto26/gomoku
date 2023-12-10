import {apiUrl} from "../../utils/configs";

//TODO: Use siren hypermedia to get the url
export async function register(email: String, username: String, password: String) {
    console.log(`register(${email}, ${username}, ${password})`)
    const response = await fetch(`${apiUrl}/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({"email": email, "username": username, "password": password})
    })

    if (!response.ok)
        throw await response.json();

    return await response.json()
}


export async function login(username: String, password: String) {
    const response = await fetch(`${apiUrl}/users/token`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: "include",
        body: JSON.stringify({"username": username, "password": password})
    })
    if (!response.ok)
        throw await response.json();

    return await response.json()
}

export async function logout() {
    const response = await fetch(`${apiUrl}/users/logout`, {
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify({"ola": "another thing", "adeus": "another another thing"})
    })

    if (!response.ok) {
        throw await response.json();
    }


    return await response.json()
}