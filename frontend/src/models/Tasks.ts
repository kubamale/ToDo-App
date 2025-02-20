
export interface Task{
  id: string,
  title: string,
  description: string,
  date: Date,
  status: TaskStatus,
}

export interface TaskDto {
  title: string,
  description: string,
  date: Date,
}

export enum TaskStatus {
  COMPLETED ='COMPLETED', INCOMPLETE='INCOMPLETE'
}
