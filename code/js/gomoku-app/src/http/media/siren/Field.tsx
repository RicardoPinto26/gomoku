export default class Field {
    name: string;
    class?: string[];
    type?: string;
    value?: string;
    title?: string;

    constructor(name: string, type?: string, value?: string, classParam?: string[], title?: string) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.class = classParam;
        this.title = title;
    }
}