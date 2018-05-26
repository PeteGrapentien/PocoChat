package warewolf.com.pocochat;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InboxCardViewModel {
    private String name;
    private String number;
    private String dateSent;
    private String dateReceived;
    private Long mostRecentInteraction;

    public InboxCardViewModel(String _name, String _number, String _dateSent, String _dateReceived) {
        this.name = _name;
        this.number = _number;
        this.dateSent = _dateSent;
        this.dateReceived = _dateReceived;
        this.mostRecentInteraction = calculateMostRecentInteraction();

    }

    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public String getDateSent(){ return dateSent; }
    public String getDateReceived() { return dateReceived; }
    public Long getMostRecentInteraction(){ return mostRecentInteraction; }

    private Long calculateMostRecentInteraction() {
        Long dateSentNum = Long.valueOf(dateSent);
        Long dateReceivedNum = Long.valueOf(dateReceived);
        if(dateSentNum > dateReceivedNum){
            return dateSentNum;
        }
        return dateReceivedNum;
    }

}