package ph.easybus.ebbusseatplan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.databinding.ItemGridSeatBinding;
import ph.easybus.ebbusseatplan.models.GridSeat;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<GridSeat> seats;

    public SeatsAdapter(Context context, ArrayList<GridSeat> seats) {
        this.context = context;
        this.seats = seats;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemGridSeatBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_grid_seat, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setSeat(seats.get(position));
    }

    @Override
    public int getItemCount() { return seats.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemGridSeatBinding binding;

        public MyViewHolder(ItemGridSeatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
