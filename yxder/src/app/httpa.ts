import axios from "axios";

export const baseURL = "/sg"
/**
 * 错误处理接口
 */
export interface ApiErrorHandler {
  processHttpErr(error: any): Promise<void>;
  processAppErr(error: any): Promise<void>;
}

axios.defaults.timeout = 120000; // 超时时间
axios.defaults.baseURL = baseURL; // 请求前缀路径
axios.defaults.headers["Content-Type"] = "application/json;charset=UTF-8"; // 请求前缀路径

axios.interceptors.request.use(
  (config) => {
    config.headers[httpHelper.tokenName] = httpHelper.token;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  res => { return res },
  error => {
    return httpHelper.errorHandler?.processHttpErr(error);
  }
)

export interface ErrResult {
  success: boolean;
  message: string;
  code: number;
}


export class Result {
  public success: boolean = false;
  public message: string | null = null;
  public data: any = null;
  public code: number = 200;
}

export interface PageResult {
  pageNum: number;
  pageSize: number;
  totalPages: number;
  totalRows: number;
  data: any[];
}

class HttpHelper {

  errorHandler?: ApiErrorHandler;
  token?: any;
  tokenName = 'xjsg';
  constructor() {
    this.token = sessionStorage.getItem(this.tokenName);
  }

  async get(url: string, data?: any): Promise<Result> {
    return axios.get(url, data).then(response => response.data as Result);
  }

  async post(url: string, data?: any): Promise<Result> {
    return axios.post(url, data).then(response => response.data as Result);
  }

  setErrorHandler(errorHandler: ApiErrorHandler) {
    this.errorHandler = errorHandler;
  }

  setToken(token: string) {
    this.token = token;
    sessionStorage.setItem(this.tokenName, token)
  }

}

const httpHelper = new HttpHelper();

export default httpHelper;
export const get = httpHelper.get;
export const post = httpHelper.post;
