<script setup lang="ts">
import { wid, wid2cid, wid2xy, wid2xyStr } from '@/app/constant';
import { Img } from '@/app/img';
import type { MemMapTile } from '@/app/modelData';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, provide, reactive, ref, shallowRef, watch } from 'vue';
import WorldMapCtrl from './WorldMapCtrl.vue';

const tileWidth = 108
const tileHeight = 54
const bgRes = [
    Img.of(`/rsm/images/playing/world/city.png`, true),// 0:城池
    Img.of(`/rsm/images/playing/world/land.png`, true),// 1:城池
    Img.of(`/rsm/images/playing/world/desert.png`, true), //2:沙漠
    Img.of(`/rsm/images/playing/world/forest.png`, true), // 3:森林
    Img.of(`/rsm/images/playing/world/grass.png`, true), //4:草地
    Img.of(`/rsm/images/playing/world/hill.png`, true),//;5:高山
    Img.of(`/rsm/images/playing/world/lake.png`, true),//6:湖泊
    Img.of(`/rsm/images/playing/world/swamp.png`, true),//7:沼泽
]


const { sg } = inject('sg') as { sg: SanGuo }
const canvas = shallowRef<MapCanvas>()

const x = ref(15)
const y = ref(15)
provide('worldMap', { x, y, moveTo })

abstract class DataLoading {
    xCount!: number
    yCount!: number
    tiles: MapTile[] = []

    async load(x: number, y: number) {
        const tileDatas = await sg.api.envApi.getMapTiles({ x, y, xw: this.xCount, yw: this.yCount })
        for (let i = 0; i < this.tiles.length; i++) {
            const tile = this.tiles[i];
            tile.setData(tileDatas[i])
        }
    }

}

class MapTile {
    x: number
    y: number
    data?: MemMapTile
    constructor(x: number = 0, y: number = 0) {
        this.x = x
        this.y = y
    }

    isHover(x: number, y: number) {
        if (x >= this.x && y >= this.y + 21 && x < this.x + 108 && y < this.y + 75) {
            return bgRes[1].isHover(x - this.x, y - this.y)
        }
        return false
    }

    setData(data: MemMapTile) {
        this.data = data
    }

    draw(ctx: CanvasRenderingContext2D) {
        if (this.data && this.data.id > 0) {
            // ctx.drawImage(bgRes[this.data.tileType].imgEl!, this.x, this.y)
            ctx.drawImage(this.getImg().imgEl!, this.x, this.y)
            // const { x, y } = wid2xy(this.data.id)
            // ctx.fillText(`${x},${y}`, this.x + 30, this.y + 50)
        }
    }

    getImg() {
        if (this.data) {
            if (this.data.tileType > 0) {
                return bgRes[this.data!.tileType]!
            } else {
                return bgRes[0]!
            }
        }
        return bgRes[1]!
    }
}


class TileCanvas extends DataLoading {
    x: number = 0
    y: number = 0
    width: number = 0
    height: number = 0
    canvas: HTMLCanvasElement
    ctx: CanvasRenderingContext2D
    constructor(xCount: number, yCount: number) {
        super()
        const widthSize = 1 + xCount * 2;
        const heightSize = 1 + yCount * 2;
        this.xCount = xCount
        this.yCount = yCount
        this.width = widthSize * tileWidth
        this.height = (yCount + 1) * tileHeight  // 21 空白补偿
        const zeroB = yCount % 2 == 0
        for (let y = 0; y < heightSize; y++) {
            const startY = y * 27 - 21;
            let startX = y % 2 == 0 ? zeroB ? 0 : -54 : zeroB ? -54 : 0
            for (let x = 0; x < widthSize; x++) {
                this.tiles.push(new MapTile(startX + x * 108, startY))
            }
        }

        this.canvas = document.createElement('canvas')
        this.canvas.width = this.width
        this.canvas.height = this.height
        this.ctx = this.canvas.getContext('2d')!
    }

    draw(pCtx: CanvasRenderingContext2D, refresh = true) {
        if (refresh) {
            this.ctx.fillStyle = '#000'
            this.ctx.fillRect(0, 0, this.width, this.height)

            for (let i = 0; i < this.tiles.length; i++) {
                this.tiles[i].draw(this.ctx)
            }
        }
        pCtx.drawImage(this.canvas, this.x, this.y)
    }

    hover(x: number, y: number) {
        x = x - this.x
        y = y - this.y
        for (let i = 0; i < this.tiles.length; i++) {
            const tile = this.tiles[i];
            if (tile.isHover(x, y)) {
                return tile;
            }
        }
        return undefined
    }
}

class MapCanvas {
    canvas: HTMLCanvasElement
    ctx: CanvasRenderingContext2D
    tileCanvas: TileCanvas
    size: { width: number, height: number }
    center: { x: number, y: number }
    centerTile?: MapTile
    centerTileDefaultPosition = { x: 0, y: 0 }
    constructor(canvas: HTMLCanvasElement, tileCanvas: TileCanvas, size: { width: number, height: number } = { width: 731, height: 550 }, center?: { x: number, y: number }
    ) {
        this.canvas = canvas
        this.canvas.width = size.width
        this.canvas.height = size.height
        this.tileCanvas = tileCanvas
        this.size = size
        this.ctx = canvas.getContext('2d')!
        if (center) {
            this.center = center
        } else {
            this.center = {
                x: Math.floor(this.size.width / 2),
                y: Math.floor(this.size.height / 2)
            }
        }
    }

    draw() {
        this.drawBg()
        // 
        this.tileCanvas.draw(this.ctx)
        this.drawAfter()
    }

    private drawBg() {
        // this.ctx.fillStyle = '#FFF';
        // this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height)
    }

    private drawAfter() {
        // 十字线与中心点
        // this.ctx.fillStyle = '#F00';
        // this.ctx.fillRect(this.center.x - 1, this.center.y - 1, 3, 3)
        // this.ctx.fillRect(this.center.x, 0, 1, 731)
        // this.ctx.fillRect(0, this.center.y, 731, 1)
        // this.ctx.lineWidth = 1
        // this.ctx.rect(this.centerTileDefaultPosition.x, this.centerTileDefaultPosition.y, 108, 54)
    }

    move(mx: number, my: number) {
        if (mx == 0 && my == 0) { return }
        this.drawBg()

        this.tileCanvas.x += mx
        this.tileCanvas.y += my
        this.tileCanvas.draw(this.ctx, false)

        this.drawAfter()
    }

    getClickTile(x: number, y: number) {
        return this.tileCanvas.hover(x, y)
    }

    async moveTo(x: number, y: number, offsetX = 0, offsetY = 0) {
        x = x < 1 ? 1 : x > 500 ? 500 : x
        y = y < 1 ? 1 : y > 500 ? 500 : y
        await this.tileCanvas.load(x, y)
        this.tileCanvasPositionToCenter(offsetX, offsetY)
        this.centerTile = this.tileCanvas.hover(this.center.x, this.center.y)
        this.draw()
    }

    async refresh() {
        const newCenter = this.tileCanvas.hover(this.center.x, this.center.y)
        if (newCenter && newCenter != this.centerTile) {
            // console.log('画布起始位置', this.center.x - Math.floor(this.tileCanvas.width / 2), this.center.y - Math.floor(this.tileCanvas.height / 2))
            // console.log('画布当前位置', this.tileCanvas.x, this.tileCanvas.y)
            // console.log('中心块: ', this.centerTileDefaultPosition.x, this.centerTileDefaultPosition.y, 108, 54)
            // console.log('newCenter ', wid2xyStr(newCenter.data!.id), newCenter)
            // console.log('oldCenter ', wid2xyStr(this.centerTile!.data!.id), this.centerTile)
            // console.log('newCenter xy', this.tileCanvas.x + newCenter.x, this.tileCanvas.y + newCenter.y)
            const offsetX = this.centerTileDefaultPosition.x - (this.tileCanvas.x + newCenter.x)
            const offsetY = this.centerTileDefaultPosition.y - (this.tileCanvas.y + newCenter.y)
            // console.log('off', offsetX, offsetY)
            const { x, y } = wid2xy(newCenter.data!.id)
            await this.moveTo(x, y, offsetX, offsetY)
        }
    }

    private tileCanvasPositionToCenter(offsetX = 0, offsetY = 0) {
        this.tileCanvas.x = this.center.x - Math.floor(this.tileCanvas.width / 2)
        this.tileCanvas.y = this.center.y - Math.floor(this.tileCanvas.height / 2)
        const tile = this.tileCanvas.tiles[Math.floor(this.tileCanvas.tiles.length / 2)]
        this.centerTileDefaultPosition = { x: this.tileCanvas.x + tile.x, y: this.tileCanvas.y + tile.y }
        this.tileCanvas.x -= offsetX
        this.tileCanvas.y -= offsetY

        const newCenter = this.tileCanvas.hover(this.center.x, this.center.y)!
        offsetX = this.centerTileDefaultPosition.x - (this.tileCanvas.x + newCenter.x)
        offsetY = this.centerTileDefaultPosition.y - (this.tileCanvas.y + newCenter.y)
        console.log('off', offsetX, offsetY)
    }



}

onMounted(async () => {
    await Img.loadImages(bgRes, () => { })
    const tileCanvas = new TileCanvas(10, 30)
    canvas.value = new MapCanvas(document.getElementById('map')! as HTMLCanvasElement, tileCanvas)
    await canvas.value.moveTo(x.value, y.value)
})

const press = ref(false)
const pressXY = ref({ x: 0, y: 0 })

async function moveTo(mapX: number, mapY: number) {
    if (canvas.value) {
        await canvas.value?.moveTo(mapX, mapY)
        const xy = wid2xy(canvas.value.centerTile!.data!.id)
        x.value = xy.x
        y.value = xy.y
    }
}

function doClick(e: MouseEvent) {
    // console.log(bgRes[1].imgEl!.height)
    console.log('click  ele: ', canvas.value!.getClickTile(e.offsetX, e.offsetY))
    // const cv = canvas.value!
    // console.log('center ele : ', canvas.value!.getClickTile(cv.center.x, cv.center.y))
    // canvas.value!.draw()
}

function doMouseDown(e: MouseEvent) {
    press.value = true
    pressXY.value = { x: e.offsetX, y: e.offsetY }
}

function doMouseUp(e: MouseEvent) {
    let pr = press.value
    press.value = false
    if (pr) {
        canvas.value!.refresh()
    }
}

function doMouseMove(e: MouseEvent) {
    if (press.value) {
        console.log(`x: ${e.offsetX - pressXY.value.x} , y: ${e.offsetY - pressXY.value.y}`)
        const mx = e.offsetX - pressXY.value.x
        const my = e.offsetY - pressXY.value.y
        canvas.value!.move(mx, my)
        pressXY.value = { x: e.offsetX, y: e.offsetY }
    }
}

onUnmounted(async () => {
})


</script>
<template>
    <canvas id="map" class="sg-playing" @click="doClick" @mousemove="doMouseMove" @mousedown="doMouseDown"
        @mouseleave="doMouseUp" @mouseup="doMouseUp">
    </canvas>
    <WorldMapCtrl />
</template>
<style lang="less" scoped>
.sg-playing {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;
    left: 120px;
    top: 20px;
}
</style>