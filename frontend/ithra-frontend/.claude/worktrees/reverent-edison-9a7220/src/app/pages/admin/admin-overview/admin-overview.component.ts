import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { AdminStats, Article, ChartData } from '../../../models';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [RouterLink, BaseChartDirective],
  templateUrl: './admin-overview.component.html'
})
export class AdminOverviewComponent implements OnInit {
  private adminService = inject(AdminService);

  stats = signal<AdminStats | null>(null);
  topArticles = signal<Article[]>([]);

  lineData: ChartConfiguration<'line'>['data'] = { labels: [], datasets: [{ data: [], label: 'Articles Published', fill: true, tension: 0.4 }] };
  barData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [{ data: [], label: 'Views' }] };
  doughnutData: ChartConfiguration<'doughnut'>['data'] = { labels: [], datasets: [{ data: [] }] };

  lineOptions: ChartConfiguration<'line'>['options'] = { responsive: true };
  barOptions: ChartConfiguration<'bar'>['options'] = { responsive: true };
  doughnutOptions: ChartConfiguration<'doughnut'>['options'] = { responsive: true };

  ngOnInit(): void {
    this.adminService.getStats().subscribe(s => this.stats.set(s));
    this.adminService.getTopArticles().subscribe(a => this.topArticles.set(a));
    this.adminService.getArticlesPerMonth().subscribe(d => this.setChart('line', d));
    this.adminService.getViewsPerMonth().subscribe(d => this.setChart('bar', d));
    this.adminService.getArticlesPerCategory().subscribe(d => this.setChart('doughnut', d));
  }

  private setChart(type: 'line' | 'bar' | 'doughnut', d: ChartData): void {
    if (type === 'line') this.lineData = { labels: d.labels, datasets: [{ data: d.data, label: 'Articles Published', fill: true, tension: 0.4 }] };
    else if (type === 'bar') this.barData = { labels: d.labels, datasets: [{ data: d.data, label: 'Views' }] };
    else this.doughnutData = { labels: d.labels, datasets: [{ data: d.data }] };
  }
}
