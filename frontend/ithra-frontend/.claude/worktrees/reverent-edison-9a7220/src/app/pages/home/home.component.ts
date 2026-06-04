import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ArticleService } from '../../services/article.service';
import { CategoryService } from '../../services/category.service';
import { ArticleCardComponent } from '../../shared/article-card/article-card.component';
import { Article, Category } from '../../models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, DatePipe, ArticleCardComponent],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  private articleService = inject(ArticleService);
  private categoryService = inject(CategoryService);

  hero = signal<Article | null>(null);
  articles = signal<Article[]>([]);
  categories = signal<Category[]>([]);

  ngOnInit(): void {
    this.articleService.list(0, 9, { sort: 'viewCount,desc' }).subscribe(page => {
      const content = page.content;
      this.hero.set(content[0] ?? null);
      this.articles.set(content.slice(0, 9));
    });
    this.categoryService.getAll().subscribe(cats => this.categories.set(cats));
  }
}
