import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Tag } from '../models';

@Injectable({ providedIn: 'root' })
export class TagService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAll() {
    return this.http.get<Tag[]>(`${this.apiUrl}/tags`);
  }

  create(data: Partial<Tag>) {
    return this.http.post<Tag>(`${this.apiUrl}/tags`, data);
  }

  update(id: number, data: Partial<Tag>) {
    return this.http.put<Tag>(`${this.apiUrl}/tags/${id}`, data);
  }

  delete(id: number) {
    return this.http.delete(`${this.apiUrl}/tags/${id}`);
  }
}
