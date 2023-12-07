import {SubEntity} from "./SubEntity";
import Link from "./Link";
import Action from "./Action";

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
export class SirenEntity<T>{
    class?: string[]
    properties?: T
    entities?: SubEntity[]
    links?: Link[]
    actions?: Action[]
    title?: string
}

