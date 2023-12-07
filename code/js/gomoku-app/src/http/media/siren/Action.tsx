import Field from "./Field";

export default class Action {
    name: string;
    class?: string[];
    method?: string;
    href: string;
    title?: string;
    type?: string;
    fields?: Field[];

    constructor(name: string, href: string, classParam?: string[], method?: string, title?: string, type?: string, fields?: Field[]) {
        this.name = name;
        this.href = href;
        this.class = classParam;
        this.method = method;
        this.title = title;
        this.type = type;
        this.fields = fields;
    }
}