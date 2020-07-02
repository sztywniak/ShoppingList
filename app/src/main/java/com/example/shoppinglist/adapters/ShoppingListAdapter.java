package com.example.shoppinglist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.models.ShoppingList;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListAdapter.ShoppingListHolder> {

    private OnItemClickListener listener;

    public ShoppingListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ShoppingList> DIFF_CALLBACK = new DiffUtil.ItemCallback<ShoppingList>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ShoppingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListHolder holder, int position) {
        ShoppingList currentList = getItem(position);
        holder.textViewName.setText(currentList.getName());
        holder.textViewDate.setText(currentList.getDate().substring(0, 10));
    }

    public ShoppingList getShoppingListAt(int position) {
        return getItem(position);
    }

    public class ShoppingListHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewDate;

        public ShoppingListHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewDate = itemView.findViewById(R.id.textView_date);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION)
                    listener.onItemClick(getItem(position));
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ShoppingList shoppingList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
