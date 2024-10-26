import java.util.ArrayList;
import java.util.List;

public class Project {
    public String name;
    public String description;
    public String start_date;
    public String end_date;
    public List<Task> tasks;
    public List<TeamMember> team_members;

    public Project(String n, String d, String sd, String ed) {
        this.name = n;
        this.description = d;
        this.start_date = sd;
        this.end_date = ed;
        this.tasks = new ArrayList<>();
        this.team_members = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    public void addTeamMember(TeamMember member) {
        team_members.add(member);
    }

    public void removeTeamMember(TeamMember member) {
        team_members.remove(member);
    }
}
