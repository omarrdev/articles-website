import { Component, signal, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  isOpen = signal(false);
  isDarkMode = signal(false);

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.isOpen.update(v => !v);
  }

  toggleDarkMode(): void {
    this.isDarkMode.update(v => !v);
  }

  @HostListener('document:click')
  closeDropdown(): void {
    this.isOpen.set(false);
  }
}
