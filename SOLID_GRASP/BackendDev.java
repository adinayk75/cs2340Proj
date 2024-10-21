public class TeamMember {
    private String name;
    private String email;
}

interface Backend{
    void apis();
}

interface Frontend{
    void divs();
}

public class BackendDev extends TeamMember implements Backend {
    public void apis() {
        System.out.print("created API");
    }    
}

public class FrontendDev extends TeamMember implements Frontend {
    public void divs() {
        System.out.print("created div");
    }    
}

