package com.example.contactslg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class TestCuAdapter extends RecyclerView.Adapter<TestCuAdapter.MyViewHolder> {

    Context context;
    ArrayList<CommunityUnit> arrayList;
    OnListItemClick onListItemClick;

    public TestCuAdapter(Context context, ArrayList<CommunityUnit> arrayList, OnListItemClick onListItemClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.onListItemClick = onListItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cu_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvCuCard.setText(arrayList.get(position).getCuName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCuCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCuCard = itemView.findViewById(R.id.textViewCuCard);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onListItemClick.onItemClick(v, getAdapterPosition());

        }
    }

    public interface OnListItemClick{

        void onItemClick(View view, int position);
    }
}
