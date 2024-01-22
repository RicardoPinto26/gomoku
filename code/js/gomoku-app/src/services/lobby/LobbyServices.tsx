import {get, post} from "../utils/fetchSiren";
import {CreateLobbyInputModel} from "./models/CreateLobbyInputModel";
import {LobbyDetailsOutputModel} from "./models/LobbyDetailsOutputModel";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {GameSettings} from "../../components/game/matchmake/GameSettings";
import {LobbyCreateOutputModel} from "./models/LobbyCreateOutputModel";
import {LobbyJoinOutputModel} from "./models/LobbyJoinOutputModel";
import {LobbySeekOutputModel} from "./models/LobbySeekOutputModel";


export class LobbyServices {
    static async createLobby(settings: CreateLobbyInputModel): Promise<SirenEntity<LobbyCreateOutputModel>> {
        const url = localStorage.getItem('createLobby')
        if (!url) {
            localStorage.clear()
            throw null
        }
        return post(url, JSON.stringify(settings))
    }


    static async getLobby(lobbyId: number): Promise<SirenEntity<LobbyDetailsOutputModel>> {
        return get(`/api/lobby/${lobbyId}`)
    }


    static async getLobbies() {
        const url = localStorage.getItem('listLobbies')
        if (!url) {
            localStorage.clear()
            throw null
        }
        return get(url)
    }


    static async joinLobby(url: string): Promise<SirenEntity<LobbyJoinOutputModel>> {
        return post(url)
    }


    static async matchMake(settings: GameSettings): Promise<SirenEntity<LobbySeekOutputModel>> {

        const url = localStorage.getItem('seekLobby')
        if (!url) {
            localStorage.clear()
            throw null
        }

        return post(url, JSON.stringify(settings))
    }
}

