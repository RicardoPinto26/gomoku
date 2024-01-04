import {deleteRequest, get, post} from "../utils/fetchSiren";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {User} from "../../domain/User";
import {Problem} from "../../http/media/Problem";
import {LoginOutputModel} from "./models/LoginOutput";
import {RegisterOutputModel} from "./models/RegisterOutput";
import {getAndStoreHome} from "../home/HomeServices";

//TODO: Use siren hypermedia to get the url
export async function register(
    email: String,
    username: String,
    password: String
): Promise<SirenEntity<RegisterOutputModel>> {
    const register = localStorage.getItem('register')
    if (!register) {
        getAndStoreHome()
    }
    const url = register || localStorage.getItem('register')
    if (!url) throw new Error('Could not find register url')
    return post(
        url,
        JSON.stringify({"email": email, "username": username, "password": password})
    )
}


export async function login(
    username: String,
    password: String
): Promise<SirenEntity<LoginOutputModel>> {
    const login = localStorage.getItem('login')
    if (!login) {
        getAndStoreHome()
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
            if (logout === undefined || seekLobby === undefined) {
                throw new Error("No href found")
            }
            localStorage.setItem('logout', logout)
            localStorage.setItem('seekLobby', seekLobby)
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

export async function getUsers(): Promise<SirenEntity<User> | Problem> {
    return await get('/users')
}