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

/*
  Sms.Inbox.Conversations was not working like the rest because the URI access by
  CONTENT_URI was wrong. The correct URI can be passed in as:
  Uri.parse("content://mms-sms/conversations")
*/

/*
    TODO: Figure out group texts
 */

public class InboxReceiver extends BroadcastReceiver{
    private ArrayList<InboxCardViewModel> inboxCardViewModels;
    private String[] conversationsProjection;
    private ContentResolver contentResolver;
    private ArrayList<NumberLookupModel> numberLookupModels;

    public InboxReceiver(){ }

    public InboxReceiver(Context context){
        inboxCardViewModels = new ArrayList<>();
        numberLookupModels = new ArrayList<>();
        conversationsProjection = createConversationsProjection();
        contentResolver = context.getContentResolver();

        Cursor numberCursor = lookupContactNumbers();
        addNamesAndNumbersToCards(numberCursor);

    }

    private String[] createConversationsProjection(){
        return new String[]{
                Telephony.Sms.Conversations.PERSON, //Returns person's _ID
                Telephony.Sms.Conversations.ADDRESS,
                Telephony.Sms.Conversations.DATE_SENT,
                Telephony.Sms.Conversations.DATE
        };
    }

    private void addNamesAndNumbersToCards(Cursor numberCursor){
        if(numberCursor != null)
        {
            while(numberCursor.moveToNext()) {
                String id = numberCursor.getString(0);
                String address = numberCursor.getString(1);
                String dateSent = numberCursor.getString(2);
                String dateReceived = numberCursor.getString(3);
                numberLookupModels.add(new NumberLookupModel(id, address, dateSent, dateReceived));
            }
        }
        numberCursor.moveToFirst();

        for (NumberLookupModel number: numberLookupModels) {
            if (number.getAddress() != null) {
                Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number.getAddress()));
                String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
                String contactName="";
                Cursor cursor= contentResolver.query(uri,projection,null,null,null);
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        contactName=cursor.getString(0);
                    }
                    cursor.close();
                }
                InboxCardViewModel card = new InboxCardViewModel(contactName, number.getAddress(), number.getDateSent(), number.getDateReceived());
                insertMostRecentInteraction(card);
            }
        }
    }

    private void insertMostRecentInteraction(InboxCardViewModel card){
        if(inboxCardViewModels.size() < 1) {
            inboxCardViewModels.add(card);
        }
        boolean insertedCard = false;
        for(int i = 0; i < inboxCardViewModels.size(); ++i){
            if(inboxCardViewModels.get(i).getMostRecentInteraction() > card.getMostRecentInteraction()){
                continue;
            } else {
                inboxCardViewModels.add(i, card);
                insertedCard = true;
                break;
            }
        }
        if(insertedCard == false) {
            inboxCardViewModels.add(card);
        }
    }

    private Cursor lookupContactNumbers(){
        Cursor conversationCursor = contentResolver.query(Uri.parse("content://mms-sms/conversations"),
                conversationsProjection,
                null,
                null,
                null);
        return conversationCursor;
    }

    public ArrayList<InboxCardViewModel> getInboxCardViewModels() {
        return inboxCardViewModels;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
