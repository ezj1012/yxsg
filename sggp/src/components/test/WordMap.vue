<script setup lang="ts">
import { Img } from '@/app/img';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, reactive, ref, shallowRef, watch } from 'vue';

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
const ctx = ref()


class MapTile {
    x: number
    y: number
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

    draw(ctx: CanvasRenderingContext2D) {
        ctx.drawImage(bgRes[1].imgEl!, this.x, this.y)
    }
}

class MapCanvas {
    canvas: HTMLCanvasElement
    ctx: CanvasRenderingContext2D
    tiles: MapTile[] = []
    size: { width: number, height: number }
    centre: { x: number, y: number }
    // ctx: H
    constructor(canvas: HTMLCanvasElement, size: { width: number, height: number } = { width: 731, height: 550 }, centre?: { x: number, y: number }
    ) {
        this.canvas = canvas
        this.canvas.width = size.width
        this.canvas.height = size.height
        this.size = size
        this.ctx = canvas.getContext('2d')!
        if (centre) {
            this.centre = centre
        } else {
            this.centre = {
                x: Math.round(this.size.width / 2),
                y: Math.round(this.size.height / 2)
            }
        }
        console.log(this.canvas.width, this.canvas.height)
        this.tiles.push(new MapTile())
        this.tiles.push(new MapTile(108))
        this.tiles.push(new MapTile(216))
        this.tiles.push(new MapTile(54, 27))
        this.tiles.push(new MapTile(162, 27))
        this.tiles.push(new MapTile(270, 27))
    }

    draw() {
        this.ctx.fillStyle = '#FFF';
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height)

        // this.ctx.lineWidth = 1
        // this.ctx.fillStyle = '#FFF';
        // this.ctx.fillRect(0, 0, 108, 54)
        for (let i = 0; i < this.tiles.length; i++) {
            const tile = this.tiles[i];
            tile.draw(this.ctx)
        }

    }

    getClickTile(e: MouseEvent) {
        for (let i = 0; i < this.tiles.length; i++) {
            const tile = this.tiles[i];
            if (tile.isHover(e.offsetX, e.offsetY)) {
                return tile;
            }
        }
        return undefined
    }

    setMap(mapX: number, mapY: number) {
        
    }

}

onMounted(async () => {
    await Img.loadImages(bgRes, () => { })
    canvas.value = new MapCanvas(document.getElementById('map')! as HTMLCanvasElement)
    canvas.value.draw()
})

function doClick(e: MouseEvent) {
    console.log(bgRes[1].imgEl!.height)
    console.log(canvas.value!.getClickTile(e))
    canvas.value!.draw()
}

onUnmounted(async () => {
})
</script>
<template>
    <canvas id="map" class="sg-playing" @click="doClick">
    </canvas>
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