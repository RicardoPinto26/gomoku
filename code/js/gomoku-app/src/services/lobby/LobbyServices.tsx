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
        return post(`/api/lobby`, JSON.stringify(settings))
    }


    static async getLobby(lobbyId: number): Promise<SirenEntity<LobbyDetailsOutputModel>> {
        return get(`/api/lobby/${lobbyId}`)
    }


    static async getLobbies() {
        return get(`/api/lobby/available`)
    }


    static async joinLobby(lobbyId: number): Promise<SirenEntity<LobbyJoinOutputModel>> {
        return post(`/api/lobby/${lobbyId}/join`)
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

