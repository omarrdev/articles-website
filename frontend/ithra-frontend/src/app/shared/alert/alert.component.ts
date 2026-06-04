import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

export type AlertType = 'success' | 'error' | 'info';

interface AlertItem {
  id: number;
  message: string;
  type: AlertType;
}

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="alert-container">
      @for (alert of alerts(); track alert.id) {
        <div class="alert alert-dismissible fade show"
             [class]="'alert alert-' + (alert.type === 'error' ? 'danger' : alert.type)">
          {{ alert.message }}
          <button type="button" class="btn-close" (click)="remove(alert.id)"></button>
        </div>
      }
    </div>
  `,
  styles: [`
    .alert-container {
      position: fixed;
      top: 80px;
      right: 16px;
      z-index: 1100;
      min-width: 300px;
      max-width: 400px;
    }
  `]
})
export class AlertComponent {
  alerts = signal<AlertItem[]>([]);
  private nextId = 0;

  show(message: string, type: AlertType = 'info'): void {
    const id = ++this.nextId;
    this.alerts.update(a => [...a, { id, message, type }]);
    setTimeout(() => this.remove(id), 4000);
  }

  remove(id: number): void {
    this.alerts.update(a => a.filter(x => x.id !== id));
  }
}
