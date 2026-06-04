import { Component, inject, signal, HostListener } from '@angular/core';
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

  isOpen = signal(false);

  get initials(): string {
    const username = this.auth.currentUser()?.username;
    if (!username) return '';
    return username.slice(0, 2).toUpperCase();
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.isOpen.update(v => !v);
  }

  @HostListener('document:click')
  closeDropdown(): void {
    this.isOpen.set(false);
  }

  logout(): void {
    this.auth.logout();
  }
}
