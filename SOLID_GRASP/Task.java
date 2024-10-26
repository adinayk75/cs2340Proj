public class Task {
    public String title;
    public String description;
    public String dueDate;
    public TaskStatus status;
    public String priority;

    public void completeTask() {
        return;
    }
}

interface TaskStatus {
    public void updateStatus(Task task);
    public void incompleteTask();
}