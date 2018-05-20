package warewolf.com.pocochat;

public class InboxCardViewModel {
    private String heading;
    private String description;

    public String getHeading() {
        return heading;
    }

    public InboxCardViewModel(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
