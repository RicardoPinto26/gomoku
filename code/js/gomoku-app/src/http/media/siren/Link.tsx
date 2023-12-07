export default class Link {
    rel: string[];
    class?: string[];
    href: string;
    type?: string;
    title?: string;

    constructor(rel: string[], href: string, classParam?: string[], type?: string, title?: string) {
        this.rel = rel;
        this.href = href;
        this.class = classParam;
        this.type = type;
        this.title = title;
    }
}