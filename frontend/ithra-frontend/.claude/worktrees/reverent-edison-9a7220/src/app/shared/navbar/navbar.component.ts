import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  auth = inject(AuthService);
  theme = inject(ThemeService);

  get initials(): string {
    const user = this.auth.currentUser();
    if (!user) return '';
    return user.username.slice(0, 2).toUpperCase();
  }

  logout(): void {
    this.auth.logout();
  }
}
