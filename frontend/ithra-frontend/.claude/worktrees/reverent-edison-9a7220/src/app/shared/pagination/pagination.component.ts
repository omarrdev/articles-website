import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  template: `
    <nav *ngIf="totalPages > 1">
      <ul class="pagination justify-content-center">
        <li class="page-item" [class.disabled]="currentPage === 0">
          <button class="page-link" (click)="go(currentPage - 1)">Previous</button>
        </li>
        @for (p of pages; track p) {
          <li class="page-item" [class.active]="p === currentPage">
            <button class="page-link" (click)="go(p)">{{ p + 1 }}</button>
          </li>
        }
        <li class="page-item" [class.disabled]="currentPage === totalPages - 1">
          <button class="page-link" (click)="go(currentPage + 1)">Next</button>
        </li>
      </ul>
    </nav>
  `
})
export class PaginationComponent {
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Output() pageChange = new EventEmitter<number>();

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  go(page: number): void {
    if (page >= 0 && page < this.totalPages) this.pageChange.emit(page);
  }
}
