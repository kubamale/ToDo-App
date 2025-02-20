
export interface Task{
  id: string,
  title: string,
  description: string,
  date: Date,
  status: TaskStatus,
}

export enum TaskStatus {
  COMPLETED ='COMPLETED', INCOMPLETE='INCOMPLETE'
}
