
export class TableHeaderDef {
    realWidth: number = 0
    constructor(public key: string, public width: number = 0, public content: string = '', public styles: Record<string, any> = {}) { }
}

export class TableDataRowDef {
    constructor(
        public datas: TableDataCellDef[] = [],
        public source: any = {},
        public empty: boolean = false,
    ) { }
}

export class TableDataCellDef {
    constructor(
        public content: string = '',
        public styles: Record<string, any> = {}
    ) { }
}



export const defaultConverHeader = (headers: any[]) => {
    return headers.map((h: any) => {
        const styles: Record<string, any> = h.styles || {}
        h.width && (styles.width = `${h.width}px`)
        h.color && (styles.color = h.color)
        styles.color = h.color || 'var(--rxsg-table-header-font-color)'
        return new TableHeaderDef(h.key, h.width || 0, h.content || '', styles)
    })
}

export const defaultConverDatas = (headers: TableHeaderDef[], datas: any[]) => {
    const rowDatas: TableDataRowDef[] = []
    datas.forEach((data: any, idx: number) => {
        console.log(data)
        const cells: TableDataCellDef[] = []
        headers.forEach(h => {
            const v = data[h.key]
            const styles = {
                textAlign: 'center'
            } as any
            let content = v
            if (typeof v !== 'string') {
                // todo
            }
            cells.push(new TableDataCellDef(content || '', styles))
        })
        rowDatas.push(new TableDataRowDef(cells, data))
    });

    return rowDatas
}