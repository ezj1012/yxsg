import { wid, wid2xy } from "./constant"
import { Img } from "./img"
import type { MemMapTile } from "./modelData"

export const tileWidth = 108
export const tileHeight = 54
export const bgRes = [
  Img.of(`/rsm/images/playing/world/city.png`, true),// 0:城池
  Img.of(`/rsm/images/playing/world/land.png`, true),// 1:城池
  Img.of(`/rsm/images/playing/world/desert.png`, true), //2:沙漠
  Img.of(`/rsm/images/playing/world/forest.png`, true), // 3:森林
  Img.of(`/rsm/images/playing/world/grass.png`, true), //4:草地
  Img.of(`/rsm/images/playing/world/hill.png`, true),//;5:高山
  Img.of(`/rsm/images/playing/world/lake.png`, true),//6:湖泊
  Img.of(`/rsm/images/playing/world/swamp.png`, true),//7:沼泽
]


export interface MapCtx {
  x: number
  y: number
  showLv: boolean
  getMapTiles: (params: { x: number, y: number, xw: number, yw: number }) => Promise<MemMapTile[]>
  updateMapPosition: (x: number, y: number) => any
  updateHoverTile: (tile: MapTile | undefined) => any
  moveTo: (mapX: number, mapY: number) => Promise<void>
}


export class MapTile {
  x: number
  y: number
  data?: MemMapTile
  constructor(x: number = 0, y: number = 0) {
    this.x = x
    this.y = y
  }

  isHover(x: number, y: number) {
    if (x >= this.x && y >= this.y + 21 && x < this.x + tileWidth && y < this.y + 75) {
      return bgRes[1].isHover(x - this.x, y - this.y)
    }
    return false
  }

  setData(data: MemMapTile) { this.data = data }

  private getImg() {
    if (this.data) {
      if (this.data.tileType > 0) {
        return bgRes[this.data!.tileType]!
      } else {
        return bgRes[0]!
      }
    }
    return bgRes[1]!
  }

  draw(ctx: CanvasRenderingContext2D, cfg: MapCtx) {
    if (!this.data || this.data.state == -1) { return } // 非有效数据
    ctx.drawImage(this.getImg().imgEl!, this.x, this.y)
    if (cfg.showLv) { // 绘制等级
      const { x, y } = wid2xy(this.data.id)
      // /
      ctx.fillText(`${x},${y}`, this.x + 30, this.y + 50)
    }
  }
}




class TileCanvas {
  x: number = 0
  y: number = 0
  width: number = 0
  height: number = 0
  canvas: HTMLCanvasElement
  ctx: CanvasRenderingContext2D
  tiles: MapTile[] = []
  centerId: number = -1
  mapCtx: MapCtx
  private xw: number
  private yw: number

  constructor(mapCtx: MapCtx, mapSize: { width: number, height: number }) {
    this.mapCtx = mapCtx
    this.xw = Math.round(mapSize.width * 1.5 / tileWidth)
    this.yw = Math.round(mapSize.height * 3 / tileHeight)


    const widthSize = 1 + this.xw * 2;
    const heightSize = 1 + this.yw * 2;

    this.width = widthSize * tileWidth
    this.height = (this.yw + 1) * tileHeight  // 21 空白补偿
    const zeroB = this.yw % 2 == 0
    for (let y = 0; y < heightSize; y++) {
      const startY = y * 27 - 21;
      let startX = y % 2 == 0 ? zeroB ? 0 : -tileHeight : zeroB ? -tileHeight : 0
      for (let x = 0; x < widthSize; x++) {
        this.tiles.push(new MapTile(startX + x * tileWidth, startY))
      }
    }

    this.canvas = document.createElement('canvas')
    this.canvas.width = this.width
    this.canvas.height = this.height
    this.ctx = this.canvas.getContext('2d')!
  }

  draw() {
    this.ctx.fillStyle = '#000'
    this.ctx.fillRect(0, 0, this.width, this.height)

    for (let i = 0; i < this.tiles.length; i++) {
      this.tiles[i].draw(this.ctx, this.mapCtx)
    }
  }

  isHover(x: number, y: number) {
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

  async updateData(x: number, y: number) {
    this.centerId = wid(x, y)
    const tileDatas = await this.mapCtx.getMapTiles({ x, y, xw: this.xw, yw: this.yw })
    tileDatas.forEach((data, idx) => this.tiles[idx].setData(data))
    this.mapCtx.updateMapPosition(x, y)
    this.draw()
  }
}



export class MapCanvas {
  canvas: HTMLCanvasElement
  ctx: CanvasRenderingContext2D;
  tileCanvas: TileCanvas
  center: { x: number, y: number }
  centerTileDefaultPosition: { x: number, y: number }

  press = false
  pressXY = { x: 0, y: 0 }
  lastHover?: MapTile

  constructor(canvas: HTMLCanvasElement, mapCtx: MapCtx, size: { width: number, height: number } = { width: 731, height: 550 }, center?: { x: number, y: number }) {
    this.canvas = canvas
    canvas.width = size.width
    canvas.height = size.height
    this.ctx = canvas.getContext('2d')!

    this.center = center || { x: Math.floor(size.width / 2), y: Math.floor(size.height / 2) }

    this.tileCanvas = new TileCanvas(mapCtx, size)
    this.tileCanvas.x = this.center.x - Math.floor(this.tileCanvas.width / 2)
    this.tileCanvas.y = this.center.y - Math.floor(this.tileCanvas.height / 2)
    const centerTile = this.tileCanvas.tiles[Math.floor(this.tileCanvas.tiles.length / 2)]
    this.centerTileDefaultPosition = { x: this.tileCanvas.x + centerTile.x, y: this.tileCanvas.y + centerTile.y }

    canvas.addEventListener('mousemove', async (e: MouseEvent) => await this.mousemove(e))
    canvas.addEventListener('mousedown', (e: MouseEvent) => this.mousedown(e))
    canvas.addEventListener('mouseup', async (e: MouseEvent) => await this.mouseup(e))
    canvas.addEventListener('mouseleave', async (e: MouseEvent) => await this.mouseup(e))
    // 
  }

  async moveTo(mapX: number, mapY: number, offsetX = 0, offsetY = 0) {
    mapX = mapX < 1 ? 1 : mapX > 500 ? 500 : mapX
    mapY = mapY < 1 ? 1 : mapY > 500 ? 500 : mapY
    await this.tileCanvas.updateData(mapX, mapY)
    this.tileCanvasPositionToCenter(offsetX, offsetY)
    this.draw()
  }

  dispose() {
    this.canvas.removeEventListener('mousemove', async (e: MouseEvent) => this.mousemove(e))
    this.canvas.removeEventListener('mousedown', (e: MouseEvent) => this.mousedown(e))
    this.canvas.removeEventListener('mouseup', async (e: MouseEvent) => await this.mouseup(e))
    this.canvas.removeEventListener('mouseleave', async (e: MouseEvent) => await this.mouseup(e))
  }

  isHover(x: number, y: number) { return this.tileCanvas.isHover(x, y) }

  showLv(show: boolean) {
    this.tileCanvas.mapCtx.showLv = show
    this.tileCanvas.draw()
    this.draw()
  }

  private tileCanvasPositionToCenter(offsetX = 0, offsetY = 0) {
    this.tileCanvas.x = this.center.x - Math.floor(this.tileCanvas.width / 2) - offsetX
    this.tileCanvas.y = this.center.y - Math.floor(this.tileCanvas.height / 2) - offsetY
  }

  private draw() {
    this.ctx.drawImage(this.tileCanvas.canvas, this.tileCanvas.x, this.tileCanvas.y)
  }

  private mousedown(e: MouseEvent) {
    this.press = true
    this.pressXY = { x: e.offsetX, y: e.offsetY }
  }

  private async mouseup(e: MouseEvent) {
    let pr = this.press
    this.press = false
    if (pr) {
      const newCenter = this.tileCanvas.isHover(this.center.x, this.center.y)

      if (newCenter && newCenter.data?.id != this.tileCanvas.centerId) {
        const offsetX = this.centerTileDefaultPosition.x - (this.tileCanvas.x + newCenter.x)
        const offsetY = this.centerTileDefaultPosition.y - (this.tileCanvas.y + newCenter.y)
        let { x, y } = wid2xy(newCenter.data!.id)
        if (newCenter.data!.state == -1 && newCenter.data!.idStr) {
          const da = newCenter.data!.idStr.split(',')
          x = Number(da[0])
          y = Number(da[1])
        }
        await this.moveTo(x, y, offsetX, offsetY)
      }
    }
  }

  private mousemove(e: MouseEvent) {
    if (this.press) {
      const mx = e.offsetX - this.pressXY.x
      const my = e.offsetY - this.pressXY.y
      if (mx == 0 && my == 0) { return }

      this.tileCanvas.x += mx
      this.tileCanvas.y += my
      this.draw()

      this.pressXY = { x: e.offsetX, y: e.offsetY }
    } else {
      const hover = this.isHover(e.offsetX, e.offsetY)
      if (this.lastHover != hover) {
        this.lastHover = hover
        this.tileCanvas.mapCtx.updateHoverTile(hover)
      }
    }
  }
}
