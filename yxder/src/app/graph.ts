import EntityShape from '@/components/entityGraph/model/EntityShape.vue';
import { CellView, Edge, Graph, Node, Options, Shape } from '@antv/x6';
import { Snapline } from '@antv/x6-plugin-snapline';
import { register } from '@antv/x6-vue-shape';
import { EntityModel, GraphModel, toGraphModel, type EntityCfg, type GraphNodeCfg, type YXCoder } from './yxcoder';
import { circle } from '@antv/x6/lib/registry/marker/circle';
import { createApp, h } from 'vue';
import type { PortManager } from '@antv/x6/lib/model/port';
const entityShape = "entityShape"
register({
    shape: entityShape,
    width: 200, // 默认宽度
    height: 102, // 默认高度
    component: EntityShape,
    ports: {
        groups: {
            idOut: {
                position: 'entityShapeIdPosition',
                markup: [
                    {
                        tagName: 'rect',
                        selector: 'rect',
                        style: {
                            refWidth: '100%',
                        }
                    }
                ],
                attrs: {
                    rect: {
                        // magnet: true,
                        fill: 'rgba(64,64,64,0.1)',
                        refWidth: '100%',
                        height: 22,
                        x: 0,
                        y: 0,
                    }
                },
            } as PortManager.GroupMetadata,
            foreignKey: {
                position: 'entityShapeForeignPosition',
                markup: [
                    {
                        tagName: 'rect',
                        selector: 'rect',
                    }
                ],
                attrs: {
                    rect: {
                        // magnet: true,
                        fill: 'rgba(96,96,96,0.3)',
                        refWidth: '100%',
                        height: 22,
                        x: 0,
                        y: 0,
                    }
                },
            }
        },
    },
})

Graph.registerPortLayout('entityShapeIdPosition', (portsPositionArgs: any) => {
    return portsPositionArgs.map(() => { return { position: { x: 1, y: 30 }, angle: 0, } })
})

Graph.registerPortLayout('entityShapeForeignPosition', (portsPositionArgs: any, elemBBox, groupPositionArgs) => {
    return portsPositionArgs.map((args: { idx: number, redefId: boolean }) => {
        return { position: { x: 1, y: 30 + (args.redefId ? 0 : 22) + args.idx * 22 }, angle: 0, }
    })
})


function configGraph(containerId: string) {
    const mainEl = document.getElementById(containerId)!
    return {
        el: mainEl as HTMLDivElement,
        graph: new Graph({
            container: mainEl,
            width: mainEl.offsetWidth,
            height: mainEl.offsetHeight,
            autoResize: true,
            background: {
                color: '#F2F7FA',
            },
            grid: {
                visible: true,
                type: 'doubleMesh',
                args: [
                    {
                        color: '#ddd', // 主网格线颜色
                        thickness: 1, // 主网格线宽度
                    },
                    {
                        color: '#ccc', // 次网格线颜色
                        thickness: 1, // 次网格线宽度
                        factor: 5, // 主次网格线间隔
                    },
                ],
            },
            panning: true,//平移
            mousewheel: {// 缩放
                enabled: true,
                zoomAtMousePosition: true,
                modifiers: 'ctrl',
                minScale: 0.5
            },
            translating: {
                restrict: false
            },
            interacting(this: Graph, cellView: CellView) {
                // console.log(cellView.cell.getData())
                if (cellView.cell.getData()?.disableMove) {
                    return { nodeMovable: false };
                }
                return true;
            },
            onPortRendered(args: Options.OnPortRenderedArgs) {
                // const selectors = args.contentSelectors
                // if (selectors) {
                //     selectors.width = args.node.getSize().width
                // }
                // 
                // const selectors = args.contentSelectors
                // console.log(JSON.stringify(selectors), selectors.contentContainer)
                // if (selectors && selectors.contentContainer) {
                //     const nodeWidth = args.node.getSize().width
                //     selectors.contentContainer.setAttribute('width', `${nodeWidth}`)
                // }
                // const selector = args.contentSelectors[0]


                // if (container) {

                // }
            },
            connecting: {
                router: {
                    name: 'er',
                    args: {
                        // offset: 40,
                        offset: 'center',
                        direction: 'H',
                    },

                    // // 只能路由
                    // name: 'orth',
                    // args: {
                    //     startDirections: ['left', 'right'], // 仅允许水平方向
                    //     endDirections: ['right']
                    // }
                },
                connector: {
                    name: 'rounded',
                    args: {
                        radius: 20,
                    },
                },
                createEdge() {
                    return new Shape.Edge({
                        attrs: {
                            line: {
                                stroke: '#30303088',
                                strokeWidth: 2,
                            },
                        },
                    })
                },
            },
        })
    }
}

export function nodeToGraphNodeCfg(node: Node) {
    return {
        id: node.id,
        x: node.getPosition().x,
        y: node.getPosition().y,
        width: node.getSize().width,
        zIndex: node.zIndex || 0,
    }
}

export class Rlt {
    // sourceInfo?: string // sysUserId> SysUser
    sourceNodeId!: string
    sourcePortId!: string // ${modelData.id}_${idx}
    idx!: number
    targetName!: string
    id?: string // 渲染后才会产生
}

export function toForeignKeyPortCfg(node: Node, rlt: Rlt, redefId: boolean = false) {
    return {
        group: 'foreignKey',
        id: rlt.sourcePortId,
        args: {
            idx: rlt.idx,
            redefId,
        }, size: {
            width: node.getSize().width,
            height: node.getSize().height,
        }
    }
}

export function attrToRlt(sourceNodeId: string, attr: string, idx: number) {
    const st = attr.indexOf('>')
    if (st > 0) {
        let targetName = ''
        let t = attr.indexOf("//")
        if (t > st) {
            targetName = attr.substring(st + 1, t).trim()
        } else {
            targetName = attr.substring(st + 1).trim()
        }
        const sourcePortId = `${sourceNodeId}_${idx}`
        return {
            sourceNodeId,
            sourcePortId,
            targetName,
            idx,
        }
    }
    return undefined
}


export class CoderGraph {
    graph: Graph
    el: HTMLDivElement
    nodeMovedListener?: (node: GraphNodeCfg) => any
    findNodeIdByClassName?: (className: string) => string | undefined
    // nodes: Map<string, Node> = new Map()
    rlts: Rlt[] = []
    constructor(containerId: string = 'graph-container') {
        const graphInfo = configGraph(containerId)
        this.graph = graphInfo.graph
        this.el = graphInfo.el
        this.graph.use(new Snapline({ enabled: true }))
        this.graph.on('node:moved', async ({ e, x, y, node, view }) => this.nodeMovedListener && await this.nodeMovedListener(nodeToGraphNodeCfg(node)))
    }

    async createNode(model: EntityModel) {
        const pointer = this.graph.clientToLocal(this.el.offsetWidth / 2, 100);
        return this.addModel({
            x: pointer.x - 140,
            y: pointer.y,
            width: 200,
            model: model
        })
    }

    async addModel(modelData: GraphModel) {
        const ports = [
            { id: `${modelData.id}_id`, group: 'idOut', },
        ] as any[]

        if (modelData.model.attrs) {
            let redefId = false
            modelData.model.attrs.forEach((attr, idx) => {
                if (attr.trim().startsWith('id>')) {
                    redefId = true
                }

                const rlt = attrToRlt(modelData.id!, attr, idx)
                if (rlt) {
                    this.rlts.push(rlt)
                    ports.push({
                        group: 'foreignKey',
                        id: rlt.sourcePortId,
                        args: { idx, redefId }
                    })
                }
            })
        }

        const node = this.graph.addNode({
            id: modelData.id,
            shape: entityShape,
            x: modelData.x,
            y: modelData.y,
            zIndex: modelData.zIndex,
            size: {
                width: modelData.width || 200,
                height: 102,
            },
            data: {},
            ports,
        });
        // this.nodes.set(modelData.model.name || '', node)
        return node;
    }

    /**
     * 更新关系
     */
    updateRlt() {
        this.graph.getEdges().forEach(edge => this.graph.removeEdge(edge))

        this.rlts.forEach(rlt => {
            const targetCell = this.findNodeByClassName(rlt.targetName)
            if (targetCell) {
                const targetPortId = `${targetCell.id}_id`
                this.graph.addEdge({
                    id: `${rlt.sourcePortId}&${targetPortId}`,
                    source: {
                        cell: this.graph.getCellById(rlt.sourceNodeId),
                        port: rlt.sourcePortId
                    },
                    target: {
                        cell: targetCell,
                        port: targetPortId,
                    }
                })
            }
        })
    }

    private findNodeByClassName(className: string) {
        if (this.findNodeIdByClassName) {
            const nodeId = this.findNodeIdByClassName(className)
            if (nodeId) {
                return this.graph.getCellById(nodeId)
            }
        }
        return undefined
    }

    updateNodeRlt(node: Node, rlts: Rlt[], redefId: boolean = false) {
        // throw new Error('Method not implemented.');

        // 更新节点,
        let updates = [] as any
        const exPorts = node.getPortsByGroup('foreignKey').reduce((exPorts, port) => { exPorts.set(port.args!.idx!, port); return exPorts; }, new Map<any, PortManager.PortMetadata>())
        rlts.forEach(rlt => {
            const port = exPorts.get(rlt.idx)
            if (port) {
                exPorts.delete(rlt.idx)
            } else {
                updates.push(toForeignKeyPortCfg(node, rlt, redefId))
            }
        })
        for (const port of exPorts.values()) {
            node.removePort(port)
        }
        if (updates.length > 0) {
            node.addPorts(updates)
            updates = []
        }

        // 更新边
        const exEdges = this.graph.getEdges().filter(edge => edge.id.startsWith(node.id)).reduce((ex, edge) => { ex.set(edge.id, edge); return ex; }, new Map<any, Edge>())
        rlts.forEach(rlt => {
            const targetCell = this.findNodeByClassName(rlt.targetName)
            if (targetCell) {
                const targetPortId = `${targetCell.id}_id`
                const id = `${rlt.sourcePortId}&${targetPortId}`
                if (exEdges.has(id)) {
                    exEdges.delete(id)
                } else {
                    updates.push({
                        id,
                        source: {
                            cell: node,
                            port: rlt.sourcePortId
                        },
                        target: {
                            cell: targetCell,
                            port: targetPortId,
                        }
                    })
                }
            }
        })
        for (const edge of exEdges.values()) {
            this.graph.removeEdge(edge.id)
        }
        if (updates.length > 0) {
            this.graph.addEdges(updates)
        }
    }

    setCenter(nodeId?: string) {
        if (nodeId) {
            const cell = this.graph.getCellById(nodeId)
            this.graph.centerCell(cell)
        } else {
            this.graph.centerContent();
        }
    }



    dispose() {
        // this.graph.clearCells()
        this.rlts = []
        // this.nodes.clear()
    }

}


