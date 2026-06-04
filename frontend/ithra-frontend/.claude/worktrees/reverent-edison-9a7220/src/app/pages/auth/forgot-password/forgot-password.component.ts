import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="min-vh-100 d-flex align-items-center justify-content-center" style="background-color:var(--bg-secondary);">
      <div class="card shadow-sm p-4" style="width:100%;max-width:420px;background-color:var(--card-bg);border-color:var(--border-color);">
        <h3 class="text-center mb-4" style="color:var(--text-primary);">Forgot Password</h3>
        @if (success()) {
          <div class="alert alert-success">Reset link sent to your email.</div>
        } @else {
          @if (error()) { <div class="alert alert-danger">{{ error() }}</div> }
          <form (ngSubmit)="submit()">
            <div class="mb-3">
              <label class="form-label" style="color:var(--text-primary);">Email</label>
              <input type="email" class="form-control" [(ngModel)]="email" name="email" required
                     style="background-color:var(--bg-secondary);color:var(--text-primary);border-color:var(--border-color);" />
            </div>
            <button class="btn btn-primary w-100" type="submit" [disabled]="loading()">
              {{ loading() ? 'Sending...' : 'Send Reset Link' }}
            </button>
          </form>
          <p class="text-center mt-3 small"><a routerLink="/login" style="color:var(--accent);">Back to Login</a></p>
        }
      </div>
    </div>
  `
})
export class ForgotPasswordComponent {
  private auth = inject(AuthService);
  email = '';
  loading = signal(false);
  error = signal('');
  success = signal(false);

  submit(): void {
    this.loading.set(true);
    this.auth.forgotPassword(this.email).subscribe({
      next: () => { this.loading.set(false); this.success.set(true); },
      error: (e) => { this.error.set(e.error?.message || 'Failed'); this.loading.set(false); }
    });
  }
}
