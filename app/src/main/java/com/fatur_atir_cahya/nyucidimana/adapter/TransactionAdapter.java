package com.fatur_atir_cahya.nyucidimana.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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
    View viewInstance;

    public TransactionAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        viewInstance = view;
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);

        holder.code.setText(transaction.getTransactionCode());
        holder.startDate.setText(transaction.getStartDate());
        holder.priceWeight.setText(transaction.getPrice() + " - " + transaction.getWeight());

        String status = transaction.getStatus();
        if(status.equals("0")) {
            holder.status.setText("Pending");
            holder.status.setBackground(holder.itemView.getResources().getDrawable(R.drawable.status_pending));
            holder.status.setTextColor(holder.itemView.getResources().getColor(R.color.colorWhite));
        } else if(status.equals("1")) {
            holder.status.setText("Diproses");
            holder.status.setBackground(holder.itemView.getResources().getDrawable(R.drawable.status_progress));
            holder.status.setTextColor(holder.itemView.getResources().getColor(R.color.colorWhite));
        } else if(status.equals("2")) {
            holder.status.setText("Selesai");
            holder.status.setBackground(holder.itemView.getResources().getDrawable(R.drawable.status_done));
            holder.status.setTextColor(holder.itemView.getResources().getColor(R.color.colorWhite));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String className = view.getContext().getClass().getSimpleName();
                if(className.equals(OwnerDashboardActivity.class.getSimpleName())) {
                    Intent detailIntent = new Intent(view.getContext(), OwnerTransactionDetailActivity.class);
                    detailIntent.putExtra("transaction", transaction);
                    view.getContext().startActivity(detailIntent);
                } else if(className.equals(UserDashboardActivity.class.getSimpleName())) {
                    Intent detailIntent = new Intent(view.getContext(), UserTransactionDetailActivity.class);
                    detailIntent.putExtra("transaction", transaction);
                    view.getContext().startActivity(detailIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView code, startDate, priceWeight, status;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.transaction_code);
            startDate = itemView.findViewById(R.id.transaction_start_date);
            priceWeight = itemView.findViewById(R.id.transaction_price_weight);
            status = itemView.findViewById(R.id.transaction_status);
        }
    }

}
