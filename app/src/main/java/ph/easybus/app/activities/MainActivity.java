package ph.easybus.app.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Comparator;

import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.app.viewmodels.MainActivityViewModel;
import ph.easybus.ebbusseatplan.app.R;
import ph.easybus.ebbusseatplan.app.databinding.ActivityMainBinding;
import ph.easybus.ebbusseatplan.layoutmanagers.SpannedGridLayoutManager;
import ph.easybus.ebbusseatplan.listeners.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity implements RecyclerTouchListener.OnItemClickListener {

    private static final GridSeat NB_NW = new GridSeat(-1, -1, "A"),
            NB_N = new GridSeat(0, -1, "A"),
            NB_NE = new GridSeat(1, -1, "A"),
            NB_W = new GridSeat(-1, 1, "A"),
            NB_E = new GridSeat(1, 0, "A"),
            NB_SW = new GridSeat(-1, 1, "A"),
            NB_S = new GridSeat(0, 1, "A"),
            NB_SE = new GridSeat(1, 1, "A");

    private static final int E_INDEX = 0, SE_INDEX = 1, S_INDEX = 2;

    private static final GridSeat[] SQUARE_DIR = { NB_E, NB_SE, NB_S };

    private String[] map = new String[] {
            "AA_UL",
            "AA_UL",
            "AA_UL",
            "AA_UL",
            "AA_RR",
            "AA_RR",
            "AA_RR",
            "AA_AA",
            "AA_UL",
            "AAAUL",
    };

    private int clickCounter = 0;

    private MainActivityViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //binding.rvNumber.setLayoutManager(new SpannedGridLayoutManager(this, 40));

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        viewModel.setNumbers(numbers);

        int rows = map.length, cols = map[0].length();

        binding.rvSeats.setLayoutManager(new SpannedGridLayoutManager(this, 45,
                cols, rows, position -> {
            ArrayList<GridSeat> seats = viewModel.getSeats().getValue();

            GridSeat seat = seats.get(position);
            return new SpannedGridLayoutManager.SpanInfo(seat.getX(), seat.getY(),
                    seat.getW(), seat.getH());
        }));

        RecyclerTouchListener.setup(this, binding.rvSeats, this);

        calculateSeatPlan();
    }

    @Override
    public void onClick(View view, int position) {
        GridSeat seat = viewModel.getSeats().getValue().get(position);
        if (seat.isSelectable()) {
            int mod = clickCounter % 2;
            if (mod == 0) {
                seat.setSelected(!seat.isSelected());
            } else {
                seat.setReserved(!seat.isReserved());
            }
            clickCounter++;
        }
    }

    @Override
    public void onLongClick(View view, int position) {}

    private void calculateSeatPlan() {
        new Thread(() -> {
        }).start();
    }
}
