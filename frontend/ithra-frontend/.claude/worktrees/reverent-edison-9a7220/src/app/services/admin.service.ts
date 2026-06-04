import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User, Article, AdminStats, ChartData } from '../models';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getStats() {
    return this.http.get<AdminStats>(`${this.apiUrl}/admin/stats`);
  }

  getArticlesPerMonth() {
    return this.http.get<ChartData>(`${this.apiUrl}/admin/charts/articles-per-month`);
  }

  getViewsPerMonth() {
    return this.http.get<ChartData>(`${this.apiUrl}/admin/charts/views-per-month`);
  }

  getArticlesPerCategory() {
    return this.http.get<ChartData>(`${this.apiUrl}/admin/charts/articles-per-category`);
  }

  getTopArticles() {
    return this.http.get<Article[]>(`${this.apiUrl}/admin/top-articles`);
  }

  getUsers() {
    return this.http.get<User[]>(`${this.apiUrl}/admin/users`);
  }

  updateUserRole(userId: number, role: string) {
    return this.http.patch<User>(`${this.apiUrl}/admin/users/${userId}/role`, { role });
  }

  deleteUser(userId: number) {
    return this.http.delete(`${this.apiUrl}/admin/users/${userId}`);
  }

  getArticles() {
    return this.http.get<Article[]>(`${this.apiUrl}/admin/articles`);
  }

  deleteArticle(id: number) {
    return this.http.delete(`${this.apiUrl}/admin/articles/${id}`);
  }
}
