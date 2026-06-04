import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Article, Page } from '../models';

@Injectable({ providedIn: 'root' })
export class ArticleService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  list(page = 0, size = 9, filters: Record<string, string> = {}) {
    let params = new HttpParams().set('page', page).set('size', size);
    Object.entries(filters).forEach(([k, v]) => { if (v) params = params.set(k, v); });
    return this.http.get<Page<Article>>(`${this.apiUrl}/articles`, { params });
  }

  getBySlug(slug: string) {
    return this.http.get<Article>(`${this.apiUrl}/articles/${slug}`);
  }

  create(data: Partial<Article>) {
    return this.http.post<Article>(`${this.apiUrl}/articles`, data);
  }

  update(id: number, data: Partial<Article>) {
    return this.http.put<Article>(`${this.apiUrl}/articles/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/articles/${id}`);
  }

  publish(id: number) {
    return this.http.patch(`${this.apiUrl}/articles/${id}/publish`, {});
  }

  hide(id: number) {
    return this.http.patch(`${this.apiUrl}/articles/${id}/hide`, {});
  }

  toggleLike(id: number) {
    return this.http.post(`${this.apiUrl}/articles/${id}/like`, {});
  }

  getLikes(id: number) {
    return this.http.get<number>(`${this.apiUrl}/articles/${id}/likes`);
  }

  getMyArticles() {
    return this.http.get<Article[]>(`${this.apiUrl}/articles/my`);
  }
}
