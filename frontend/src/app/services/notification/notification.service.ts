import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { SseData, SseResponse } from '../../interface/notification';
import { EventSourceController, EventSourcePlus } from 'event-source-plus';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private zone: NgZone) {}
  token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcyNDA4NDI4MiwiZXhwIjoxNzI0MDg1NzIyfQ.ghtXWBJS7E_QwCquCh-zFwsFc-eiuJ00LjTypSXyjC8"

  subscribe(): Observable<SseData> {
    return new Observable<SseData>((observer) => {
      const eventSource = new EventSourcePlus(
        `http://localhost:8080/api/v1/notification/subscribe`,
        {
          headers: {
            Authorization: `Bearer ${this.token}`,
          }
        }
      );

      const controller = eventSource.listen({
        onMessage: (message) => {
          this.zone.run(() => {
            const data = JSON.parse(message.data)
            observer.next(data);
          });
        },
        onResponseError: ({ request, response, options }) => {
          console.error(`[response error]`, response.status, response.body);
          if (response.status >= 500) {
            controller.abort(); // Abort after certain conditions, e.g., repeated server errors
          }
        },
        onRequestError: ({ request, options, error }) => {
          console.error(`[request error]`, error);
          observer.error(error); // Notify the observer about the request error
        },
      });

      // Clean up the connection on unsubscription
      return () => controller.abort();
    });
  }

  unsubscribe(controller: EventSourceController) {
    if (controller) {
      controller.abort();
    }
  }
}