import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  template: `
    <div class="d-flex flex-column min-vh-100" style="background-color:var(--bg-primary);color:var(--text-primary);">
      <app-navbar />
      <main class="flex-grow-1">
        <router-outlet />
      </main>
      <app-footer />
    </div>
  `
})
export class App {}
