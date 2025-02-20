import {Component, OnInit} from '@angular/core';
import {TaskComponent} from "../task/task.component";
import {Task, TaskDto, TaskStatus} from "../../models/Tasks";
import {faAngleLeft, faAngleRight, faCoffee, faPlus} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {TaskService} from "../task.service";
import {TaskFormComponent} from "../task-form/task-form.component";

@Component({
  selector: 'app-tasks-overview',
  standalone: true,
  imports: [
    TaskComponent,
    FaIconComponent,
    TaskFormComponent
  ],
  templateUrl: './tasks-overview.component.html',
  styleUrl: './tasks-overview.component.css'
})
export class TasksOverviewComponent implements OnInit {
  date = new Date();
  oneDayMillis = 24 * 60 * 60 * 1000;
  isCreateWindowVisible: boolean = false;
  days: number[] = this.getDays();
  tasks: Task[] = []

  constructor(private taskService: TaskService) {
  }


  getDays(): number[] {
    let millis = this.date.getTime();
    let days: number[] = []
    days.push(new Date(millis - 2 * this.oneDayMillis).getDate(), new Date(millis - this.oneDayMillis).getDate(), this.date.getDate(), new Date(millis + this.oneDayMillis).getDate(), new Date(millis + 2 * this.oneDayMillis).getDate());
    return days
  }

  nextDay() {
    let millis = this.date.getTime();
    this.date = new Date(millis + this.oneDayMillis);
    this.days = this.getDays()

    this.loadTasks()
  }

  previousDay() {
    let millis = this.date.getTime();
    this.date = new Date(millis - this.oneDayMillis);
    this.days = this.getDays()
    this.loadTasks()
  }

  todayClass(day: number): string {
    if (this.date.getDate() == day) {
      return "today"
    }
    return ""
  }

  protected readonly faCoffee = faCoffee;
  protected readonly faAngleRight = faAngleRight;
  protected readonly faAngleLeft = faAngleLeft;

  ngOnInit(): void {
    this.loadTasks()
  }

  loadTasks(): void {
    this.taskService.getTasks(this.date).subscribe(tasks => {
      this.tasks = tasks;
    });
  }

  delete(id: string): void {
    this.taskService.deleteTask(id).subscribe(() => this.tasks = this.tasks.filter(task => task.id != id));
  }

  markAsCompleted(id: string): void {
    this.taskService.markTaskAsCompleted(id).subscribe(() => {
      let task = this.tasks.find(task =>
        task.id == id
      )
      if (task) {
        task.status = TaskStatus.COMPLETED;
      }
    });
  }

  showCreateWindow(): void {
    this.isCreateWindowVisible = true;
  }

  closeTaskWindow(): void {
    this.isCreateWindowVisible = false;
  }

  createTask(task: TaskDto): void {
    this.taskService.createTask(task).subscribe(task => {
      if (this.formatDate(task.date) == this.formatDate(this.date)) {
        this.tasks.push(task);
      }
    })

    this.closeTaskWindow();
  }

  formatDate(date: Date | string): string {
    const d = new Date(date);
    return d.toISOString().split('T')[0];
  }

  protected readonly faPlus = faPlus;
}
