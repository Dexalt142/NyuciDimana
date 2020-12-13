package com.fatur_atir_cahya.nyucidimana.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatur_atir_cahya.nyucidimana.OwnerDashboardActivity;
import com.fatur_atir_cahya.nyucidimana.OwnerTransactionDetailActivity;
import com.fatur_atir_cahya.nyucidimana.R;
import com.fatur_atir_cahya.nyucidimana.UserDashboardActivity;
import com.fatur_atir_cahya.nyucidimana.UserTransactionDetailActivity;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;

import java.util.ArrayList;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = view.getContext().getClass().getSimpleName();
                if(className.equals(OwnerDashboardActivity.class.getSimpleName())) {
                    Intent detailIntent = new Intent(view.getContext(), OwnerTransactionDetailActivity.class);
                    detailIntent.putExtra("transaction", transaction);
                    view.getContext().startActivity(detailIntent);
                } else if(className.equals(UserDashboardActivity.class.getSimpleName())) {
                    view.getContext().startActivity(new Intent(view.getContext(), UserTransactionDetailActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView code, startDate, price, weight;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.transaction_code);
            startDate = itemView.findViewById(R.id.transaction_start_date);
            price = itemView.findViewById(R.id.transaction_price);
            weight = itemView.findViewById(R.id.transaction_weight);
        }
    }

}
