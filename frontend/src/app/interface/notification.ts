export interface SseResponse {
    data: SseData;
}

export interface SseData {
    event: string;
    message: string | Object
}