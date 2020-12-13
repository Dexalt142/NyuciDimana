package com.fatur_atir_cahya.nyucidimana.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatur_atir_cahya.nyucidimana.R;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ListViewHolder> {

    ArrayList<Transaction> transactions;

    public TransactionAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);

        holder.code.setText(transaction.getTransactionCode());
        holder.startDate.setText(transaction.getStartDate());
        holder.price.setText(String.valueOf(transaction.getPrice()));
        holder.weight.setText(String.valueOf(transaction.getWeight()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView code, startDate, endDate, price, weight;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.transaction_code);
            startDate = itemView.findViewById(R.id.transaction_start_date);
            price = itemView.findViewById(R.id.transaction_price);
            weight = itemView.findViewById(R.id.transaction_weight);
        }
    }

}
