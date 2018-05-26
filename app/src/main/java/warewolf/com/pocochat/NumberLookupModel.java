package warewolf.com.pocochat;

public class NumberLookupModel {
    private String id;
    private String address;
    private String dateSent;
    private String dateReceived;

    public NumberLookupModel(String _id, String _address, String _dateSent, String _dateReceived) {
        this.id = _id;
        this.address = _address;
        this.dateSent = _dateSent;
        this.dateReceived = _dateReceived;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDateSent() {
        return dateSent;
    }

    public String getDateReceived() {
        return dateReceived;
    }

}
