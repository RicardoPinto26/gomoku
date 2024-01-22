import {deleteRequest, get, post} from "../utils/fetchSiren";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {User} from "../../domain/User";
import {LoginOutputModel} from "./models/LoginOutput";
import {getAndStoreHome} from "../home/HomeServices";

export async function register(
    email: String,
    username: String,
    password: String
): Promise<SirenEntity<LoginOutputModel>> {
    const register = localStorage.getItem('register')
    if (!register) {
        await getAndStoreHome()
    }
    const url = register || localStorage.getItem('register')
    if (!url) throw new Error('Could not find register url')

    return post(
        url,
        JSON.stringify({"email": email, "username": username, "password": password})
    ).then(async () => {
        return await login(username, password);
    })
}


export async function login(
    username: String,
    password: String
): Promise<SirenEntity<LoginOutputModel>> {
    const login = localStorage.getItem('login')
    if (!login) {
        await getAndStoreHome()
    }
    const url = login || localStorage.getItem('login')
    if (!url) throw new Error('Could not find login url')
    return await post(
        url,
        JSON.stringify({"username": username, "password": password})
    )
}

export function storeLoginUrls(siren: SirenEntity<LoginOutputModel>): void {
    if (localStorage.getItem('logout') == null || localStorage.getItem('seekLobby') == null) {

        console.log(siren)

        const url = siren.links?.find((link) => {
            return link.rel.find((str) => {
                return str === "user-home"
            }) !== undefined
        })?.href

        if (url === undefined) {
            throw new Error("No href found")
        }

        get(url).then((response: SirenEntity<unknown>) => {
            const logout = response.actions?.find((action) => {
                return action.name === "logout"
            })?.href
            const seekLobby = response.actions?.find((action) => {
                return action.name === "seek-lobby"
            })?.href
            const createLobby = response.actions?.find((action) => {
                return action.name === "create-lobby"
            })?.href
            const listLobbies = response.actions?.find((action) => {
                return action.name === "list-lobbies"
            })?.href
            if (logout === undefined || seekLobby === undefined || createLobby === undefined || listLobbies === undefined) {
                throw new Error("No href found")
            }
            localStorage.setItem('logout', logout)
            localStorage.setItem('seekLobby', seekLobby)
            localStorage.setItem('createLobby', createLobby)
            localStorage.setItem('listLobbies', listLobbies)

        })
    }
}

export async function logout() {
    const url = localStorage.getItem('logout')
    if (!url) throw new Error('Could not find logout url')
    return await deleteRequest(
        url
    )
}

export async function getUsers(): Promise<SirenEntity<User>> {
    const users = localStorage.getItem('list-users')
    if (!users) {
        await getAndStoreHome()
    }
    const url = users || localStorage.getItem('list-users')
    if (!url) throw new Error('Could not find list-users url')

    return await get(url)
}