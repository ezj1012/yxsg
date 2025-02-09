import type { Ref } from "vue";
import type { CoderGraph } from "./graph";
import { get, post } from "./httpa";
import { CellView, Graph, Node } from '@antv/x6';

export enum EntityModelType {
    intId = 0,
    longId = 1,
    intIdNoAudit = 2,
    longIdNoAudit = 3,
}

export class EntityModelAttr {
    name?: string
    typeSymbol?: boolean
    type?: string
    defaultValueSymbol?: boolean
    defaultValue?: string // asdsdas
    zhNameSymbol?: boolean
    zhName?: string // asdsdas
}


export class EntityModel {
    extendsType: EntityModelType = EntityModelType.intId
    name?: string
    attrs: string[] = []
}

export class GraphModel implements GraphNodeCfg {
    id?: string
    x!: number
    y!: number
    width: number = 200
    zIndex?: number
    model!: EntityModel
}

export function toGraphModel(data: EntityCfg) {
    //     entity.id = data.id;
    const gm = new GraphModel()
    gm.id = data.id
    gm.x = data.x;
    gm.y = data.y;
    gm.width = data.width
    gm.zIndex = data.zIndex;

    const entity = new EntityModel()
    if (data.modelContent) {
        const mdata = JSON.parse(data.modelContent)
        Object.assign(entity, mdata);
    }

    gm.model = entity

    return gm;
}

export function toCfgModelData(data: GraphModel) {
    const cfg = new EntityCfg()
    cfg.id = data.id
    cfg.x = data.x;
    cfg.y = data.y;
    cfg.width = data.width
    cfg.zIndex = data.zIndex;
    cfg.modelContent = JSON.stringify(data.model)
    return cfg
}

export class YXCoder {

    cfg?: CodeCfg
    models: Ref<GraphModel[]>
    private graph: CoderGraph

    constructor(graph: CoderGraph, models: Ref<GraphModel[]>) {
        this.graph = graph
        this.models = models
        this.graph.nodeMovedListener = async (data: GraphNodeCfg) => { await this.updatePosition(data) }
        this.graph.findNodeIdByClassName = (className: string) => { return this.models.value.find(m => m.model.name == className)?.id }
    }

    async loading() {
        this.cfg = (await get(`/code/get`)).data;
        this.cfg!.entities = this.cfg!.entities || []
        this.models.value = this.cfg!.entities.map(data => toGraphModel(data)) || []
        this.graph.dispose()
        for (let i = 0; i < this.models.value.length; i++) {
            await this.graph.addModel(this.models.value[i])
        }
        this.graph.updateRlt()
    }

    async newEntity(entity: EntityModel = new EntityModel()) {
        const node = await this.graph.createNode(entity)
        const gm = new GraphModel()
        gm.id = node.id
        gm.model = entity
        gm.x = node.getPosition().x
        gm.y = node.getPosition().y
        gm.width = node.getSize().width
        gm.zIndex = node.zIndex || 0
        await this.updateEntity(gm)
    }

    async save() {
        await post(`/code/save`, { entities: this.cfg?.entities, graphs: this.cfg?.graphs });
    }

    async gen(m: EntityModel[] = []) {
        await post(`/code/gen`, m);
    }

    async updatePosition(data: GraphNodeCfg) {
        const nodeIdx = this.models.value.findIndex(n => n.id === data.id)
        if (nodeIdx >= 0) {
            Object.assign(this.models.value[nodeIdx], data)
            Object.assign(this.cfg!.entities[nodeIdx], data)
        }
    }

    async updateEntity(data: GraphModel) {
        const cfg = toCfgModelData(data)
        const oldNodeIdx = this.models.value.findIndex(n => n.id === data.id)
        if (oldNodeIdx < 0) {
            this.models.value.push(data)
            this.cfg!.entities.push(cfg)
        } else {
            this.models.value[oldNodeIdx] = data
            this.cfg!.entities[oldNodeIdx] = cfg
        }
        await this.save()
    }
}



export interface GraphNodeCfg {
    id?: string
    x: number
    y: number
    width: number
    zIndex?: number
}

export class EntityCfg implements GraphNodeCfg {
    id?: string;
    x!: number;
    y!: number;
    width: number = 200;
    zIndex?: number;
    modelContent?: string;
}

export class Project {
    name: string = ''
    desc: string = ''
    javaPackage: string = ''
}

export class CodeCfg {
    project?: Project
    entities: EntityCfg[] = []
    graphs: GraphCfg[] = []
}

export class GraphCfg {
    name!: string
    nodes!: GraphNodeCfg[]
}






