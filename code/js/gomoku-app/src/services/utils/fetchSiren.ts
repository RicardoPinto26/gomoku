import {SirenEntity, sirenMediaType} from "../../http/media/siren/SirenEntity";
import {Problem, problemMediaType, UserAlreadyInALobbyProblem} from "../../http/media/Problem";
import {apiUrl} from "../../utils/configs";

export async function fetchSiren<T>(
    input: RequestInfo | URL,
    method: string,
    body: BodyInit | undefined
): Promise<SirenEntity<T>> {

    const headers: any = {
        'Accept': `${sirenMediaType}, ${problemMediaType}`,
        'Content-Type': 'application/json',
    }

    const res = await fetch(input, {
        method: method,
        headers,
        body: body,
        credentials: 'include'
    })

    const json = await res.json()

    if (!res.ok) {
        if ("lobbyID" in json) {
            throw new UserAlreadyInALobbyProblem(json)
        } else {
            throw new Problem(json)
        }
    }

    return new SirenEntity<T>(json)
}

export function get<T>(input: RequestInfo | URL): Promise<SirenEntity<T>> {
    return fetchSiren<T>(apiUrl + input, 'GET', undefined)
}

export function post<T>(
    input: RequestInfo | URL,
    body?: BodyInit,
): Promise<SirenEntity<T>> {
    return fetchSiren<T>(apiUrl + input, 'POST', body)
}

export function deleteRequest<T>(input: RequestInfo | URL): Promise<SirenEntity<T>> {
    return fetchSiren<T>(apiUrl + input, 'DELETE', undefined)
}

export async function handleRequest<T, E = Error>(promise: Promise<T>): Promise<[E, null] | [null, T]> {
    try {
        const res: T = await promise;
        return [null, res];
    } catch (err) {
        return [err as E, null];
    }
}