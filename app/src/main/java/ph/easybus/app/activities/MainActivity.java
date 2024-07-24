package ph.easybus.app.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
                    "CC_UL",
                    "/D_UL",
                    "RA_UL",
                    "//_UL",
                    "RR_uR",
                    "RR_uR",
                    "AA_UL",
                    "AA_UL",
                    "UU_UL",
                    "LL_UL"
            ));

    private ArrayList<ArrayList<Integer>> seatNumbers = new ArrayList<>(
            Arrays.asList(
                    new ArrayList<>(Arrays.asList(0, 0, 0, 3, 4)),
                    new ArrayList<>(Arrays.asList(1, 2, 0, 3, 4)),
                    new ArrayList<>(Arrays.asList(0, 7, 0, 5, 6)),
                    new ArrayList<>(Arrays.asList(9, 8, 0, 5, 6)),
                    new ArrayList<>(Arrays.asList(0, 0, 0, 10, 0)),
                    new ArrayList<>(Arrays.asList(0, 0, 0, 10, 0)),
                    new ArrayList<>(Arrays.asList(11, 13, 0, 16, 15)),
                    new ArrayList<>(Arrays.asList(12, 14, 0, 16, 15)),
                    new ArrayList<>(Arrays.asList(17, 17, 0, 18, 19)),
                    new ArrayList<>(Arrays.asList(20, 20, 0, 18, 19))
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
        //bus.setLayout("c");
        bus.setLayout("d");
        bus.setSeatMap(map);
        bus.setSeatNumbers(seatNumbers);

        /*
        Parcel parcel = Parcel.obtain();
        bus.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

        Bus parcelBus = new Bus(parcel);
        String builder = "";
        for (int i = 0; i < parcelBus.getSeatNumbers().size(); i++) {
            for (int j = 0; j < parcelBus.getSeatNumbers().get(i).size(); j++) {
                builder += parcelBus.getSeatNumbers().get(i).get(j) + " | ";
            }
            builder += "\n";
        }
        System.out.println("parcel bus: " + builder);

        parcel.recycle(); */

        Trip trip = new Trip();
        trip.setBus(bus);
        viewModel.setTrip(trip);

        ObservableArrayList<Reservation> reservations = new ObservableArrayList<>();

        ObservableArrayList<Integer> reservedSeats = new ObservableArrayList<>();

        reservedSeats.addAll(Arrays.asList(13, 14));

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
