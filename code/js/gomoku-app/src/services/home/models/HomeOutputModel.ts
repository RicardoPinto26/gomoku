import {AuthorModel} from "./AuthorModel";

export interface HomeOutputModel {
    title: String,
    version: String,
    description: String,
    authors: AuthorModel[]
}