package ph.easybus.app.utils;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.app.adapters.NumbersAdapter;
import ph.easybus.ebbusseatplan.adapters.SeatsAdapter;
import ph.easybus.ebbusseatplan.layoutmanagers.SpannedGridLayoutManager;
import ph.easybus.ebbusseatplan.models.GridSeat;

public class RecyclerViewBindingUtil {

    @BindingAdapter("numbers")
    public static void setNumbers(RecyclerView recyclerView, ArrayList<Integer> numbers) {
        if (numbers != null) {
            recyclerView.setLayoutManager(new SpannedGridLayoutManager(recyclerView.getContext(),
                    60, 10, 5,
                    position -> new SpannedGridLayoutManager.SpanInfo(1, 1, 1, 1)));
            //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(new NumbersAdapter(recyclerView.getContext(), numbers));
        }
    }

    @BindingAdapter("seats")
    public static void setSeats(RecyclerView recyclerView, ArrayList<GridSeat> seats) {
        if (seats != null) {
            //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(new SeatsAdapter(recyclerView.getContext(), seats));
        }
    }
}
