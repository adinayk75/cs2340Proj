public class Main {
    public static void main(String[] args) {
        // Create a Task object
        Task task = new Task();
        task.title = "Complete Assignment";
        task.description = "Finish the SOLID principles assignment";
        task.dueDate = "2023-10-10";
        task.priority = "High";

        // Create a RecurringTask object
        RecurringTask recurringTask = new RecurringTask();
        recurringTask.title = "Weekly Meeting";
        recurringTask.description = "Attend the weekly team meeting";
        recurringTask.dueDate = "Every Monday";
        recurringTask.priority = "Medium";
        recurringTask.recurrence_period = "Weekly";

        // Create a HighPriorityTask object
        HighPriorityTask highPriorityTask = new HighPriorityTask();
        highPriorityTask.title = "Urgent Bug Fix";
        highPriorityTask.description = "Fix the critical bug in production";
        highPriorityTask.dueDate = "2023-10-05";
        highPriorityTask.priority = "High";

        // Create a CompletedStatus object and update task status
        CompletedStatus completedStatus = new CompletedStatus();
        completedStatus.updateStatus(task);

        // Print task details
        System.out.println("Task: " + task.title + ", Status: " + task.status);
        System.out.println("Recurring Task: " + recurringTask.title + ", Recurrence: " + recurringTask.recurrence_period);
        System.out.println("High Priority Task: " + highPriorityTask.title + ", Priority: " + highPriorityTask.priority);
    }
}