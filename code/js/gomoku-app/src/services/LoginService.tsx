const apiUrl = 'http://localhost:8080/api';

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
    const response = await fetch(`${apiUrl}/users/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, password})
    })

    if (!response.ok)
        throw await response.json();

    return await response.json()
}