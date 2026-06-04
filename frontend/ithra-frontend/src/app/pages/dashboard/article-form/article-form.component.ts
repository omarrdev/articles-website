import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { CategoryService } from '../../../services/category.service';
import { TagService } from '../../../services/tag.service';
import { UploadService } from '../../../services/upload.service';
import { Category, Tag } from '../../../models';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import {
  ClassicEditor,
  Bold,
  Essentials,
  Italic,
  Link,
  Paragraph,
  Heading,
  List,
  BlockQuote,
  Undo
} from 'ckeditor5';

@Component({
  selector: 'app-article-form',
  standalone: true,
  imports: [FormsModule, RouterLink, CKEditorModule],
  templateUrl: './article-form.component.html'
})
export class ArticleFormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private articleService = inject(ArticleService);
  private categoryService = inject(CategoryService);
  private tagService = inject(TagService);
  private uploadService = inject(UploadService);

  Editor = ClassicEditor;
  editorConfig = {
    licenseKey: 'GPL',
    plugins: [Essentials, Bold, Italic, Link, Paragraph, Heading, List, BlockQuote, Undo],
    toolbar: ['undo', 'redo', '|', 'heading', '|', 'bold', 'italic', 'link', '|', 'bulletedList', 'numberedList', '|', 'blockQuote']
  };

  editId: number | null = null;
  isEdit = false;
  categories = signal<Category[]>([]);
  tags = signal<Tag[]>([]);
  loading = signal(false);
  error = signal('');
  coverPreview = signal('');

  form = {
    title: '',
    summary: '',
    content: '',
    categoryId: null as number | null,
    tagIds: [] as number[],
    coverImageUrl: ''
  };

  get slug(): string {
    return this.form.title.toLowerCase().replace(/\s+/g, '-').replace(/[^a-z0-9-]/g, '');
  }

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(c => this.categories.set(c));
    this.tagService.getAll().subscribe(t => this.tags.set(t));

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = +id;
      this.articleService.getForEdit(+id).subscribe(a => {
        this.form.title = a.title;
        this.form.summary = a.summary;
        this.form.content = a.content;
        this.form.categoryId = a.category?.id ?? null;
        this.form.tagIds = a.tags?.map(t => t.id) ?? [];
        this.form.coverImageUrl = a.coverImageUrl;
        this.coverPreview.set(a.coverImageUrl);
      });
    }
  }

  toggleTag(id: number): void {
    const ids = this.form.tagIds;
    const idx = ids.indexOf(id);
    if (idx === -1) ids.push(id); else ids.splice(idx, 1);
  }

  isTagSelected(id: number): boolean { return this.form.tagIds.includes(id); }

  onCoverChange(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.coverPreview.set(URL.createObjectURL(file));
    this.uploadService.uploadImage(file).subscribe(r => this.form.coverImageUrl = r.url);
  }

  save(publish: boolean): void {
    this.loading.set(true);
    this.error.set('');
    const payload: Partial<import('../../../models').Article> = { ...this.form, status: publish ? 'PUBLISHED' : 'DRAFT' };
    const req = this.editId
      ? this.articleService.update(this.editId, payload)
      : this.articleService.create(payload);
    req.subscribe({
      next: () => this.router.navigate(['/dashboard/articles']),
      error: (e) => { this.error.set(e.error?.message || 'Failed to save'); this.loading.set(false); }
    });
  }
}
