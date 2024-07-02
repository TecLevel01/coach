package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.oli.coach.R;

import models.chatMessageModel;
import models.util;

public class chatAdaptor extends FirestoreRecyclerAdapter<chatMessageModel, chatAdaptor.chatViewHolder> {
    Context context;


    public chatAdaptor(@NonNull FirestoreRecyclerOptions<chatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull chatAdaptor.chatViewHolder holder, int position, @NonNull chatMessageModel model) {
        if (model.getSenderId().equals(util.currentUserId())) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMessage.setText(model.getMessage());
        } else {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftMessage.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public chatAdaptor.chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new chatViewHolder(view);
    }

    public static class chatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout, rightLayout;
        TextView leftMessage, rightMessage;

        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_chat_layout);
            rightLayout = itemView.findViewById(R.id.right_chat_layout);
            leftMessage = itemView.findViewById(R.id.receiverMessage);
            rightMessage = itemView.findViewById(R.id.senderMessage);
        }
    }
}

