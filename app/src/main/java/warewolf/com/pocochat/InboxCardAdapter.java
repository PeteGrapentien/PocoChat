package warewolf.com.pocochat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InboxCardAdapter extends RecyclerView.Adapter<InboxCardAdapter.CardViewHolder> {

    private List<InboxCardViewModel> conversationCards;
    private Context context;

    public InboxCardAdapter(List<InboxCardViewModel> conversationCards, Context context) {
        this.conversationCards = conversationCards;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_card, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, MainConversationActivity.class);
                //TO DO: Put extras here -- phone number, name, whatever
                //context.startActivity(intent);
            }
        });
        return new CardViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return conversationCards.size();
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        InboxCardViewModel card = conversationCards.get(position);
        holder.header.setText(card.getHeading());
        holder.description.setText(card.getDescription());
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        public TextView header;
        public TextView description;

        public CardViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.textViewHeading);
            description = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
