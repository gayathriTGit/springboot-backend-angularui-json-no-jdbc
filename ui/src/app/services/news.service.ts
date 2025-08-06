import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiUrl = 'http://localhost:8080/news';

  constructor(private http: HttpClient) { }

  async getNews(): Promise<any> {
    return await firstValueFrom(this.http.get<any>(this.apiUrl));
  }
}