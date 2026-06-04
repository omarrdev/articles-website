import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ArticleService } from '../../../services/article.service';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { Article } from '../../../models';

@Component({
  selector: 'app-my-articles',
  standalone: true,
  imports: [RouterLink, DatePipe, ConfirmDialogComponent],
  templateUrl: './my-articles.component.html'
})
export class MyArticlesComponent implements OnInit {
  private articleService = inject(ArticleService);
  articles = signal<Article[]>([]);
  confirmVisible = false;
  pendingDeleteId: number | null = null;

  ngOnInit(): void {
    this.articleService.getMyArticles().subscribe(a => this.articles.set(a));
  }

  publish(id: number): void {
    this.articleService.publish(id).subscribe(() =>
      this.articles.update(list => list.map(a => a.id === id ? { ...a, status: 'PUBLISHED' as const } : a))
    );
  }

  confirmDelete(id: number): void { this.pendingDeleteId = id; this.confirmVisible = true; }

  onConfirmed(yes: boolean): void {
    this.confirmVisible = false;
    if (yes && this.pendingDeleteId) {
      this.articleService.delete(this.pendingDeleteId).subscribe(() =>
        this.articles.update(list => list.filter(a => a.id !== this.pendingDeleteId))
      );
    }
    this.pendingDeleteId = null;
  }
}
