export interface SseResponse {
    data: SseData;
}

export interface SseData {
    event: string;
    message: string | Notification
}

export interface Notification {
    id: number;
    title: string;
    message: string;
    read: boolean;
    date: Date;
}