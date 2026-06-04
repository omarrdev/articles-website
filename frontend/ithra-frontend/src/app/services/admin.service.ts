import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { User, AdminStats, ChartData, Page } from '../models';

interface MonthlyStatResponse { year: number; month: number; value: number; }
interface TopArticleResponse { id: number; title: string; slug: string; viewCount: number; likeCount: number; }
interface TopCategoryResponse { name: string; slug: string; articleCount: number; }

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getStats() {
    return this.http.get<AdminStats>(`${this.apiUrl}/admin/stats/overview`);
  }

  getArticlesPerMonth() {
    return this.http.get<MonthlyStatResponse[]>(`${this.apiUrl}/admin/stats/articles-over-time`).pipe(
      map(data => ({
        labels: data.map(d => `${d.year}-${String(d.month).padStart(2, '0')}`),
        data: data.map(d => Number(d.value))
      }) as ChartData)
    );
  }

  getViewsPerMonth() {
    return this.http.get<MonthlyStatResponse[]>(`${this.apiUrl}/admin/stats/views-over-time`).pipe(
      map(data => ({
        labels: data.map(d => `${d.year}-${String(d.month).padStart(2, '0')}`),
        data: data.map(d => Number(d.value))
      }) as ChartData)
    );
  }

  getArticlesPerCategory() {
    return this.http.get<TopCategoryResponse[]>(`${this.apiUrl}/admin/stats/top-categories`).pipe(
      map(data => ({
        labels: data.map(d => d.name),
        data: data.map(d => Number(d.articleCount))
      }) as ChartData)
    );
  }

  getTopArticles() {
    return this.http.get<TopArticleResponse[]>(`${this.apiUrl}/admin/stats/top-articles`);
  }

  getUsers(page = 0, size = 50) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<User>>(`${this.apiUrl}/admin/users`, { params }).pipe(
      map(p => p.content)
    );
  }

  updateUserRole(userId: number, role: string) {
    return this.http.patch<User>(`${this.apiUrl}/admin/users/${userId}/role`, { role });
  }

  deleteUser(userId: number) {
    return this.http.delete(`${this.apiUrl}/admin/users/${userId}`);
  }

  getArticles(page = 0, size = 50) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<any>>(`${this.apiUrl}/admin/articles`, { params }).pipe(
      map(p => p.content)
    );
  }

  deleteArticle(id: number) {
    return this.http.delete(`${this.apiUrl}/admin/articles/${id}`);
  }
}
