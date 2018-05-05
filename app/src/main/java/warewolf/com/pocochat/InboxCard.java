package warewolf.com.pocochat;

public class InboxCard {
    private String heading;
    private String description;

    public String getHeading() {
        return heading;
    }

    public InboxCard(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
