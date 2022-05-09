package com.example.ednevnik.ui.addUsersToGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ednevnik.databinding.FragmentGroupChooseBinding;
import com.example.ednevnik.ui.addUsersToGroup.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.ednevnik.ui.addUsersToGroup.databinding.FragmentGroupChooseBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyGroupRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentGroupChooseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final RadioButton mRadioButton;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentGroupChooseBinding binding) {
            super(binding.getRoot());
            mIdView = binding.textView13;
            mRadioButton = binding.radioButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}