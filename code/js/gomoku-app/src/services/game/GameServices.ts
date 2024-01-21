import {GamePlayInputModel} from "./models/GamePlayInputModel";
import {get, post} from "../utils/fetchSiren";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
import {GameDetailsOutputModel} from "./models/GameDetailsOutputModel";

export class GameServices {

    static async getGame(lobbyId: number, gameID: number): Promise<SirenEntity<GameDetailsOutputModel>> {
        return get(`/api/lobby/${lobbyId}/game/${gameID}`)
    }

    static async play(lobbyId: number, gameId: number, move: GamePlayInputModel): Promise<SirenEntity<GameDetailsOutputModel>> {
        return post(`/api/lobby/${lobbyId}/game/${gameId}/play`, JSON.stringify(move))
    }

    static async forfeit(lobbyId: number, gameId: number): Promise<SirenEntity<GameDetailsOutputModel>> {
        return post(`/api/lobby/${lobbyId}/game/${gameId}/forfeit`)
    }
}