import { Injectable, signal, effect } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  isDark = signal<boolean>(this.loadFromStorage());

  constructor() {
    effect(() => {
      const dark = this.isDark();
      document.body.classList.toggle('theme-dark', dark);
      document.body.classList.toggle('theme-light', !dark);
      localStorage.setItem('theme', dark ? 'dark' : 'light');
    });
    this.applyTheme();
  }

  toggle(): void {
    this.isDark.update(v => !v);
  }

  private loadFromStorage(): boolean {
    return localStorage.getItem('theme') === 'dark';
  }

  private applyTheme(): void {
    const dark = this.isDark();
    document.body.classList.toggle('theme-dark', dark);
    document.body.classList.toggle('theme-light', !dark);
  }
}
