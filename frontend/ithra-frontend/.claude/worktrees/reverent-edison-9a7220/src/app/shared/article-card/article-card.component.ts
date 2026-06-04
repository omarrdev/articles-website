import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Article } from '../../models';

@Component({
  selector: 'app-article-card',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.scss'
})
export class ArticleCardComponent {
  @Input() article!: Article;

  get initials(): string {
    return this.article.author.username.slice(0, 2).toUpperCase();
  }
}
