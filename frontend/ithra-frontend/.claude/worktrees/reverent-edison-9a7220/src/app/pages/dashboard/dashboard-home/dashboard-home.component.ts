import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { Article } from '../../../models';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard-home.component.html'
})
export class DashboardHomeComponent implements OnInit {
  private articleService = inject(ArticleService);
  articles = signal<Article[]>([]);

  get totalArticles() { return this.articles().length; }
  get drafts() { return this.articles().filter(a => a.status === 'DRAFT').length; }
  get totalViews() { return this.articles().reduce((s, a) => s + a.viewCount, 0); }
  get totalLikes() { return this.articles().reduce((s, a) => s + a.likeCount, 0); }

  ngOnInit(): void {
    this.articleService.getMyArticles().subscribe(a => this.articles.set(a));
  }
}
