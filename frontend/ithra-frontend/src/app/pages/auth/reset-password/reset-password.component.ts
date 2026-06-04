import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="min-vh-100 d-flex align-items-center justify-content-center" style="background-color:var(--bg-secondary);">
      <div class="card shadow-sm p-4" style="width:100%;max-width:420px;background-color:var(--card-bg);border-color:var(--border-color);">
        <h3 class="text-center mb-4" style="color:var(--text-primary);">Reset Password</h3>
        @if (success()) {
          <div class="alert alert-success">Password reset! <a routerLink="/login" style="color:var(--accent);">Login</a></div>
        } @else {
          @if (error()) { <div class="alert alert-danger">{{ error() }}</div> }
          <form (ngSubmit)="submit()">
            <div class="mb-3">
              <label class="form-label" style="color:var(--text-primary);">New Password</label>
              <input type="password" class="form-control" [(ngModel)]="password" name="password" required
                     style="background-color:var(--bg-secondary);color:var(--text-primary);border-color:var(--border-color);" />
            </div>
            <div class="mb-3">
              <label class="form-label" style="color:var(--text-primary);">Confirm Password</label>
              <input type="password" class="form-control" [(ngModel)]="confirmPassword" name="confirm" required
                     style="background-color:var(--bg-secondary);color:var(--text-primary);border-color:var(--border-color);" />
            </div>
            <button class="btn btn-primary w-100" type="submit" [disabled]="loading()">
              {{ loading() ? 'Resetting...' : 'Reset Password' }}
            </button>
          </form>
        }
      </div>
    </div>
  `
})
export class ResetPasswordComponent implements OnInit {
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private token = '';
  password = '';
  confirmPassword = '';
  loading = signal(false);
  error = signal('');
  success = signal(false);

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  submit(): void {
    if (this.password !== this.confirmPassword) { this.error.set('Passwords do not match'); return; }
    this.loading.set(true);
    this.auth.resetPassword(this.token, this.password).subscribe({
      next: () => { this.loading.set(false); this.success.set(true); },
      error: (e) => { this.error.set(e.error?.message || 'Failed'); this.loading.set(false); }
    });
  }
}
