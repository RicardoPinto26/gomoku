/**
 * Entity for HTTP request problems
 *
 * @property type the type of problem
 * @property title title of the problem
 * @property status the HTTP status code of the problem
 * @property detail a more thorough description of the problem
 */
export interface Problem {
    type: string,
    title: string,
    status: number,
    detail?: string
}

export class Problem{
    constructor( problem : Problem) {
        this.type = problem.type
        this.title = problem.title
        this.status = problem.status
        this.detail = problem.detail
    }
}

export const problemMediaType = "application/problem+json"

export class UserAlreadyInALobbyProblem {
    type: string;
    title: string;
    status: number;
    detail: string;
    lobbyID: number;
    gameID: number

    constructor(json: any) {
        this.type = json.type;
        this.title = json.title;
        this.status = json.status;
        this.detail = json.detail;
        this.lobbyID = json.lobbyID;
        this.gameID = json.gameID;
    }
}
