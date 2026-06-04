import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models';

@Component({
  selector: 'app-categories-page',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container py-4">
      <h2 class="mb-4 fw-semibold" style="color:var(--text-primary);">Categories</h2>
      <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
        @for (cat of categories(); track cat.id) {
          <div class="col">
            <a [routerLink]="['/categories', cat.slug]" class="text-decoration-none">
              <div class="card h-100" style="background-color:var(--card-bg);border-color:var(--border-color);">
                <div class="card-body">
                  <h5 class="card-title" style="color:var(--accent);">{{ cat.name }}</h5>
                  <p class="card-text small" style="color:var(--text-secondary);">{{ cat.description }}</p>
                  <span class="badge" style="background-color:var(--accent);">{{ cat.articleCount ?? 0 }} articles</span>
                </div>
              </div>
            </a>
          </div>
        }
        @empty {
          <p style="color:var(--text-secondary);">No categories found.</p>
        }
      </div>
    </div>
  `
})
export class CategoriesPageComponent implements OnInit {
  private categoryService = inject(CategoryService);
  categories = signal<Category[]>([]);
  ngOnInit(): void { this.categoryService.getAll().subscribe(c => this.categories.set(c)); }
}
