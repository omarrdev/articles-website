import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  template: `
    <footer class="py-4 mt-auto" style="background-color: var(--bg-secondary); border-top: 1px solid var(--border-color);">
      <div class="container text-center">
        <p class="mb-1 fw-semibold" style="color: var(--accent);">إثراء | Ithra</p>
        <p class="mb-0 small" style="color: var(--text-secondary);">
          &copy; {{ year }} Ithra. All rights reserved.
          &nbsp;|&nbsp;
          <a routerLink="/" style="color: var(--accent); text-decoration: none;">Home</a>
          &nbsp;|&nbsp;
          <a routerLink="/articles" style="color: var(--accent); text-decoration: none;">Articles</a>
        </p>
      </div>
    </footer>
  `
})
export class FooterComponent {
  year = new Date().getFullYear();
}
