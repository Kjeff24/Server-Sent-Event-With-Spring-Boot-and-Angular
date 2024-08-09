import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { NotificationService } from '../../services/notification/notification.service';
import { SseData } from '../../interface/notification';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css',
})
export class NotificationComponent {
  messages: string[] = [];
  userId!: string;

  constructor(
    private location: Location,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    const urlSegments = this.location.path().split('/');
    this.userId = urlSegments[urlSegments.length - 1];
    if (this.userId) {
      console.log(this.userId)
      this.getNotification();
    }
  }

  ngOnDestroy(): void {
    this.notificationService.unsubscribe();
  }

  getNotification() {
    this.notificationService.subscribe(this.userId).subscribe({
      next: (data: SseData) => {
        console.log(data)
        console.log('Message received: ' + data.event);
        if(typeof data.message === 'string') {
          console.log('Message received: ' + data.message);
        } else {
          console.log('Title: ' + data.message.title);
          console.log('Title: ' + data.message.message);
        }
      },
      error: (error) => {
        console.error('Error receiving notifications: ', error);
      },
      complete: () => {
        console.log('Notification stream closed');
      }
    })
  }
  
}
