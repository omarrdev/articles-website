import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ArticleService } from '../../../services/article.service';
import { CommentService } from '../../../services/comment.service';
import { AuthService } from '../../../services/auth.service';
import { Article, Comment } from '../../../models';

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [RouterLink, DatePipe, FormsModule],
  templateUrl: './article-detail.component.html'
})
export class ArticleDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private articleService = inject(ArticleService);
  private commentService = inject(CommentService);
  auth = inject(AuthService);

  article = signal<Article | null>(null);
  comments = signal<Comment[]>([]);
  liked = signal(false);
  newComment = '';
  submitting = signal(false);

  ngOnInit(): void {
    const slug = this.route.snapshot.paramMap.get('slug')!;
    this.articleService.getBySlug(slug).subscribe(a => {
      this.article.set(a);
      this.commentService.list(a.id).subscribe(c => this.comments.set(c));
    });
  }

  toggleLike(): void {
    if (!this.auth.isLoggedIn()) return;
    const a = this.article();
    if (!a) return;
    this.articleService.toggleLike(a.id).subscribe(() => {
      this.liked.update(v => !v);
      this.article.update(art => art ? { ...art, likeCount: art.likeCount + (this.liked() ? 1 : -1) } : art);
    });
  }

  addComment(): void {
    const a = this.article();
    if (!a || !this.newComment.trim()) return;
    this.submitting.set(true);
    this.commentService.add(a.id, this.newComment).subscribe(c => {
      this.comments.update(list => [...list, c]);
      this.newComment = '';
      this.submitting.set(false);
    });
  }
}
