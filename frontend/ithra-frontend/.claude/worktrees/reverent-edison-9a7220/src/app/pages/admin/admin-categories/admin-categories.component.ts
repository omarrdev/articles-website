import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CategoryService } from '../../../services/category.service';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { Category } from '../../../models';

@Component({
  selector: 'app-admin-categories',
  standalone: true,
  imports: [FormsModule, ConfirmDialogComponent],
  templateUrl: './admin-categories.component.html'
})
export class AdminCategoriesComponent implements OnInit {
  private categoryService = inject(CategoryService);
  categories = signal<Category[]>([]);
  editingId: number | null = null;
  confirmVisible = false;
  pendingDeleteId: number | null = null;

  form = { name: '', description: '' };

  ngOnInit(): void { this.categoryService.getAll().subscribe(c => this.categories.set(c)); }

  submit(): void {
    const req = this.editingId
      ? this.categoryService.update(this.editingId, this.form)
      : this.categoryService.create(this.form);
    req.subscribe(cat => {
      if (this.editingId) {
        this.categories.update(list => list.map(c => c.id === cat.id ? cat : c));
      } else {
        this.categories.update(list => [...list, cat]);
      }
      this.form = { name: '', description: '' };
      this.editingId = null;
    });
  }

  edit(cat: Category): void { this.editingId = cat.id; this.form = { name: cat.name, description: cat.description }; }
  cancel(): void { this.editingId = null; this.form = { name: '', description: '' }; }

  confirmDelete(id: number): void { this.pendingDeleteId = id; this.confirmVisible = true; }
  onConfirmed(yes: boolean): void {
    this.confirmVisible = false;
    if (yes && this.pendingDeleteId) {
      this.categoryService.delete(this.pendingDeleteId).subscribe(() =>
        this.categories.update(list => list.filter(c => c.id !== this.pendingDeleteId))
      );
    }
    this.pendingDeleteId = null;
  }
}
