package warewolf.com.pocochat;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.HashMap;

public class InboxReceiver extends BroadcastReceiver{
    private ArrayList<InboxCard> inboxCards = new ArrayList<>();

    private String[] smsColumnProjection = new String[]{
            Telephony.Sms.Inbox.PERSON,
            Telephony.Sms.Inbox.ADDRESS };

    private String[] contactRawDataProjection = new String[]{
            ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

    public InboxReceiver(){ }

    public InboxReceiver(Context context){

        //SMS Inbox Lookup
        ContentResolver contentResolver = context.getContentResolver();
        Cursor smsCursor = contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI,
                smsColumnProjection,
                null,
                null,
                null);

        HashMap<String, String> smsNumberHash = new HashMap<>();
        if(smsCursor != null){
            while(smsCursor.moveToNext()){
                smsNumberHash.put(smsCursor.getString(0), smsCursor.getString(1));
            }
        }

        //Raw Data Lookup
        Cursor contactRawDataCursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                contactRawDataProjection,
                null,
                null,
                null);

        HashMap<String, String> nameNumberHash = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if(contactRawDataCursor != null){
            while(contactRawDataCursor.moveToNext()){
                String number = smsNumberHash.get(contactRawDataCursor.getString(0));
                String name = contactRawDataCursor.getString(1);
                if(name != null && number != null){
                    inboxCards.add(new InboxCard(name, number));
                }
            }
        }
    }

    public ArrayList<InboxCard> getInboxCards() {
        return inboxCards;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
