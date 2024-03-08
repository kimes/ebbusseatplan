package ph.easybus.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.app.R;
import ph.easybus.ebbusseatplan.app.databinding.ItemNumberBinding;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Integer> numbers;

    public NumbersAdapter(Context context, ArrayList<Integer> numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemNumberBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_number, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setNumber(numbers.get(position));
    }

    @Override
    public int getItemCount() { return numbers.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemNumberBinding binding;

        public MyViewHolder(ItemNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
