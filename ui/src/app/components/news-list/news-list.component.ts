import { Component, OnInit } from '@angular/core';
import { NewsService } from '../../services/news.service';
import { News, NewsResponse } from '../../models/news.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-news-list',
  imports: [CommonModule],
  templateUrl: './news-list.component.html',
  styleUrl: './news-list.component.css'
})
export class NewsListComponent implements OnInit {
  news: News[] = [];
  loading = false;
  error: string | null = null;
  total = 0;

  constructor(private newsService: NewsService) {}

  ngOnInit(): void {
    this.loadNews();
  }

  private async loadNews(): Promise<void> {
    this.loading = true;
    this.error = null;
    
    try {
      const response: NewsResponse = await this.newsService.getNews();
      this.news = response.articles;
      this.total = response.total;
      this.loading = false;
    } catch (err) {
      this.error = 'Failed to load news articles';
      this.loading = false;
      console.error('Error loading news:', err);
    }
  }
}