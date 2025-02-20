import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Task, TaskDto} from "../../models/Tasks";

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './task-form.component.html',
  styleUrl: './task-form.component.css'
})
export class TaskFormComponent implements OnInit {
  @Input() task!: Task
  @Input() date!: Date
  @Output() onSubmit = new EventEmitter<TaskDto>();
  @Output() onCancel = new EventEmitter<void>();

  taskForm!: FormGroup;

  ngOnInit(): void {
    this.initializeForm()
  }

  initializeForm(){
    this.taskForm = new FormGroup({
      title: new FormControl(this.task ? this.task.title : '', Validators.required),
      description: new FormControl(this.task ? this.task.description : '', Validators.required),
      date: new FormControl(this.task ? this.task.date : (this.date ? this.date : new Date()), Validators.required),
    })
  }

  save(){
    let taskDto: TaskDto = {
      title: this.taskForm.value.title,
      description: this.taskForm.value.description,
      date: this.taskForm.value.date
    }

    this.onSubmit.emit(taskDto);
  }

  cancel() {
    this.onCancel.emit();
  }
}
