
import * as monaco from 'monaco-editor'
import editorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker'
import jsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker'
import cssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker'
import htmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker'
import tsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker'

export const coderLanguageName = 'entity-coder'


monaco.languages.register({ id: coderLanguageName })
monaco.languages.setMonarchTokensProvider(coderLanguageName, {
    keywords: [
        "aint",
        "along",
        "nint",
        "nlong",
    ],
    defKeywords: [
        "id",
        "createTime",
        "modifyTime",
    ],
    typeKeywords: [
        "s40",    // String varchar(40) 名字,邮箱
        "s100",  // String varchar(100) 名字,文件名
        "s200", // String varchar(200)  
        "s500",
        "s1000",
        "s2000",
        "tiny",   // int 256 tinyint
        "int2", // int 256 tinyint
        "small", // int 65535 smallint
        "int4", // int 65535 smallint
        "int",    // int 21亿 int
        "big", // long bigint
        "long", //  long bigint
        "number", // int 21亿 int
        "string", //  String varchar(200)  
        "str", //  String varchar(200)  
    ],
    operators: [
        ":",
        "=",
        "!",
    ],
    symbols: /[=><!~?:&|+\-*\/\^%]+/,
    escapes: /\\(?:[abfnrtv\\"']|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,
    digits: /\d+(_+\d+)*/,
    octaldigits: /[0-7]+(_+[0-7]+)*/,
    binarydigits: /[0-1]+(_+[0-1]+)*/,
    hexdigits: /[[0-9a-fA-F]+(_+[0-9a-fA-F]+)*/,

    regexpctl: /[(){}\[\]\$\^|\-*+?\.]/,
    regexpesc: /\\(?:[bBdDfnrstvwWn0\\\/]|@regexpctl|c[A-Z]|x[0-9a-fA-F]{2}|u[0-9a-fA-F]{4})/,
    tokenizer: {
        root: [
            [/[{}]/, "delimiter.bracket"],
            { include: "common" },
        ],
        common: [
            [
                /#?[a-z_$][\w$]*/,
                {
                    cases: {
                        "@keywords": "keyword",
                        '@defKeywords': 'keyword.defAttr',
                        '@typeKeywords': 'keyword.type',
                        "@default": "identifier",
                    }
                }
            ],
            [/>/, "delimiter.custom"],
            [/[A-Z][\w\$]*/, "type.identifier"],
            [/[<>](?!@symbols)/, "@brackets"],
            [/!(?=([^=]|$))/, "delimiter.custom"],
            [/\/\/.*$/, "comment"],
            [
                /@symbols/,
                {
                    cases: {
                        "@operators": "delimiter",
                        "@default": ""
                    }
                }
            ],
            [/(@digits)[eE]([\-+]?(@digits))?/, "number.float"],
            [/(@digits)\.(@digits)([eE][\-+]?(@digits))?/, "number.float"],
            [/0[xX](@hexdigits)n?/, "number.hex"],
            [/0[oO]?(@octaldigits)n?/, "number.octal"],
            [/0[bB](@binarydigits)n?/, "number.binary"],
            [/(@digits)n?/, "number"],
            // strings
            [/"([^"\\]|\\.)*$/, "string.invalid"],
            // non-teminated string
            [/'([^'\\]|\\.)*$/, "string.invalid"],
            // non-teminated string
            [/"/, "string", "@string_double"],
            [/'/, "string", "@string_single"],
            [/`/, "string", "@string_backtick"]
        ],
        string_double: [
            [/[^\\"]+/, "string"],
            [/@escapes/, "string.escape"],
            [/\\./, "string.escape.invalid"],
            [/"/, "string", "@pop"]
        ],
        string_single: [
            [/[^\\']+/, "string"],
            [/@escapes/, "string.escape"],
            [/\\./, "string.escape.invalid"],
            [/'/, "string", "@pop"]
        ],
        string_backtick: [
            [/\$\{/, { token: "delimiter.bracket", next: "@bracketCounting" }],
            [/[^\\`$]+/, "string"],
            [/@escapes/, "string.escape"],
            [/\\./, "string.escape.invalid"],
            [/`/, "string", "@pop"]
        ],
        bracketCounting: [
            [/\{/, "delimiter.bracket", "@bracketCounting"],
            [/\}/, "delimiter.bracket", "@pop"],
            { include: "common" }
        ]
    }
})


monaco.editor.defineTheme('cc', {
    base: "vs-dark",
    inherit: true,
    rules: [
        {
            token: 'keyword',
            foreground: "#569cd6",
        },
        {
            token: 'keyword.type',
            foreground: "#4ec9b0",
        },
        {
            token: 'keyword.defAttr',
            foreground: "#c8c8c8",
        },
        {
            token: 'delimiter.custom',
            foreground: "#da70d6",
        },
        {
            token: 'identifier',
            foreground: "#9cdcfe",
        },
    ],
    colors: {}
})

monaco.languages.registerCompletionItemProvider(coderLanguageName, {
    provideCompletionItems: function (model, position, context, token) {
        // debugger
        // // find out if we are completing a property in the 'dependencies' object.
        var textUntilPosition = model.getValueInRange({
            startLineNumber: position.lineNumber,
            startColumn: 1,
            endLineNumber: position.lineNumber + 1,
            endColumn: 1,
        });
        // console.log(textUntilPosition)
        // console.log(position)
        // console.log(context)
        // console.log(token)
        // debugger
        // var match = textUntilPosition.match(
        //     /"dependencies"\s*:\s*\{\s*("[^"]*"\s*:\s*"[^"]*"\s*,\s*)*([^"]*)?$/
        // );
        // if (!match) {
        //     return { suggestions: [] };
        // }
        var word = model.getWordUntilPosition(position);
        // console.log(`word `, word)
        var range = {
            startLineNumber: position.lineNumber,
            endLineNumber: position.lineNumber,
            startColumn: word.startColumn,
            endColumn: word.endColumn,
        };
        return {
            suggestions: [{
                label: '"lodash"',
                kind: monaco.languages.CompletionItemKind.Function,
                documentation: "The Lodash library exported as Node.js modules.",
                insertText: '"lodash": "*"',
                range: range,
            }]
        };
    },
})

self.MonacoEnvironment = {
    getWorker(_, label) {
        if (label === 'typescript' || label === 'javascript') {
            return new tsWorker()
        }
        return new editorWorker()
    }
}