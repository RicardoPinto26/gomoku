import Action from "./Action";
import Link from "./Link";

export abstract class SubEntity {
    abstract rel: string[];
}

export class EmbeddedLink extends SubEntity {
    rel: string[];
    class?: string[];
    href: string;
    type?: string;
    title?: string;

    constructor(rel: string[], href: string, classParam?: string[], type?: string, title?: string) {
        super();
        this.rel = rel;
        this.href = href;
        this.class = classParam;
        this.type = type;
        this.title = title;
    }
}

export class EmbeddedSubEntity<T> extends SubEntity {
    class?: string[];
    rel: string[];
    properties?: T;
    entities?: SubEntity[];
    links?: Link[];
    actions?: Action[];
    title?: string;

    constructor(rel: string[], classParam?: string[], properties?: T, entities?: SubEntity[], links?: Link[], actions?: Action[], title?: string) {
        super();
        this.rel = rel;
        this.class = classParam;
        this.properties = properties;
        this.entities = entities;
        this.links = links;
        this.actions = actions;
        this.title = title;
    }
}