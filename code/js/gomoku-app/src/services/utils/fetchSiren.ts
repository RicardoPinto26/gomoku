import {SirenEntity, sirenMediaType} from "../../http/media/siren/SirenEntity";
import {Problem, problemMediaType} from "../../http/media/Problem";
import {apiUrl} from "../../utils/configs";

export async function fetchSiren<T>(
    input : RequestInfo | URL,
    method : string,
    body : BodyInit | undefined
) : Promise<SirenEntity<T>>{

    const headers: any = {
        'Accept': `${sirenMediaType}, ${problemMediaType}`,
        'Content-Type': 'application/json',
    }

    const res = await fetch(input,{
        method: method,
        headers,
        body: body,
        credentials: 'include'
    })

    const json = await res.json()

    if (!res.ok)
        throw new Problem( json )

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