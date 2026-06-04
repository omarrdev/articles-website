import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) },
  { path: 'articles', loadComponent: () => import('./pages/articles/article-list/article-list.component').then(m => m.ArticleListComponent) },
  { path: 'articles/:slug', loadComponent: () => import('./pages/articles/article-detail/article-detail.component').then(m => m.ArticleDetailComponent) },
  { path: 'categories', loadComponent: () => import('./pages/categories/categories-page/categories-page.component').then(m => m.CategoriesPageComponent) },
  { path: 'categories/:slug', loadComponent: () => import('./pages/categories/category-articles/category-articles.component').then(m => m.CategoryArticlesComponent) },
  { path: 'login', loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./pages/auth/register/register.component').then(m => m.RegisterComponent) },
  { path: 'verify-email', loadComponent: () => import('./pages/auth/verify-email/verify-email.component').then(m => m.VerifyEmailComponent) },
  { path: 'forgot-password', loadComponent: () => import('./pages/auth/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent) },
  { path: 'reset-password', loadComponent: () => import('./pages/auth/reset-password/reset-password.component').then(m => m.ResetPasswordComponent) },
  { path: 'profile', canActivate: [authGuard], loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent) },
  { path: 'dashboard', canActivate: [authGuard], loadComponent: () => import('./pages/dashboard/dashboard-home/dashboard-home.component').then(m => m.DashboardHomeComponent) },
  { path: 'dashboard/articles', canActivate: [authGuard], loadComponent: () => import('./pages/dashboard/my-articles/my-articles.component').then(m => m.MyArticlesComponent) },
  { path: 'dashboard/articles/new', canActivate: [authGuard], loadComponent: () => import('./pages/dashboard/article-form/article-form.component').then(m => m.ArticleFormComponent) },
  { path: 'dashboard/articles/:id/edit', canActivate: [authGuard], loadComponent: () => import('./pages/dashboard/article-form/article-form.component').then(m => m.ArticleFormComponent) },
  { path: 'admin', canActivate: [authGuard, adminGuard], loadComponent: () => import('./pages/admin/admin-overview/admin-overview.component').then(m => m.AdminOverviewComponent) },
  { path: 'admin/users', canActivate: [authGuard, adminGuard], loadComponent: () => import('./pages/admin/admin-users/admin-users.component').then(m => m.AdminUsersComponent) },
  { path: 'admin/categories', canActivate: [authGuard, adminGuard], loadComponent: () => import('./pages/admin/admin-categories/admin-categories.component').then(m => m.AdminCategoriesComponent) },
  { path: 'admin/articles', canActivate: [authGuard, adminGuard], loadComponent: () => import('./pages/admin/admin-articles/admin-articles.component').then(m => m.AdminArticlesComponent) },
  { path: '**', redirectTo: '' }
];
