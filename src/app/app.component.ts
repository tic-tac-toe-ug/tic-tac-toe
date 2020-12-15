import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'Demo';
  data: Data = new Data("test", "test")

  constructor(private http: HttpClient) {
    http.get('resource').subscribe((data: any) => this.data = new Data(data['id'], data['content']));
  }
}

export class Data {
  id: String
  content: String

  constructor(id: String, content: String) {
    this.id = id;
    this.content = content;
  }
}
