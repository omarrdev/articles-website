import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Comment } from '../models';

@Injectable({ providedIn: 'root' })
export class CommentService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  list(articleId: number) {
    return this.http.get<Comment[]>(`${this.apiUrl}/articles/${articleId}/comments`);
  }

  add(articleId: number, content: string) {
    return this.http.post<Comment>(`${this.apiUrl}/articles/${articleId}/comments`, { content });
  }

  delete(articleId: number, commentId: number) {
    return this.http.delete(`${this.apiUrl}/articles/${articleId}/comments/${commentId}`);
  }
}
