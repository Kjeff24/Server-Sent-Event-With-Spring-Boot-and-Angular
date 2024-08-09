import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { SseResponse } from '../../interface/notification';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private eventSource!: EventSource;

  constructor(private zone: NgZone) {}

  subscribe(userId: string): Observable<any> {
    return new Observable<SseResponse>((observer) => {
      this.eventSource = new EventSource(
        `http://localhost:8050/api/v1/notifications/subscribe/${userId}`
      );
      this.eventSource.onmessage = (event) => {
        this.zone.run(() => {
          const data = JSON.parse(event.data)
          observer.next(data);
        });
      };

      this.eventSource.onerror = (error) => {
        console.error('EventSource error:', error);
        if (this.eventSource.readyState === EventSource.CLOSED) {
          observer.complete();
        } else {
          observer.error('EventSource error: ' + error);
        }
      };

      // Clean up the connection on unsubscription
      return () => this.eventSource.close();
    });
  }

  unsubscribe() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }
}
