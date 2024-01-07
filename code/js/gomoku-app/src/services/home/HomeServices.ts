import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {get} from "../utils/fetchSiren";
import {HomeOutputModel} from "./models/HomeOutputModel";

export function getAndStoreHome() {
    if (localStorage.getItem('list_users') == null || localStorage.getItem('register') == null || localStorage.getItem('login') == null) {
        // @ts-ignore
        return get('/api').then((response: SirenEntity<HomeOutputModel>) => {
            const list_users = response.actions?.find((action) => {
                return action.name === "list-users"
            })?.href
            const register = response.actions?.find((action) => {
                return action.name === "register"
            })?.href
            const login = response.actions?.find((action) => {
                return action.name === "login"
            })?.href
            if (list_users === undefined || register === undefined || login === undefined) {
                throw new Error("No href found")
            }
            localStorage.setItem('list-users', list_users)
            localStorage.setItem('register', register)
            localStorage.setItem('login', login)
        })
    }
}