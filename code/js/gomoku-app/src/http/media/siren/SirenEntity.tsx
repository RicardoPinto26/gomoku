import {SubEntity} from "./SubEntity";
import Link from "./Link";
import Action from "./Action";

export interface SirenEntityProps<T>{
    class?: string[]
    properties?: T
    entities?: SubEntity[]
    links?: Link[]
    actions?: Action[]
    title?: string
}


/**
 * Siren Entity
 *
 * @see <a href="https://github.com/kevinswiber/siren"">Siren Specification</a>
 *
 * @property class describes the nature of an entity's content based on the current representation.Optional
 * @property properties a set of key-value pairs that describe the state of an entity.Optional
 * @property entities a collection of related sub-[SubEntity].Optional
 * @property links a collection of items that describe navigational [Link]s, distinct from entity relationships.Optional
 * @property actions A collection of [Action] objects, represented in JSON Siren as an array such as { "actions": [{ ... }] }.Optional
 * @property title the title of the siren entity. Optional
 */
export class SirenEntity<T> implements SirenEntityProps<T>{

    class?: string[]
    properties?: T
    entities?: SubEntity[]
    links?: Link[]
    actions?: Action[]
    title?: string

    constructor( props : SirenEntityProps<T>) {
        this.class = props.class
        this.properties = props.properties
        this.entities = props.entities
        this.links = props.links
        this.actions = props.actions
        this.title = props.title
    }
}

export const sirenMediaType = "application/vnd.siren+json"
