package ph.easybus.ebbusseatplan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.databinding.ItemBusSeatBinding;
import ph.easybus.ebbusseatplan.models.BusSeat;

public class BusSeatPlanAdapter extends RecyclerView.Adapter<BusSeatPlanAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BusSeat> busSeats;

    public BusSeatPlanAdapter(Context context, ArrayList<BusSeat> busSeats) {
        this.context = context;
        this.busSeats = busSeats;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemBusSeatBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_bus_seat, viewGroup, false);
        return new MyViewHolder(binding);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        BusSeat seat = busSeats.get(position);
        holder.binding.setSeatModel(seat);
    }

    public int getItemCount() {
        return busSeats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemBusSeatBinding binding;
        public MyViewHolder(ItemBusSeatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
