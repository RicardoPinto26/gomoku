import {apiUrl} from "../../utils/configs";
import {EmbeddedSubEntity} from "../../http/media/siren/SubEntity";
import {Lobby} from "../../domain/Lobby";
import {get} from "../utils/fetchSiren";
import {CreateLobbyInputModel} from "./models/CreateLobbyInputModel";
import {LobbyDetailsOutputModel} from "./models/LobbyDetailsOutputModel";
import {SirenEntity} from "../../http/media/siren/SirenEntity";
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

export async function getLobby(lobbyId: number): Promise<SirenEntity<LobbyDetailsOutputModel>> {
    return get(`/lobby/${lobbyId}`)
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
    console.log(res);
    return res;
}

export async function matchMake(settings: GameSettings): Promise<{ status: number, response: any }> {

    const url = localStorage.getItem('seekLobby')
    if (!url) {
        localStorage.clear()
        throw null
    }
    const res = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(settings)
    });
    const body = await res.json();
    return {response: body, status: res.status};
}

