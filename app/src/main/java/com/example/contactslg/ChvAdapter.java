package com.example.contactslg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChvAdapter extends FirestoreRecyclerAdapter<CommunityUnit, ChvAdapter.MyViewHolder> {


    private OnItemClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChvAdapter(@NonNull FirestoreRecyclerOptions<CommunityUnit> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull CommunityUnit model) {

        holder.tvChvName.setText(model.getChvName());
        holder.tvChvPhone.setText(model.getChvPhone());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chv_card, parent, false);

        return new MyViewHolder(view);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvChvName, tvChvPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvChvName = itemView.findViewById(R.id.textViewChvName);
            tvChvPhone = itemView.findViewById(R.id.textViewChvPhone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){

                        listener.onItemClick(v, getSnapshots().getSnapshot(position), position);
                    }


                }
            });


        }
    }

    public interface OnItemClickListener{

        void onItemClick(View view, DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;

    }
}
