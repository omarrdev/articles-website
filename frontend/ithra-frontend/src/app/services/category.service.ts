import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Category } from '../models';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAll() {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }

  getBySlug(slug: string) {
    return this.http.get<Category>(`${this.apiUrl}/categories/${slug}`);
  }

  create(data: Partial<Category>) {
    return this.http.post<Category>(`${this.apiUrl}/categories`, data);
  }

  update(id: number, data: Partial<Category>) {
    return this.http.put<Category>(`${this.apiUrl}/categories/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/categories/${id}`);
  }
}
