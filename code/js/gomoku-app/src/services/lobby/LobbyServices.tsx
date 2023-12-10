import {apiUrl} from "../../utils/configs";
import {EmbeddedSubEntity} from "../../http/media/siren/SubEntity";
import {Lobby} from "../../domain/Lobby";
import {GameSettings} from "../../components/game/matchmake/GameSettings";


export async function createLobbyServices(settings: CreateLobbyInputModel): Promise<{ status: number, response: any }> {
    console.log(JSON.stringify(settings))
    const response = await fetch(`${apiUrl}/lobby`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(settings)
    })

    if (!response.ok)
        throw await response.json();

    const body = await response.json();
    return {status: response.status, response: body}
}

export async function getLobbies() {
    const response = await fetch(`${apiUrl}/lobby/available`, {
        method: "GET",
        credentials: "include",
    })

    if (!response.ok)
        throw await response.json();

    return (await (response.json())).entities.map((entity: EmbeddedSubEntity<Lobby>) => (entity as EmbeddedSubEntity<Lobby>).properties as Lobby)
}

export async function joinLobby(lobbyId: number): Promise<{ joinned: boolean, response: any }> {
    const response = await fetch(`${apiUrl}/lobby/${lobbyId}/join`, {
        method: "POST",
        credentials: "include",
    })

    if (!response.ok)
        throw await response.json();

    const body = await response.json();
    return {joinned: response.status == 200, response: body}
}

export async function getLobbyState(lobby: number) {
    let res = await (await fetch(`${apiUrl}/lobby/${lobby}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
    })).json();
    console.log("Entrou getLobbyState: ");
    console.log(res);
    return res;
}

export interface CreateLobbyInputModel {
    name: string,
    gridSize: number;
    winningLength: number;
    opening: string;
    pointsMargin: number;
    overflow: boolean;
}

export class CreateLobbyInputModel {
    constructor(name: string, settings: GameSettings) {
        this.name = name
        this.gridSize = settings.gridSize
        this.winningLength = settings.winningLength
        this.opening = settings.opening
        this.pointsMargin = settings.pointsMargin
        this.overflow = settings.overflow
    }
}