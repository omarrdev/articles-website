import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { CategoryService } from '../../../services/category.service';
import { ArticleCardComponent } from '../../../shared/article-card/article-card.component';
import { PaginationComponent } from '../../../shared/pagination/pagination.component';
import { Article, Category } from '../../../models';

@Component({
  selector: 'app-category-articles',
  standalone: true,
  imports: [ArticleCardComponent, PaginationComponent],
  template: `
    <div class="container py-4">
      @if (category()) {
        <h2 class="mb-1 fw-semibold" style="color:var(--text-primary);">{{ category()!.name }}</h2>
        <p class="mb-4" style="color:var(--text-secondary);">{{ category()!.description }}</p>
      }
      <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-4 mb-4">
        @for (a of articles(); track a.id) {
          <div class="col"><app-article-card [article]="a" /></div>
        }
        @empty { <p style="color:var(--text-secondary);">No articles in this category.</p> }
      </div>
      <app-pagination [currentPage]="page()" [totalPages]="totalPages()" (pageChange)="onPage($event)" />
    </div>
  `
})
export class CategoryArticlesComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private articleService = inject(ArticleService);
  private categoryService = inject(CategoryService);

  articles = signal<Article[]>([]);
  category = signal<Category | null>(null);
  page = signal(0);
  totalPages = signal(0);

  ngOnInit(): void {
    const slug = this.route.snapshot.paramMap.get('slug')!;
    this.categoryService.getBySlug(slug).subscribe(c => this.category.set(c));
    this.load(slug);
  }

  load(slug = this.route.snapshot.paramMap.get('slug')!): void {
    this.articleService.list(this.page(), 9, { category: slug }).subscribe(p => {
      this.articles.set(p.content);
      this.totalPages.set(p.totalPages);
    });
  }

  onPage(p: number): void { this.page.set(p); this.load(); }
}
