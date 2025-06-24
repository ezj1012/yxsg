
export class TableHeaderDef {
    realWidth: number = 0
    constructor(public width: number = 0, public content: string = '', public styles: Record<string, any> = {}) { }
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
        return new TableHeaderDef(h.width || 0, h.content || '', styles)
    })
}

export const defaultConverDatas = (datas: any[]) => {
    const rowDatas: TableDataRowDef[] = []
    datas.forEach((data: any, idx: number) => {
        const cells: TableDataCellDef[] = []
        for (const d of data) {
            const styles = {
                textAlign: 'center'
            } as any
            cells.push(new TableDataCellDef(d.content || '', styles))
        }
        rowDatas.push(new TableDataRowDef(cells, data))
    });

    return rowDatas
}