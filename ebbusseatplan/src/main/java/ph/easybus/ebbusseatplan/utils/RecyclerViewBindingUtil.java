package ph.easybus.ebbusseatplan.utils;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.adapters.BusSeatPlanAdapter;
import ph.easybus.ebbusseatplan.models.BusSeat;

public class RecyclerViewBindingUtil {

    @BindingAdapter(value = { "columns", "busSeats" }, requireAll = false)
    public static void setBusColumns(RecyclerView recyclerView, int columns, ArrayList<BusSeat> busSeats) {
        if (columns > 0) {
            // Because of IndexOutOfBoundsException: Inconsistency detected
            // Possible fix
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), columns) {
                        public boolean supportsPredictiveItemAnimations() {
                            return false;
                        }
                    });
        }

        if (busSeats != null) {
            recyclerView.setAdapter(new BusSeatPlanAdapter(recyclerView.getContext(), busSeats));
        }
    }
}
