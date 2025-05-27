import axios, { AxiosError, AxiosHeaders } from "axios";
export const baseURL = "/sg-api"

axios.defaults.timeout = 120000; // 超时时间
axios.defaults.baseURL = baseURL; // 请求前缀路径
axios.defaults.headers["Content-Type"] = "application/json;charset=UTF-8"; // 请求前缀路径
axios.defaults.headers["x-requested-with"] = "XMLHttpRequest"; // ajax

export class Result {
    public success: boolean = false;
    public message: string | null = null;
    public data: any = null;
    public code: number = 200;
}

export interface PageResult<T> {
    pageNum: number;
    pageSize: number;
    totalPages: number;
    totalRows: number;
    data: T[];
}

export interface PageCdt<T> {
    pageNum: number;
    pageSize: number;
    cdt: T;
}

export function emptyPageResult<T>(): PageResult<T> {
    return {
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalRows: 0,
        data: [] as T[],
    }
}

export interface IUserToken {
    handler401(): void
    getToken(): string | undefined
    getTokenKey(): string
}

export abstract class AbsApi {
    user!: IUserToken
    constructor(userToken: IUserToken) {
        this.user = userToken
    }

    async get(url: string, data?: any): Promise<Result> {
        return axios.get(url, {
            headers: this.headers()
        }).then(response => response.data as Result)
            .catch(err => {
                if (err instanceof AxiosError) {
                    if (err.response?.status == 401) {
                        this.user.handler401()
                    }
                }
                throw err
            })
    }

    async post(url: string, data?: any): Promise<Result> {
        return axios.post(url, data, {
            headers: this.headers(),
        }).then(response => response.data as Result)
            .catch(err => {
                if (err instanceof AxiosError) {
                    if (err.response?.status == 401) {
                        this.user.handler401()
                    }
                }
                throw err
            })
    }

    private headers() {
        const h = new AxiosHeaders()
        h.set(this.user.getTokenKey(), this.user.getToken() || '')
        return h
    }

}