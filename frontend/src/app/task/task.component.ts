import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Task, TaskStatus} from "../../models/Tasks";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faCheck, faTrash} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [
    FaIconComponent
  ],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent {

  @Input() task!: Task
  @Output() onDelete = new EventEmitter<string>();
  @Output() onMarkAsCompleted = new EventEmitter<string>();
  protected readonly faCheck = faCheck;
  protected readonly faTrash = faTrash;

  delete() {
    this.onDelete.emit(this.task.id);
  }

  complete() {
    this.onMarkAsCompleted.emit(this.task.id);
    this.task.status = TaskStatus.COMPLETED;
  }

  protected readonly TaskStatus = TaskStatus;
}
