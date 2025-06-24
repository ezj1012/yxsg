import type { Shapable } from "../commModel"

interface ScaleDefinition {
    x1: number
    x2: number
    y1: number
    y2: number
}

interface ImageDatabaseData {
    key: string
    instData: any
}

class ImageDatabase {
    static KEY = 'img_cache'
    db?: IDBDatabase
    private dbReady: Promise<IDBDatabase>

    constructor() {
        const request = indexedDB.open(ImageDatabase.KEY, 1)
        this.dbReady = new Promise((resolve, reject) => {
            request.onsuccess = () => {
                this.db = request.result
                resolve(this.db)
            }

            request.onupgradeneeded = () => {
                this.db = request.result
                if (!this.db.objectStoreNames.contains(ImageDatabase.KEY)) {
                    this.db.createObjectStore(ImageDatabase.KEY, { keyPath: 'key' })
                }
            }

            request.onerror = () => reject(request.error)
        })
    }

    async add(value: any) {
        const db = await this.dbReady
        const transaction = db.transaction(ImageDatabase.KEY, 'readwrite')
        const objectStore = transaction.objectStore(ImageDatabase.KEY)
        return new Promise<void>((resolve, reject) => {
            const request = objectStore.put(value)
            request.onsuccess = () => resolve()
            request.onerror = () => reject(request.error)
        })
    }

    async get(key: string): Promise<ImageDatabaseData | undefined> {
        const db = await this.dbReady
        const transaction = db.transaction(ImageDatabase.KEY, 'readonly')
        const objectStore = transaction.objectStore(ImageDatabase.KEY)
        return new Promise<any>((resolve, reject) => {
            const request = objectStore.get(key)
            request.onsuccess = () => resolve(request.result)
            request.onerror = () => reject(request.error)
        })
    }
}
namespace CanvasUtils {
    class CanvasHolder {
        id: number
        canvas: HTMLCanvasElement
        ctx: CanvasRenderingContext2D
        locked: boolean = false

        constructor(id: number) {
            this.id = id
            this.canvas = document.createElement('canvas')
            this.ctx = this.canvas.getContext('2d')!
        }

        toDataURL() {
            return this.canvas.toDataURL("image/png", 1)
        }

    }

    class CanvasPool {
        holders: CanvasHolder[] = []

        constructor() {
            for (let i = 0; i < 5; i++) {
                this.holders.push(new CanvasHolder(i))
            }
        }

        getCanvas(): CanvasHolder {
            let holder
            const findAvailableCanvas = () => {
                for (const h of this.holders) {
                    if (!h.locked) {
                        h.locked = true
                        h.locked
                        holder = h
                        return
                    }
                }
                setTimeout(findAvailableCanvas, 100)
            }
            findAvailableCanvas()
            return holder!
        }

    }
    const pool = new CanvasPool()

    export function getCanvas() {
        return pool.getCanvas()
    }
}
export namespace Img {
    const db = new ImageDatabase()
    const getKey = (w: number, h: number) => `${w}_${h}`
    const parseScale = (scale: string) => {
        const [x1, y1, x2, y2] = scale.split(',').map(Number)
        return { x1, y1, x2, y2 } as ScaleDefinition
    }

    export function of(url: string, pixel: boolean, scale: string | undefined): ImgDef {
        return new ImgDef(url, pixel, scale);
    }
    export class ImgDef {

        imgEl?: HTMLImageElement
        width: number = 0
        height: number = 0
        imageData?: ImageData
        scale?: ScaleDefinition
        constructor(public url: string, public pixel: boolean, _scale: string | undefined) {
            this.scale = _scale ? parseScale(_scale) : undefined
        }

        isHover(x: number, y: number): boolean {
            x = Math.floor(x)
            y = Math.floor(y)

            if (x < 0 || y < 0 || x >= this.width || y >= this.height) { return false }
            if (this.imageData) {
                let index = (y * this.width + x) * 4
                return this.imageData.data[index + 3] > 0
            }
            return true;

        }
    }

    export async function loadImages(defs: ImgDef[], callback: (imageData: ImgDef, err?: any) => void) {
        await db.get('-1')

        for (const def of defs) {
            const dataCache = await db.get(def.url)
            const img = new Image()
            img.src = def.url
            img.crossOrigin = 'Anonymous'
            def.imgEl = img

            if (dataCache && dataCache.instData) {
                def.width = dataCache.instData.width
                def.height = dataCache.instData.height
                def.imageData = dataCache.instData.imageData as ImageData
            }

            img.onerror = (err) => callback(def, err)
            img.onload = async () => {

                def.width = img.width
                def.height = img.height
                if (def.pixel) {
                    const hold = CanvasUtils.getCanvas()
                    try {
                        hold.canvas.height = img.height
                        hold.canvas.width = img.width
                        hold.ctx.drawImage(img, 0, 0)
                        const imageData = hold.ctx.getImageData(0, 0, img.width, img.height)
                        def.imageData = imageData
                        db.add({ key: def.url, instData: { width: img.width, height: img.height, imageData } })
                    } finally {
                        hold.locked = false
                    }
                }
                callback(def)
            }
        }
    }
}

const ImgDef = Img.ImgDef
export { ImgDef }

export interface ImgDefCache {
    get(key: string): Img.ImgDef | undefined
}

export interface ImgGroupCache {
    get(key: string): ImgGroupInfo | undefined
}

export class ImgGroupInfo {
    constructor(private key: string, private imgCache: ImgDefCache) { }
    getKey() {
        if (this.hasDef()) {
            return this.key
        } else if (this.hasOn()) {
            return this.key + "_on"
        } else if (this.hasDown()) {
            return this.key + "_down"
        } else if (this.hasAlarm()) {
            return this.key + "_alarm"
        } else if (this.hasSct()) {
            return this.key + "_sct"
        } else if (this.hasSctOn()) {
            return this.key + "_son"
        } else if (this.hasDis()) {
            return this.key + "_dis"
        }
        return ''
    }
    hasDef() { return this.imgCache.get(this.key) }
    hasOn() { return this.imgCache.get(this.key + "_on") }
    hasSctOn() { return this.imgCache.get(this.key + "_son") }
    hasDown() { return this.imgCache.get(this.key + "_down") }
    hasAlarm() { return this.imgCache.get(this.key + "_alarm") }
    hasSct() { return this.imgCache.get(this.key + "_sct") }
    hasDis() { return this.imgCache.get(this.key + "_dis") }
}
