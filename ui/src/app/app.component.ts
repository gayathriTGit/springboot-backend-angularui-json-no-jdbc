import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NewsListComponent } from './components/news-list/news-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NewsListComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ui';
}
