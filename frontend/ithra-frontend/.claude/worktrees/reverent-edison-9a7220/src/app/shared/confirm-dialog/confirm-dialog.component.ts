import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [NgIf],
  template: `
    <div class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.5);" *ngIf="visible">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="background-color: var(--card-bg); color: var(--text-primary);">
          <div class="modal-header">
            <h5 class="modal-title">Confirm</h5>
            <button type="button" class="btn-close" (click)="respond(false)"></button>
          </div>
          <div class="modal-body">{{ message }}</div>
          <div class="modal-footer">
            <button class="btn btn-secondary" (click)="respond(false)">Cancel</button>
            <button class="btn btn-danger" (click)="respond(true)">Confirm</button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ConfirmDialogComponent {
  @Input() message = 'Are you sure?';
  @Input() visible = false;
  @Output() confirmed = new EventEmitter<boolean>();

  respond(value: boolean): void {
    this.confirmed.emit(value);
  }
}
