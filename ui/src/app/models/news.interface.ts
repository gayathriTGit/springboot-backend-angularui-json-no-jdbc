export interface News {
  author: string;
  title: string;
  content: string;
}

export interface NewsResponse {
  status: string;
  total: number;
  requestId: number;
  timestamp: string;
  articles: News[];
}