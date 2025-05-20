interface ScaleDefinition {
    x1: number
    x2: number
    y1: number
    y2: number
}

export interface ImageResourceInstance {
    width: number
    height: number
    imageData: ImageData
    imgDataUrl: string
    pixel?: boolean
    isHover: (x: number, y: number) => boolean
}

export interface ImageResource {
    url: string
    scale?: ScaleDefinition
    getWidth: () => number
    getHeight: () => number
    getBaseImg: () => ImageResourceInstance
    getImg: (w?: number, h?: number) => ImageResourceInstance
    getDataUrl: () => string
    isPixel: () => boolean
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
    export const of = (url: string, pixel: boolean = false, scale?: string) => new ImgDef(url, pixel, scale);

    const scaleImage = (w: number, h: number, baseImg: ImageInstanceData, scale: ScaleDefinition, imgEl: HTMLImageElement) => {
        const holder = CanvasUtils.getCanvas()
        try {
            holder.canvas.height = h
            holder.canvas.width = w
            if (holder.ctx && baseImg) {
                if (scale) {
                    let h1 = scale.y1;
                    let h2 = scale.y2 - scale.y1 + 1;
                    let h3 = baseImg.imageData.height - h2 - h1;
                    let w1 = scale.x1;
                    let w2 = scale.x2 - scale.x1 + 1;
                    let w3 = baseImg.imageData.width - w2 - w1;

                    let tw2 = w - w1 - w3; // 目标中间宽度
                    let th2 = h - h1 - h3;
                    let tw3Start = w - w3;
                    let th3Start = h - h3;
                    // a left top  
                    holder.ctx.drawImage(imgEl, 0, 0, w1, h1, 0, 0, w1, h1);
                    // b center top
                    holder.ctx.drawImage(imgEl, w1, 0, w2, h1, w1, 0, tw2, h1);
                    // c top right
                    holder.ctx.drawImage(imgEl, w1 + w2, 0, w3, h1, tw3Start, 0, w3, h1);
                    // d left center 
                    holder.ctx.drawImage(imgEl, 0, h1, w1, h2, 0, h1, w1, th2);
                    // e center
                    holder.ctx.drawImage(imgEl, w1, h1, w2, h2, w1, h1, tw2, th2);
                    // f center right
                    holder.ctx.drawImage(imgEl, w1 + w2, h1, w3, h2, tw3Start, h1, w3, th2);
                    // g
                    holder.ctx.drawImage(imgEl, 0, h1 + h2, w1, h3, 0, th3Start, w1, h3);
                    // h
                    holder.ctx.drawImage(imgEl, w1, h1 + h2, w2, h3, w1, th3Start, tw2, h3);
                    // i
                    holder.ctx.drawImage(imgEl, w1 + w2, h1 + h2, w3, h3, tw3Start, th3Start, w3, h3);
                    // const { x1, x2, y1, y2 } = scale
                    // let h1 = y1
                    // let h2 = y2 - y1 + 1
                    // let h3 = baseImg.imageData.height - h2 - h1
                    // let w1 = x1
                    // let w2 = x2 - x1 + 1
                    // let w3 = baseImg.imageData.width - w2 - w1

                    // let tw2 = w - w1 - w3 // 目标中间宽度
                    // let th2 = h - h1 - h3
                    // let tw3Start = w - w3
                    // let th3Start = h - h3
                    // // a left top  
                    // holder.ctx.drawImage(imgEl, 0, 0, w1, h1, 0, 0, w1, h1)
                    // // b center top
                    // holder.ctx.drawImage(imgEl, w1, 0, w2, h1, w1, 0, tw2, h1)
                    // // c top right
                    // holder.ctx.drawImage(imgEl, w1 + w2, 0, w3, h1, tw3Start, 0, w3, h1)
                    // // d left center 
                    // holder.ctx.drawImage(imgEl, 0, h1, w1, h2, 0, h1, w1, th2)
                    // // e center
                    // holder.ctx.drawImage(imgEl, w1, h1, w2, h2, w1, h1, tw2, th2)
                    // // f center right
                    // holder.ctx.drawImage(imgEl, w1 + w2, h1, w3, h2, tw3Start, h1, w3, th2)
                    // // g
                    // holder.ctx.drawImage(imgEl, 0, h1 + h2, w1, h3, 0, th3Start, w1, h3)
                    // // h
                    // holder.ctx.drawImage(imgEl, w1, h1 + h2, w2, h3, w1, th3Start, tw2, h3)
                    // // i
                    // holder.ctx.drawImage(imgEl, w1 + w2, h1 + h2, w3, h3, tw3Start, th3Start, w3, h3)
                } else {
                    holder.ctx.putImageData(baseImg.imageData, 0, 0)
                }
                return new ImageInstanceData(holder.ctx.getImageData(0, 0, holder.canvas.width, holder.canvas.height), holder.toDataURL(), w, h)
            }
            return baseImg
        } finally {
            holder.locked = false
        }
    }

    class ImageInstanceData implements ImageResourceInstance {
        width: number
        height: number
        imageData: ImageData
        imgDataUrl: string
        pixel: boolean = false
        constructor(imageData: ImageData, imgDataUrl: string, width: number, height: number) {
            this.imageData = imageData
            this.imgDataUrl = imgDataUrl
            this.width = width
            this.height = height
        }

        isHover(x: number, y: number): boolean {
            if (!this.imageData) {
                return true
            }
            x = Math.floor(x)
            y = Math.floor(y)
            if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
                return false
            }

            let index = (y * this.width + x) * 4
            return this.imageData.data[index + 3] > 0
        }

        toString() {
            const t = {
                data: Array.from(this.imageData.data),
                width: this.width,
                height: this.height,
                url: this.imgDataUrl
            }
            return JSON.stringify(t)
        }

        static form(json: string) {
            const obj = JSON.parse(json)
            const imageData = new ImageData(new Uint8ClampedArray(obj.data), obj.width, obj.height)
            return new ImageInstanceData(imageData, obj.url, obj.width, obj.height)
        }

        getDataUrl() {
            return this.imgDataUrl
        }
    }

    export class ImgDef implements ImageResource {
        imageDatas = new Map<string, ImageResourceInstance>()
        url: string
        pixel: boolean
        scale?: ScaleDefinition
        height: number = 0
        width: number = 0
        private baseImage?: ImageInstanceData
        imgEl?: HTMLImageElement

        constructor(url: string, pixel: boolean = false, scale?: string) {
            this.url = url
            this.pixel = pixel
            if (scale) {
                this.scale = parseScale(scale)
            }
        }

        getImg(w?: number, h?: number) {
            w = w ? w : this.width
            h = h ? h : this.height
            const key = getKey(w, h)
            let instance = this.imageDatas.get(key)
            if (instance) {
                return instance
            }
            if (this.baseImage && this.scale) {
                const image = scaleImage(w, h, this.baseImage, this.scale, this.imgEl!)
                image.pixel = this.pixel
                this.imageDatas.set(key, image)
                return image
            }
            if (this.baseImage) {
                return this.baseImage
            }
            throw Error('img' + this.url + ' err!')
        }

        getWidth() { return this.width }
        getHeight() { return this.height }
        getBaseImg() { return this.baseImage! }
        getDataUrl() { return this.baseImage ? this.baseImage.imgDataUrl : this.url }
        isPixel() { return this.pixel }
        setBaseImg(baseImg: ImageInstanceData) {
            this.baseImage = baseImg
            this.baseImage.pixel = this.pixel
            this.imageDatas.set(getKey(baseImg.width, baseImg.height), baseImg)
        }
        setImageElement(imgEl: HTMLImageElement) {
            this.imgEl = imgEl
        }

        isHover(x: number, y: number): boolean {
            return this.baseImage!.isHover(x, y)
        }
    }

    export async function loadImages(defs: ImgDef[], callback: (imageData: ImgDef, err?: any) => void) {
        await db.get('-1')

        for (const def of defs) {
            if (def.getBaseImg()) {
                callback(def)
                continue
            }
            const dataCache = await db.get(def.url)
            const img = new Image()
            img.src = def.url
            img.crossOrigin = 'Anonymous'
            def.setImageElement(img)

            if (dataCache) {
                try {
                    const base = dataCache.instData
                    // console.log('load from cache', dataCache)
                    def.width = base.width
                    def.height = base.height
                    const instData = new ImageInstanceData(base.imageData, base.imgDataUrl, base.width, base.height)
                    instData.pixel = base.pixel
                    def.setBaseImg(instData)
                    callback(def)
                    continue
                } catch (err) {
                    console.error(err)
                }
            }
            // 
            img.onerror = (err) => callback(def, err)
            img.onload = async () => {
                const hold = CanvasUtils.getCanvas()
                try {
                    def.width = img.width
                    def.height = img.height
                    hold.canvas.height = img.height
                    hold.canvas.width = img.width
                    hold.ctx.drawImage(img, 0, 0)
                    const imageData = hold.ctx.getImageData(0, 0, img.width, img.height)
                    const instData = new ImageInstanceData(imageData, hold.toDataURL(), img.width, img.height)
                    instData.pixel = def.pixel
                    def.setBaseImg(instData)
                    db.add({ key: def.url, instData: instData })
                } finally {
                    hold.locked = false
                }
                callback(def)
            }
        }

    }
}