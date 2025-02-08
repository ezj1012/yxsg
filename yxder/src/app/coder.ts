import { get, post } from "./httpa";
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import type { EntityModel, YXCoder } from "./yxcoder";
import { coderLanguageName } from "./monaco";


export class EntityMonacoCode {
    modelEditor: monaco.editor.IStandaloneCodeEditor
    changeContent: (content: string) => any
    constructor(elId: string, changeContent: (content: string) => any) {
        this.changeContent = changeContent
        this.modelEditor = monaco.editor.create(document.getElementById(elId)!, {
            value: ``,
            language: coderLanguageName,
            // language: 'typescript',
            // theme: 'vs-dark',
            theme: 'cc',
            automaticLayout: true,
            minimap: {
                enabled: false
            }
        });
        this.modelEditor.onDidChangeModelContent(async () => { await changeContent(this.modelEditor.getValue()) })
        this.modelEditor.trigger('keyboard', 'editor.action.triggerSuggest', {});
    }

    setValue(model: EntityModel) {
        this.modelEditor.setValue(model.attrs?.join('\n') || '')
    }

    dispose() {
        this.modelEditor?.dispose()
    }

    formatContent() {
        const hint = new SimpleModelAttrHint()
        const value = this.modelEditor.getValue().split('\n').map(line => hint.update(line).format()).join('\n')
        this.modelEditor.setValue(value)
    }

    focus() {
        this.modelEditor.focus()
    }
}

export enum AttrType {
    s40 = "s40",    // String varchar(40) 名字,邮箱
    s100 = "s100",  // String varchar(100) 名字,文件名
    s200 = "s200", // String varchar(200)  
    s500 = "s500",
    s1000 = "s1000",
    s2000 = "s2000",
    tiny = "tiny",   // int 256 tinyint
    small = "small", // int 65535 smallint
    int = "int",    // int 21亿 int
    big = "big", // long bigint
}

// export interface ModelAttr {
//     name?: string;
//     notNullSymbol?: boolean;
//     typeSymbol?: boolean;
//     type?: string | AttrType;
//     typeOK?: boolean;
//     defaultValueSymbol?: boolean;
//     defaultValue?: string;   // 
//     defaultValueOK?: boolean;    //
//     zhNameSymbol?: boolean; // '//'
//     zhName?: string; // 
//     comment?: string; // 
//     ref?: string;
// }

export class SimpleModelAttr {
    type?: string | AttrType
    name: string = ''
    ref?: string
    notNullSymbol: boolean = false
    typeSymbol: boolean = false
    typeOK: boolean = false
    defaultValueSymbol: boolean = false
    defaultValue?: string
    defaultValueOK: boolean = false
    zhNameSymbol: boolean = false
    zhName?: string

    toFieldString(format: boolean = false) {
        if (format) {
            this.name = this.name.trim()
            if (this.type) {
                this.type = ` ${this.type?.trim()} `
            }
            if (this.defaultValue) {
                this.defaultValue = ` ${this.defaultValue?.trim()} `
            } else if (this.defaultValueSymbol) {
                this.defaultValue = ' '
            }

            if (this.zhName) {
                this.zhName = ` ${this.zhName?.trim()} `
            } else if (this.zhNameSymbol) {
                this.zhName = ' '
            }
        }
        return `${this.name || ''}${this.notNullSymbol ? '!' : ''}${this.typeSymbol ? ':' : ''}${this.type || ''}${this.defaultValueSymbol ? '=' : ''}${this.defaultValue || ''}${this.zhNameSymbol ? '//' : ''}${this.zhName || ''}`;
    }

}

export class SimpleModelAttrHint {
    attr: SimpleModelAttr;
    constructor() {
        this.attr = new SimpleModelAttr()
        this.updateContent()
    }

    update(content: string = '', form: string = '') {
        this.updateContent(content)
        return this;
    }

    private updateContent(content: string = '') {
        let line = content.replace(/\u00a0/g, ' '); //  &nbsp; 转为普通空格
        let t = line.indexOf("//");
        if (t >= 0) {
            this.attr.zhNameSymbol = true;
            this.attr.zhName = line.substring(t + 2)
            line = line.substring(0, t)
        } else {
            this.attr.zhNameSymbol = false;
            this.attr.zhName = ''
        }

        // 默认值
        t = line.indexOf("=");
        if (t >= 0) {
            this.attr.defaultValueSymbol = true
            this.attr.defaultValue = line.substring(t + 1)
            line = line.substring(0, t)
        } else {
            this.attr.defaultValueSymbol = false
            this.attr.defaultValue = ''
            this.attr.defaultValueOK = false
        }

        t = line.indexOf(":");
        if (t >= 0) {
            this.attr.typeSymbol = true
            this.attr.type = line.substring(t + 1)
            this.attr.typeOK = AttrTypeMapping[this.attr.type.trim()] != null
            line = line.substring(0, t)
        } else {
            this.attr.typeSymbol = false
            this.attr.type = ''
            this.attr.typeOK = false
        }

        t = line.trimEnd().indexOf("!")
        if (t >= 0) {
            this.attr.notNullSymbol = true
            line = line.substring(0, t)
        } else {
            this.attr.notNullSymbol = false
        }


        this.attr.name = line;
    }

    format() {
        const content = this.attr.toFieldString(true);
        this.updateContent(content);
        return content
    }

    getZhName() { return this.attr.zhName?.replace(/ /g, '\u00a0'); }
    getDefaultValue() { return this.attr.defaultValue?.replace(/ /g, '\u00a0'); }
    getType() { return this.attr.type?.replace(/ /g, '\u00a0'); }
    getName() { return this.attr.name.replace(/ /g, '\u00a0'); }
    isNumber() { return this.attr.type === undefined ? false : isNumber(this.attr.type) }
}

export function isNumber(type: AttrType | string) {
    return type == AttrType.tiny ||
        type == AttrType.small ||
        type == AttrType.int ||
        type == AttrType.big
}

export const AttrTypeMapping: { [key: string]: string } = {
    s40: 'varchar(40)',
    s100: 'varchar(100)',
    s200: 'varchar(200)',
    s500: 'varchar(500)',
    s1000: 'varchar(1000)',
    s2000: 'varchar(2000)',
    tiny: 'tinyint',
    int: 'int',
    small: 'smallint',
    big: 'bigint',
}
