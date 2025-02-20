import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TasksOverviewComponent} from "./tasks-overview/tasks-overview.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TasksOverviewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ToDo';
}
