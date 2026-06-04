import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [DatePipe],
  template: `
    <div class="container py-4" style="max-width:600px;">
      <h2 class="mb-4 fw-semibold" style="color:var(--text-primary);">My Profile</h2>
      @if (auth.currentUser(); as user) {
        <div class="card p-4" style="background-color:var(--card-bg);border-color:var(--border-color);">
          <div class="d-flex align-items-center gap-3 mb-4">
            <div class="avatar-lg">{{ user.username.slice(0,2).toUpperCase() }}</div>
            <div>
              <h4 class="mb-0" style="color:var(--text-primary);">{{ user.username }}</h4>
              <span class="badge" style="background-color:var(--accent);">{{ user.role }}</span>
            </div>
          </div>
          <dl class="row">
            <dt class="col-sm-4" style="color:var(--text-secondary);">Email</dt>
            <dd class="col-sm-8" style="color:var(--text-primary);">{{ user.email }}</dd>
            <dt class="col-sm-4" style="color:var(--text-secondary);">Verified</dt>
            <dd class="col-sm-8">
              <span [class]="user.isEmailVerified ? 'text-success' : 'text-danger'">
                {{ user.isEmailVerified ? '✅ Yes' : '❌ No' }}
              </span>
            </dd>
            <dt class="col-sm-4" style="color:var(--text-secondary);">Member since</dt>
            <dd class="col-sm-8" style="color:var(--text-primary);">{{ user.createdAt | date:'mediumDate' }}</dd>
          </dl>
        </div>
      }
    </div>
    <style>
    .avatar-lg { width:64px;height:64px;border-radius:50%;background-color:var(--accent);color:#fff;display:flex;align-items:center;justify-content:center;font-size:1.25rem;font-weight:700; }
    </style>
  `
})
export class ProfileComponent {
  auth = inject(AuthService);
}
