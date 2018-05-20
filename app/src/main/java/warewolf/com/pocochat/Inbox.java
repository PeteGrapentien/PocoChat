package warewolf.com.pocochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Inbox extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private InboxReceiver inboxReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inbox);

        recyclerView = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Inbox.this));
        inboxReceiver = new InboxReceiver(this);

        adapter = new InboxCardAdapter(inboxReceiver.getInboxCardViewModels(),this);
        recyclerView.setAdapter(adapter);
    }
}
