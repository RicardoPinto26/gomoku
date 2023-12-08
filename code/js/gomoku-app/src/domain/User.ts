export interface User {
    username: string
    gamesPlayed: number
    rating: number
}

export class User {
    constructor(username: string, gamesPlayed: number, rating: number) {
        this.username = username
        this.gamesPlayed = gamesPlayed
        this.rating = rating
    }
}