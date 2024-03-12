package ph.easybus.app.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.app.viewmodels.MainActivityViewModel;
import ph.easybus.ebbusseatplan.app.R;
import ph.easybus.ebbusseatplan.app.databinding.ActivityMainBinding;
import ph.easybus.ebbusseatplan.layoutmanagers.SpannedGridLayoutManager;
import ph.easybus.ebbusseatplan.listeners.RecyclerTouchListener;
import ph.easybus.ebmodels.models.Bus;
import ph.easybus.ebmodels.models.Reservation;
import ph.easybus.ebmodels.models.Trip;

public class MainActivity extends AppCompatActivity {

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

    private ArrayList<String> map = new ArrayList<>(
            Arrays.asList(
                    "RR_UL",
                    "/D_UL",
                    "RA_UL",
                    "AA_UL",
                    "RR_uR",
                    "RR_uR",
                    "AA_UL",
                    "AA_UL",
                    "UU_UL",
                    "LL_UL"
            ));

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
        binding.setListener(this);

        Bus bus = new Bus();
        bus.setLayout("d");
        bus.setSeatMap(map);

        Trip trip = new Trip();
        trip.setBus(bus);
        viewModel.setTrip(trip);

        ObservableArrayList<Reservation> reservations = new ObservableArrayList<>();

        ObservableArrayList<Integer> reservedSeats = new ObservableArrayList<>();

        reservedSeats.addAll(Arrays.asList(1, 2, 3));

        Reservation reservation = new Reservation();
        reservation.setShortAlias("M");
        reservation.setStatus(1);
        reservation.setReservedSeats(reservedSeats);

        reservations.add(reservation);
        viewModel.setReservations(reservations);

        /*
        int rows = map.length, cols = map[0].length();

        binding.rvSeats.setLayoutManager(new SpannedGridLayoutManager(this, 45,
                cols, rows, position -> {
            ArrayList<GridSeat> seats = viewModel.getSeats().getValue();

            GridSeat seat = seats.get(position);
            return new SpannedGridLayoutManager.SpanInfo(seat.getX(), seat.getY(),
                    seat.getW(), seat.getH());
        }));

        RecyclerTouchListener.setup(this, binding.rvSeats, this);

        calculateSeatPlan(); */
    }

    public void onSeatSelected(GridSeat busSeat, boolean selected) {

    }
}
