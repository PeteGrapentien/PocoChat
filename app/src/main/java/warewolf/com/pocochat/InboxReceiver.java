package warewolf.com.pocochat;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.HashMap;

        /*
            Sms.Inbox.Conversations was not working like the rest because the URI access by
            CONTENT_URI was wrong. The correct URI can be passed in as:
             Uri.parse("content://mms-sms/conversations")
         */

public class InboxReceiver extends BroadcastReceiver{
    private ArrayList<InboxCardViewModel> inboxCardViewModels = new ArrayList<>();

    private String[] contactRawDataProjection = new String[]{
            ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

    private String[] conversationsProjection = new String[]{
            Telephony.Sms.Conversations.PERSON, //Returns person's _ID
            Telephony.Sms.Conversations.ADDRESS
    };

    private ContentResolver contentResolver;

    public InboxReceiver(){ }

    public InboxReceiver(Context context){

        contentResolver = context.getContentResolver();

        Cursor nameCursor = lookupContactNames();
        Cursor numberCursor = lookupContactNumbers();
        HashMap<String, String> phoneNumberHash = createNumberHash(numberCursor);
        addNamesAndNumbersToCards(nameCursor, phoneNumberHash);

    }

    private void addNamesAndNumbersToCards(Cursor nameCursor, HashMap<String, String> phoneNumberHash){
        if(nameCursor != null){
            while(nameCursor.moveToNext()){
                String number = phoneNumberHash.get(nameCursor.getString(0));
                String name = nameCursor.getString(1);
                if(name != null && number != null){
                    inboxCardViewModels.add(new InboxCardViewModel(name, number));
                }
            }
        }
    }

    private Cursor lookupContactNames(){
        Cursor nameCursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                contactRawDataProjection,
                null,
                null,
                null);
        return nameCursor;
    }

    private Cursor lookupContactNumbers(){
        Cursor conversationCursor = contentResolver.query(Uri.parse("content://mms-sms/conversations"),
                conversationsProjection,
                null,
                null,
                null);
        return conversationCursor;
    }

    private HashMap<String, String> createNumberHash(Cursor conversationCursor){

        HashMap<String, String> conversationHash = new HashMap<>();
        if(conversationCursor != null){
            while(conversationCursor.moveToNext()){
                conversationHash.put(conversationCursor.getString(0), conversationCursor.getString(1));
            }
        }
        return conversationHash;
    }

    public ArrayList<InboxCardViewModel> getInboxCardViewModels() {
        return inboxCardViewModels;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
