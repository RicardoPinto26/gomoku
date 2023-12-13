import {apiUrl} from "../../utils/configs";
import {GamePlayInputModel} from "./modals/GamePlayInputModel";

export class GameServices {

    static async getGame(lobbyId: number, gameID: number) {
        const response = await fetch(`${apiUrl}/lobby/${lobbyId}/game/${gameID}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include"
        });
        if (response.status === 200) {
            return response.json();
        } else {
            throw await response.json()
            //throw new Error("Error getting game");
        }
    }

    static async makeMove(lobbyId: number, gameId: number, move: GamePlayInputModel) {
        console.log("lobbyId: " + lobbyId)
        console.log("gameId: " + gameId)
        console.log("move: " + JSON.stringify(move))
        const response = await fetch(`${apiUrl}/lobby/${lobbyId}/game/${gameId}/play`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify(move)
        });
        if (response.status === 200) {
            return response.json();
        } else {
            throw await response.json()
        }
    }
}