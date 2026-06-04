import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ArticleService } from '../../../services/article.service';
import { CategoryService } from '../../../services/category.service';
import { ArticleCardComponent } from '../../../shared/article-card/article-card.component';
import { PaginationComponent } from '../../../shared/pagination/pagination.component';
import { Article, Category } from '../../../models';

@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [FormsModule, ArticleCardComponent, PaginationComponent],
  templateUrl: './article-list.component.html'
})
export class ArticleListComponent implements OnInit {
  private articleService = inject(ArticleService);
  private categoryService = inject(CategoryService);

  articles = signal<Article[]>([]);
  categories = signal<Category[]>([]);
  totalPages = signal(0);
  currentPage = signal(0);

  search = '';
  categorySlug = '';
  sort = 'createdAt,desc';

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(c => this.categories.set(c));
    this.load();
  }

  load(): void {
    const filters: Record<string, string> = { sort: this.sort };
    if (this.search) filters['search'] = this.search;
    if (this.categorySlug) filters['category'] = this.categorySlug;
    this.articleService.list(this.currentPage(), 9, filters).subscribe(page => {
      this.articles.set(page.content);
      this.totalPages.set(page.totalPages);
    });
  }

  onSearch(): void { this.currentPage.set(0); this.load(); }
  onPage(p: number): void { this.currentPage.set(p); this.load(); }
}
