import java.util.ArrayList;
import Task;
import TeamMember;

public class Project {
    public String name;
    public String description;
    public String start_date;
    public String end_date;
    public ArrayList<Task> tasks;
    public ArrayList<TeamMember> team_members;

    public Project(String n, String d, String sd, String ed) {
        this.name = n;
        this.description = d;
        this.start_date = sd;
        this.end_date = ed;
        this.tasks = new ArrayList<Task>();
        this.team_members = new ArrayList<TeamMember>();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    public void addTeamMember(TeamMember member) {
        this.team_members.add(member);
    }

    public void removeTeamMember(TeamMember member) {
        this.team_members.remove(member);
    }
}
