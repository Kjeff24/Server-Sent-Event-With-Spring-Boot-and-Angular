import { Component, OnDestroy, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { NotificationService } from '../../services/notification/notification.service';
import { SseData } from '../../interface/notification';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [],
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css'], // Corrected to styleUrls
})
export class NotificationComponent implements OnInit, OnDestroy {
  messages: string[] = [];
  userId!: string;
  private controller: any; // Store the controller for unsubscribing

  constructor(
    private location: Location,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.getNotification();
  }

  ngOnDestroy(): void {
    if (this.controller) {
      this.controller.abort();
    }
  }

  getNotification(): void {
    this.controller = this.notificationService.subscribe().subscribe({
      next: (data: SseData) => {
        console.log('Message received: ', data);

        if (typeof data.message === 'string') {
          console.log('Message: ' + data.message);
          this.messages.push(data.message);
        } else if (data.message && typeof data.message === 'object') {
          console.log('Title: ' + data.message.title);
          console.log('Message: ' + data.message.message);
        }
      },
      error: (error) => {
        console.error('Error receiving notifications: ', error);
      },
      complete: () => {
        console.log('Notification stream closed');
      },
    });
  }
}
