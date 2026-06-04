import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AdminService } from '../../../services/admin.service';
import { ArticleService } from '../../../services/article.service';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { Article } from '../../../models';

@Component({
  selector: 'app-admin-articles',
  standalone: true,
  imports: [RouterLink, DatePipe, ConfirmDialogComponent],
  templateUrl: './admin-articles.component.html'
})
export class AdminArticlesComponent implements OnInit {
  private adminService = inject(AdminService);
  private articleService = inject(ArticleService);
  articles = signal<Article[]>([]);
  confirmVisible = false;
  pendingDeleteId: number | null = null;

  ngOnInit(): void { this.adminService.getArticles().subscribe(a => this.articles.set(a)); }

  hide(id: number): void {
    this.articleService.hide(id).subscribe(() =>
      this.articles.update(list => list.map(a => a.id === id ? { ...a, status: 'DRAFT' as const } : a))
    );
  }

  confirmDelete(id: number): void { this.pendingDeleteId = id; this.confirmVisible = true; }
  onConfirmed(yes: boolean): void {
    this.confirmVisible = false;
    if (yes && this.pendingDeleteId) {
      this.adminService.deleteArticle(this.pendingDeleteId).subscribe(() =>
        this.articles.update(list => list.filter(a => a.id !== this.pendingDeleteId))
      );
    }
    this.pendingDeleteId = null;
  }
}
