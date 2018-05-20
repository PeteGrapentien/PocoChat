package warewolf.com.pocochat;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class InboxReceiver extends BroadcastReceiver{
    private ArrayList<InboxCard> inboxCards = new ArrayList<>();

    private String[] contactRawDataProjection = new String[]{
            ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

    private String[] conversationsProjection = new String[]{
            Telephony.Sms.Conversations.PERSON, //Returns person's _ID
            Telephony.Sms.Conversations.ADDRESS
    };

    public InboxReceiver(){ }

    public InboxReceiver(Context context){

        /*
            Sms.Inbox.Conversations was not working like the rest because the URI access by
            CONTENT_URI was wrong. The correct URI can be passed in as:
             Uri.parse("content://mms-sms/conversations")
         */

        ContentResolver contentResolver = context.getContentResolver();

        //Conversation Lookup
        Cursor conversationCursor = contentResolver.query(Uri.parse("content://mms-sms/conversations"),
                conversationsProjection,
                null,
                null,
                null);

        HashMap<String, String> conversationHash = new HashMap<>();
        if(conversationCursor != null){
            while(conversationCursor.moveToNext()){
                conversationHash.put(conversationCursor.getString(0), conversationCursor.getString(1));
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
                String number = conversationHash.get(contactRawDataCursor.getString(0));
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
