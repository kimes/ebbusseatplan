package ph.easybus.ebbusseatplan.utils;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.adapters.BusSeatPlanAdapter;
import ph.easybus.ebbusseatplan.adapters.SeatsAdapter;
import ph.easybus.ebbusseatplan.layoutmanagers.SpannedGridLayoutManager;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebbusseatplan.models.GridSeat;

public class RecyclerViewBindingUtil {

    @BindingAdapter(value = { "columns", "rows", "seats" })
    public static void setBusSeats(RecyclerView recyclerView, int columns, int rows,
                                              ArrayList<GridSeat> seats) {
        if (seats != null) {
            System.out.println("SETTING BUS SEATS");
            recyclerView.setLayoutManager(new SpannedGridLayoutManager(recyclerView.getContext(), 45,
                    columns, rows, position -> {
                GridSeat seat = seats.get(position);
                return new SpannedGridLayoutManager.SpanInfo(seat.getX(), seat.getY(),
                        seat.getW(), seat.getH());
            }));

            recyclerView.setAdapter(new SeatsAdapter(recyclerView.getContext(), seats));
        }
    }

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
