
export class ImgDef {
    imgEl?: HTMLImageElement
    width: number = 0
    height: number = 0
    constructor(public url: string, public pixel: boolean, public scale: string | undefined) { };

    static of(url: string, pixel: boolean, scale: string | undefined): ImgDef {
        return new ImgDef(url, pixel, scale);
    }
}

export async function loadImages(defs: ImgDef[], callback: (imageData: ImgDef, err?: any) => void) {
    for (const def of defs) {
        const img = new Image()
        img.src = def.url
        img.crossOrigin = 'Anonymous'
        def.imgEl = img
        img.onerror = (err) => callback(def, err)
        img.onload = async () => {
            try {
                def.width = img.width
                def.height = img.height
                if (def.pixel) {
                    
                }
                // const instData = new ImageInstanceData(imageData, hold.toDataURL(), img.width, img.height)
                // instData.pixel = def.pixel
                // def.setBaseImg(instData)

                // db.add({ key: def.url, instData: instData })
            } finally {
                // hold.locked = false
            }
            callback(def)
        }
    }
}

export interface ImgDefCache {
    get(key: string): ImgDef | undefined
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
