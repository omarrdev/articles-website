export interface User {
  id: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'AUTHOR' | 'READER';
  isEmailVerified: boolean;
  createdAt: string;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  description: string;
  articleCount?: number;
}

export interface Tag {
  id: number;
  name: string;
  slug: string;
}

export interface Article {
  id: number;
  title: string;
  slug: string;
  summary: string;
  content: string;
  coverImageUrl: string;
  status: 'DRAFT' | 'PUBLISHED';
  viewCount: number;
  likeCount: number;
  author: User;
  category: Category;
  tags: Tag[];
  createdAt: string;
  publishedAt: string;
}

export interface Comment {
  id: number;
  content: string;
  author: User;
  createdAt: string;
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
}

export interface AdminStats {
  totalArticles: number;
  totalUsers: number;
  totalViews: number;
  totalLikes: number;
  totalComments: number;
}

export interface ChartData {
  labels: string[];
  data: number[];
}
