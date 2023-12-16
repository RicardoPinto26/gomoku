import {get, post} from "../utils/fetchSiren";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {User} from "../../domain/User";
import {Problem} from "../../http/media/Problem";
import {LoginOutputModel} from "./models/LoginOutput";
import {RegisterOutputModel} from "./models/RegisterOutput";

//TODO: Use siren hypermedia to get the url
export async function register(
    email: String,
    username: String,
    password: String
):Promise<SirenEntity<RegisterOutputModel>> {
    return post(
        '/users',
        JSON.stringify({"email": email, "username": username, "password": password})
    )
}


export async function login(
    username: String,
    password: String
) :Promise<SirenEntity<LoginOutputModel>> {
    return await post(
        '/users/token',
        JSON.stringify({"username": username, "password": password})
    )
}

export async function logout() {
    return await post(
        '/users/logout',
        JSON.stringify({"ola": "another thing", "adeus": "another another thing"})
    )
}

export async function getUsers(): Promise<SirenEntity<User> | Problem> {
    return await get('/users')
}