import type { Ref } from "vue";
import { Img } from "./img";
import type { MemMapTile } from "./modelData";
import { wid2xy } from "./constant";
import type { SanGuo } from "./sg";

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



export class CanvasWindow {
  w: number
  h: number
  // children: Sp[] = []
  canvas: HTMLCanvasElement
  ctx: CanvasRenderingContext2D
  // moveMgr: CanvasEventMgr
  // map: WMap
  mapChange?: (x: number, y: number) => any

  constructor(sg: SanGuo, w: number, h: number, ws: number, hs: number, canvas: HTMLCanvasElement) {
    this.w = w
    this.h = h
    this.canvas = canvas
    this.canvas.width = w
    this.canvas.height = h
    this.ctx = canvas.getContext('2d')!
    // this.moveMgr = new CanvasEventMgr(this, canvas)
    // this.map = new WMap(sg, this, ws, hs)
  }

  async draw() {
    const ctx = this.ctx
    // for (let i = 0; i < this.children.length; i++) {
    //   await this.children[i].prepareDraw()
    // }
    this.drawBg(ctx)
    // for (let i = 0; i < this.children.length; i++) {
    //   await this.children[i].draw(this.ctx)
    // }
  }

  async moveTo(x: number, y: number) {
    // console.log('moveTo', this.map.mapX, this.map.mapY, x, y)
    // this.map.updateMapIdx(x, y)
    // await this.draw()
  }

  async moveOffset(x: number, y: number) {
    // await this.moveTo(this.map.mapX + x, this.map.mapY + y)
  }

  getMapIdx() {
    // return { x: this.map.mapX, y: this.map.mapY }
  }

  private drawBg(ctx: CanvasRenderingContext2D) {
    ctx.fillStyle = '#fff'
    ctx.fillRect(0, 0, this.w, this.h)
    ctx.lineWidth = 1
    ctx.fillStyle = '#F00'
  }

}


// export class WorldMapTile {

// }

// export interface Sp {
//   prepareDraw: () => Promise<any> | void
//   draw: (ctx: CanvasRenderingContext2D) => Promise<any> | void
//   isHover?: (x: number, y: number) => boolean
//   mousemove?: (x: number, y: number) => any
//   mouseup?: () => any
//   move?: (x: number, y: number) => any
// }

// export class WTile implements Sp {
//   idx: number
//   x: number
//   y: number
//   map: WMap
//   data?: MemMapTile
//   constructor(map: WMap, idx: number, x: number, y: number) {
//     this.map = map
//     this.idx = idx
//     this.x = x
//     this.y = y
//   }


//   async prepareDraw() {

//   }

//   async draw(ctx: CanvasRenderingContext2D) {
//     // console.log(this)
//     // console.log(`draw `, JSON.stringify(this.data))

//     if (this.data && this.data.state != -1) {
//       const { x, y } = wid2xy(this.data.id)
//       ctx.drawImage(this.getImg().imgEl!, this.x, this.y)

//       ctx.font = "14px console"
//       ctx.fillText(`${x},${y}`, this.x + 40, this.y + 54)
//     } else {

//     }
//     // ctx.drawImage(this.getImg().imgEl!, this.x, this.y)
//   }

//   isHover(x: number, y: number) {
//     const cx = x - this.x
//     const cy = y - this.y
//     return this.getImg().isHover(cx, cy)
//   }

//   getImg() {
//     if (this.data) {
//       if (this.data.tileType > 0) {
//         return bgRes[this.data!.tileType]!
//       } else {
//         return bgRes[0]!
//       }
//     }
//     return bgRes[1]!
//   }

// }

// export class WMapPosition {
//   private initialX: number  // 初始坐标X
//   private initialY: number // 初始坐标y
//   private offsetMapX: number = 0
//   private offsetMapY: number = 0
//   private offsetX: number = 0
//   private offsetY: number = 0
//   private map: WMap

//   x: number
//   y: number

//   constructor(win: CanvasWindow, map: WMap) {
//     this.map = map
//     const initialX = (win.w - map.w) / 2 - 10
//     const initialY = (win.h - map.h) / 2 - 21
//     // console.log(`initial ${initialX} ${initialY} , ${(map.w - 108) / 2 + initialX} ${(map.h - 54) / 2 + initialY}`)
//     // this.centerX = (map.w - 108) / 2 + initialX
//     // this.centerY = (map.h - 54) / 2 + initialY
//     this.initialX = initialX
//     this.initialY = initialY
//     this.x = this.initialX
//     this.y = this.initialY
//   }

//   reset(offset: boolean = false) {
//     // console.log(`reset `)
//     this.x = this.initialX
//     this.y = this.initialY
//     if (offset) {
//       this.x += this.offsetX
//       this.y += this.offsetY
//     }
//   }

//   getPosition(parentX: number, parentY: number) {
//     return {
//       x: parentX - this.x,
//       y: parentY - this.y,
//     }
//   }

//   move(x: number, y: number) {
//     this.x += x
//     this.y += y
//     //


//     const ox = this.x - this.initialX
//     const oy = this.y - this.initialY

//     let offsetMapX = 0
//     let offsetMapY = 0

//     let tx = ox > 0 ? Math.floor(ox / 108) : Math.floor(Math.abs(ox) / 108) * -1
//     let ty = oy > 0 ? Math.floor(oy / 54) : Math.floor(Math.abs(oy) / 54) * -1
//     offsetMapX -= tx
//     offsetMapY += tx
//     offsetMapX -= ty
//     offsetMapY -= ty

//     let olx = ox % 108
//     let oly = oy % 54
//     // const mx = olx < 0 ? 108 + olx : olx
//     const mx = olx + 54
//     const my = oly + 27
//     // console.log(`aa off : ${this.map.mapX + offsetMapX} ${this.map.mapY + offsetMapY} ${ox} ${oy} ${olx} ${oly} ,${mx} ${my}`)
//     if (!this.isInCenter(mx, my)) {
//       if (olx < -5 && oly < -5) { // 左上移动
//         offsetMapX += 1
//         olx += 54
//         oly += 27
//         // console.log(`左上 `, olx, oly)
//       } else if (olx < -5 && oly > 5) {
//         offsetMapY -= 1
//         olx += 54
//         oly -= 27
//         // console.log(`左下 `, olx, oly)
//       } else if (olx > 5 && oly < -5) {
//         // console.log(`右上 `, olx, oly)
//         offsetMapY += 1
//         olx -= 54
//         oly += 27
//       } else if (olx > 5 && oly > 5) {
//         // console.log(`右下 `, olx, oly)
//         offsetMapY += 1
//         olx -= 54
//         oly += 27
//       }
//     }
//     this.offsetMapX = offsetMapX
//     this.offsetMapY = offsetMapY
//     this.offsetX = olx
//     this.offsetY = oly
//     // console.log(`bb off : ${this.map.mapX + offsetMapX} ${this.map.mapY + offsetMapY} `, olx, oly)
//   }

//   private isInCenter(x: number, y: number) {
//     if (x < 0 || y < 0 || x > 108 || y > 54) { return false }
//     // return bgRes[1].isHover(x, y + 21)
//     return true
//   }

//   getOffsetMapIdx() {
//     return { x: this.offsetMapX, y: this.offsetMapY }
//   }

// }


// export class WMap implements Sp {
//   sg: SanGuo
//   window: CanvasWindow
//   ws: number // 获取地图的数量 = 1 + ws *2
//   hs: number // 获取地图的数量 = 1 + hs *2
//   p: WMapPosition
//   w: number
//   h: number
//   mapX: number = 3
//   mapY: number = 3

//   canvas: HTMLCanvasElement
//   ctx: CanvasRenderingContext2D
//   tiles: WTile[] = []
//   lastHoverTiles?: WTile
//   changeHoverTile?: (t: WTile | undefined) => any
//   needRepaint = true
//   constructor(sg: SanGuo, window: CanvasWindow, ws: number, hs: number) {
//     this.sg = sg
//     this.window = window
//     this.ws = ws
//     this.hs = hs
//     const tws = this.ws * 2 + 1
//     const ths = (this.hs * 2) * 2 + 1
//     this.w = tws * 108
//     this.h = (this.hs * 2 + 1) * 54
//     this.window.children.push(this as any as Sp)
//     // const tileCount = tws * ths

//     let needLine = false
//     for (let i = 0; i < ths; i++) {
//       needLine = !needLine
//       for (let j = 0; j < tws; j++) {
//         const x = j * 108 + (needLine ? 0 : -54)
//         const y = i * 27 - 21
//         const idx = j + i * tws
//         this.tiles.push(new WTile(this, idx, x, y))
//       }
//     }

//     // 画布
//     this.canvas = document.createElement('canvas');
//     this.canvas.width = this.w
//     this.canvas.height = this.h
//     this.ctx = this.canvas.getContext('2d')!

//     // 坐标
//     this.p = new WMapPosition(window, this)

//     // console.log(`width: ${this.w} ${this.h}`)

//   }

//   async prepareDraw() {
//     if (!this.needRepaint) { return }
//     const ctx = this.ctx
//     //
//     // this.drawBg(ctx)
//     //
//     // const tileDatas = this.sg.res.getGlobarCfgMgr().getMap(this.mapX, this.mapY, this.ws, this.hs)

//     const tileDatas = await this.sg.api.envApi.getMapTiles({ x: this.mapX, y: this.mapY, xw: this.ws, yw: this.hs })
//     for (let i = 0; i < this.tiles.length; i++) {
//       try {
//         this.tiles[i].data = tileDatas[i]
//       } catch (error) {
//         console.log(error)
//       }
//     }
//   }

//   async draw(ctx: CanvasRenderingContext2D) {
//     if (this.needRepaint) {
//       // console.log(`repaint `)
//       this.repaint()
//       this.needRepaint = false
//     }

//     // ctx.drawImage(this.canvas, 0, 0)
//     ctx.drawImage(this.canvas, this.p.x, this.p.y)
//   }

//   async repaint() {
//     this.drawBg(this.ctx)
//     this.tiles.forEach(t => t.draw(this.ctx))

//     this.ctx.fillStyle = '#F00'
//     this.ctx.fillRect(this.w / 2 - 2, this.h / 2 - 2, 4, 4)
//   }

//   drawBg(ctx: CanvasRenderingContext2D) {
//     ctx.fillStyle = '#000'
//     ctx.fillRect(0, 0, this.w, this.h)

//     ctx.fillStyle = '#000'
//     ctx.fillRect(0, 0, 3, 3)
//   }

//   mousemove(parentX: number, parentY: number) {
//     const { x, y } = this.p.getPosition(parentX, parentY)
//     if (x < 0 || x > this.w || y < 0 || y > this.h) { return false }
//     // console.log(`x: ${x},y: ${y}`)
//     this.findHoverTile(x, y)

//     return false
//   }

//   findHoverTile(x: number, y: number) {
//     let curTile = undefined
//     for (let i = 0; i < this.tiles.length; i++) {
//       const ti = this.tiles[i];
//       if (ti.isHover(x, y)) {
//         curTile = ti
//         break
//       }
//     }
//     if (curTile != this.lastHoverTiles) {
//       this.lastHoverTiles = curTile
//       this.changeHoverTile && this.changeHoverTile(curTile)
//     }
//   }

//   move(x: number, y: number) {
//     this.p.move(x, y)
//   }

//   mouseup() {
//     const { x, y } = this.p.getOffsetMapIdx()
//     if (x !== 0 || y !== 0) {
//       this.updateMapIdx(this.mapX + x, this.mapY + y)
//     }
//   }

//   updateMapIdx(idxX: number, idxY: number) {
//     // console.log(`idxX: ${idxX} idxY ${idxY}`)
//     const oldx = this.mapX, oldy = this.mapY
//     this.mapX = Number(idxX)
//     this.mapY = Number(idxY)
//     const s = this.mapX < 0 || this.mapX > 500 || this.mapY < 0 || this.mapY > 500
//     this.mapX < 0 && (this.mapX = 1)
//     this.mapY < 0 && (this.mapY = 1)
//     this.mapX > 500 && (this.mapX = 500)
//     this.mapY > 500 && (this.mapY = 500)
//     this.p.reset(!s)
//     // console.log(`map :  `, this.mapX, this.mapY)
//     if (oldx != this.mapX || oldy != this.mapY) {
//       this.window.mapChange && this.window.mapChange(this.mapX, this.mapY)
//     }

//     this.needRepaint = true
//   }

// }


// export class CanvasWindow {
//   w: number
//   h: number
//   children: Sp[] = []
//   canvas: HTMLCanvasElement
//   ctx: CanvasRenderingContext2D
//   moveMgr: CanvasEventMgr
//   map: WMap
//   mapChange?: (x: number, y: number) => any

//   constructor(sg: SanGuo, w: number, h: number, ws: number, hs: number, canvas: HTMLCanvasElement) {
//     this.w = w
//     this.h = h
//     this.canvas = canvas
//     this.canvas.width = w
//     this.canvas.height = h
//     this.ctx = canvas.getContext('2d')!
//     this.moveMgr = new CanvasEventMgr(this, canvas)
//     this.map = new WMap(sg, this, ws, hs)
//   }

//   async draw() {
//     const ctx = this.ctx
//     for (let i = 0; i < this.children.length; i++) {
//       await this.children[i].prepareDraw()
//     }
//     this.drawBg(ctx)
//     for (let i = 0; i < this.children.length; i++) {
//       await this.children[i].draw(this.ctx)
//     }
//   }

//   async moveTo(x: number, y: number) {
//     // console.log('moveTo', this.map.mapX, this.map.mapY, x, y)
//     this.map.updateMapIdx(x, y)
//     await this.draw()
//   }

//   async moveOffset(x: number, y: number) {
//     await this.moveTo(this.map.mapX + x, this.map.mapY + y)
//   }

//   getMapIdx() {
//     return { x: this.map.mapX, y: this.map.mapY }
//   }

//   private drawBg(ctx: CanvasRenderingContext2D) {
//     ctx.fillStyle = '#fff'
//     ctx.fillRect(0, 0, this.w, this.h)
//     ctx.lineWidth = 1
//     ctx.fillStyle = '#F00'
//   }

// }


// export class CanvasEventMgr {
//   window: CanvasWindow
//   press = false
//   pressX: number = 0
//   moved = false
//   pressY: number = 0
//   constructor(window: CanvasWindow, canvas: HTMLCanvasElement) {
//     this.window = window
//     canvas.addEventListener('mousemove', async (e: MouseEvent) => await this.mousemove(e))
//     canvas.addEventListener('mousedown', (e: MouseEvent) => this.mousedown(e))
//     canvas.addEventListener('mouseup', async (e: MouseEvent) => await this.mouseup(e))
//     canvas.addEventListener('mouseleave', async (e: MouseEvent) => await this.mouseup(e))
//   }

//   mousedown(e: MouseEvent) {
//     if (e.buttons !== 1) { return (this.press = false) }
//     this.press = true
//     this.moved = false
//     this.pressX = e.offsetX
//     this.pressY = e.offsetY
//   }

//   async mouseclick(e: MouseEvent) {

//   }


//   async mouseup(e: MouseEvent) {
//     if (!this.press) { return }
//     this.press = false
//     if (this.moved) {
//       this.window.children.forEach(async c => c.mouseup && c.mouseup())
//       await this.window.draw()
//     }
//   }

//   async mousemove(e: MouseEvent) {
//     if (this.press) {
//       this.moved = true
//       const x = (e.offsetX - this.pressX)
//       const y = (e.offsetY - this.pressY)
//       if (x && y) {
//         this.pressX = e.offsetX
//         this.pressY = e.offsetY
//         this.window.children.forEach(async c => c.move && c.move(x, y))
//         await this.window.draw()
//       }
//     }

//     this.window.children.forEach(async c => c.mousemove && c.mousemove(e.offsetX, e.offsetY))
//   }


// }
// // // 加载本身资源
// Img.loadImages(bgRes, (def, err) => { }); 



