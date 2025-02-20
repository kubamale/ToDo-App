import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Task, TaskDto} from "../models/Tasks";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient) {
  }

  getTasks(date: Date): Observable<Task[]> {
    return this.http.get<Task[]>("http://localhost:8080/api/v1/tasks", {params: {date: date.toLocaleDateString('en-CA')}})
  }

  deleteTask(id: string): Observable<void> {
    return this.http.delete<void>("http://localhost:8080/api/v1/tasks/" + id);
  }

  markTaskAsCompleted(id: string): Observable<void> {
    return this.http.patch<void>("http://localhost:8080/api/v1/tasks/" + id, {})
  }

  createTask(task:TaskDto): Observable<Task> {
    return this.http.post<Task>("http://localhost:8080/api/v1/tasks", task)
  }
}
