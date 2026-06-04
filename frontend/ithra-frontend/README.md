# إثراء | Ithra — Frontend

An Angular 21 article-publishing platform frontend.

## Prerequisites

- **Node.js** 25.x
- **Angular CLI** 21.x — `npm install -g @angular/cli`

## Getting Started

```bash
npm install
ng serve
```

App runs at **http://localhost:4200**

> The backend must be running on **port 8080** before making API calls.

## Change API Base URL

Edit `src/environments/environment.ts`:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'   // ← change this
};
```

## Dark Mode

Click the **🌙 / ☀️** button in the navbar to toggle dark/light mode. Preference is saved in `localStorage` and restored on next visit.

## Project Structure

```
src/app/
  guards/          # authGuard, adminGuard
  interceptors/    # JWT Bearer token + 401 handler
  models/          # TypeScript interfaces
  pages/           # All route components
    auth/          # login, register, verify-email, forgot/reset-password
    home/
    articles/      # list + detail
    categories/
    dashboard/     # my articles, article form
    admin/         # overview, users, categories, articles
    profile/
  services/        # AuthService, ArticleService, CategoryService, …
  shared/          # Navbar, Footer, ArticleCard, Pagination, Alert, …
```

## Key Tech

| Package | Purpose |
|---|---|
| Angular 21 (Standalone + Signals) | Framework |
| Bootstrap 5 | UI / responsive layout |
| CKEditor 5 | Rich-text article editor |
| Chart.js + ng2-charts | Admin analytics charts |
| Angular HTTP Interceptor | Auth token injection |

## Build

```bash
ng build
```
