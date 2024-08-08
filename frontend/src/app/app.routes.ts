import { Routes } from '@angular/router';
import { NotificationComponent } from './components/notification/notification.component';

export const routes: Routes = [
  {
    path: '',
    component: NotificationComponent,
    title: 'Notification'
  },
  {
    path: ':userId',
    component: NotificationComponent,
    title: 'Notification'
  },
];
