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

public class RecurringTask extends Task{
    public String recurrence_period;

    public void completeTask() {
        return;
    }
}

public class HighPriorityTask extends Task{
    public void completeTask() {
        return;
    }
}

interface TaskStatus {
    public void updateStatus(Task task);
    public void incompleteTask();
}

public class CompletedStatus implements TaskStatus {
    public void updateStatus(Task task) {
        return;
    }
}

public class PendingStatus implements TaskStatus {
    public void updateStatus(Task task) {
        return;
    }
}