import type { SgCtx } from "./sg";

export class ChatMgr {
  private wsUrl: string // http://10.100.42.242:1111/sgws/ 
  private ws: WebSocket | null = null; // WebSocket 实例
  private reconnectTimer: NodeJS.Timeout | null = null; // 重试定时器
  private messages: string[] = []; // 存储消息（最多 300 条）
  private MAX_MESSAGE_COUNT = 300; // 消息上限

  constructor(private ctx: SgCtx) {
    this.wsUrl = ctx.ws
  }

  /**
   * 链接webscoket
   * 如果失败则5秒钟后重试
   * 
  */
  active() {
    if (this.ws) {
      this.dispose(); // 如果已有连接，先关闭
    }
    this.ws = new WebSocket(this.wsUrl);

    // 连接成功
    this.ws.onopen = () => {
      console.log("WebSocket 连接成功");
      this.clearReconnectTimer(); // 清除重试定时器
    };
    // 接收消息
    this.ws.onmessage = (event) => {
      const msg = event.data.toString();
      this.recMsg(msg);
    };

    // 错误处理
    this.ws.onerror = (error) => {
      console.error("WebSocket 错误:", error);
      this.handleReconnect();
    };

    // 连接关闭
    this.ws.onclose = (event) => {
      console.log(`WebSocket 关闭: ${event.reason}`);
      this.handleReconnect();
    };
  }

  /**关闭连接  */
  dispose() {
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
    this.clearReconnectTimer();
  }
  /**
   * 处理重连逻辑（5 秒后重试）
   */
  private handleReconnect() {
    this.clearReconnectTimer();
    this.reconnectTimer = setTimeout(() => {
      console.log("尝试重新连接 WebSocket...");
      this.active();
    }, 5000); // 5 秒后重试
  }

  /**
   * 清除重连定时器
   */
  private clearReconnectTimer() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
  }
  /**
   * 接收消息 使用变量存储消息.最多保留300条.
   * @param msg 
   */
  async recMsg(msg: string) {
    this.messages.unshift(msg); // 将新消息插入数组开头
    if (this.messages.length > this.MAX_MESSAGE_COUNT) {
      this.messages = this.messages.slice(0, this.MAX_MESSAGE_COUNT); // 截断超出部分
    }
  }

  /**
   * 发送消息
   * @param msg 
   */
  async sendMsg(msg: string) {
    console.log("sendMsg: " + msg);
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(msg);
    } else {
      console.warn("WebSocket 未连接，消息发送失败");
    }
  }

}