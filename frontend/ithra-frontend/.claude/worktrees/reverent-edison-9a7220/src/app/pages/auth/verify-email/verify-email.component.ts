import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="min-vh-100 d-flex align-items-center justify-content-center" style="background-color:var(--bg-secondary);">
      <div class="card shadow-sm p-4 text-center" style="max-width:420px;background-color:var(--card-bg);border-color:var(--border-color);">
        @if (loading()) {
          <div class="spinner-border text-primary mb-3"></div>
          <p style="color:var(--text-primary);">Verifying your email...</p>
        } @else if (success()) {
          <div class="text-success fs-1">✅</div>
          <h4 style="color:var(--text-primary);">Email Verified!</h4>
          <p style="color:var(--text-secondary);">Your account is now active.</p>
          <a routerLink="/login" class="btn btn-primary">Login</a>
        } @else {
          <div class="text-danger fs-1">❌</div>
          <h4 style="color:var(--text-primary);">Verification Failed</h4>
          <p style="color:var(--text-secondary);">{{ error() }}</p>
          <a routerLink="/login" class="btn btn-outline-primary">Back to Login</a>
        }
      </div>
    </div>
  `
})
export class VerifyEmailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private auth = inject(AuthService);
  loading = signal(true);
  success = signal(false);
  error = signal('');

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token') || '';
    this.auth.verifyEmail(token).subscribe({
      next: () => { this.loading.set(false); this.success.set(true); },
      error: (e) => { this.loading.set(false); this.error.set(e.error?.message || 'Invalid or expired token'); }
    });
  }
}
