package ph.easybus.ebbusseatplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.databinding.ViewBusSeatPlanBinding;
import ph.easybus.ebbusseatplan.listeners.RecyclerTouchListener;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.ebbusseatplan.viewmodels.BusSeatPlanViewModel;
import ph.easybus.ebmodels.models.Bus;
import ph.easybus.ebmodels.models.Trip;

public class BusSeatPlanView extends LinearLayout implements RecyclerTouchListener.OnItemClickListener,
        BusSeatPlanViewModel.OnSeatPlanEventListener {

    private BusSeatPlanViewModel viewModel;
    public BusSeatPlanViewModel getViewModel() { return viewModel; }

    private RecyclerView rvSeat;

    private boolean enabled = true;
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    private int maxSelectedSeats = 0;
    public void setMaxSelectedSeats(int maxSeats) { maxSelectedSeats = maxSeats; }

    private OnMaxSeatsSelectedListener onMaxSeatsSelectedListener;
    public void setOnMaxSeatsSelectedListener(OnMaxSeatsSelectedListener listener) {
        onMaxSeatsSelectedListener = listener;
    }

    private OnReservedSeatClickListener onReservedSeatClickListener;
    public void setOnReservedSeatClickListener(OnReservedSeatClickListener listener) {
        onReservedSeatClickListener = listener;
    }

    private ArrayList<OnSeatSelectionListener> onSeatSelectionListeners = new ArrayList<>();
    public void addOnSeatSelectionListeners(OnSeatSelectionListener onSeatSelectionListener) {
        this.onSeatSelectionListeners.add(onSeatSelectionListener);
    }
    public void removeOnSeatSelectionListeners(OnSeatSelectionListener onSeatSelectionListener) {
        this.onSeatSelectionListeners.add(onSeatSelectionListener);
    }
    /*
    private OnSeatSelectionListener onSeatSelectionListener;
    public void setOnSeatSelectionListener(OnSeatSelectionListener listener) {
        onSeatSelectionListener = listener;
    } */

    private BusSeatPlanViewModel.OnSeatPlanEventListener onSeatPlanEventListener;
    public void setOnSeatPlanEventListener(BusSeatPlanViewModel.OnSeatPlanEventListener listener) {
        onSeatPlanEventListener = listener;
        if (viewModel.isSeatPlanFinished()) {
            if (onSeatPlanEventListener != null) onSeatPlanEventListener.onSeatPlanFinish();
        }
    }

    public BusSeatPlanView(Context context) {
        super(context);
        init();
    }

    public BusSeatPlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void onSeatPlanFinish() {
        if (onSeatPlanEventListener != null) onSeatPlanEventListener.onSeatPlanFinish();
    }

    private void init() {
        ViewBusSeatPlanBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.view_bus_seat_plan,
                this, true);
        viewModel = new BusSeatPlanViewModel();
        binding.setViewModel(viewModel);

        viewModel.setOnSeatPlanEventListener(this);

        rvSeat = binding.rvSeat;
        RecyclerTouchListener.setup(getContext(), rvSeat, this);
    }

    public void onClick(View view, int position) {
        if (!enabled) { return; }

        Trip trip = viewModel.getTrip();
        Bus bus = trip.getBus();

        GridSeat seat = viewModel.getSeats().get(position);

        if (seat.isSelectable()) {
            boolean process = true;
            if (seat.isReserved()) process = false;

            if (maxSelectedSeats > 0) {

                if (bus.isUseAlias()) {
                    if (viewModel.getSelectedSeatsAlias().size() >= maxSelectedSeats) {
                        process = false;
                        if (onMaxSeatsSelectedListener != null) onMaxSeatsSelectedListener.onMaxSeatSelected();
                    }
                } else {
                    if (viewModel.getSelectedSeats().size() >= maxSelectedSeats) {
                        process = false;
                        if (onMaxSeatsSelectedListener != null) onMaxSeatsSelectedListener.onMaxSeatSelected();
                    }
                }

                if (seat.isSelected()) process = true;
            }

            if (process) {
                boolean selected = false;

                if (bus.isUseAlias()) {
                    if (!seat.isSelected()) {
                        //viewModel.getSelectedSeats().add(1);
                        viewModel.getSelectedSeatsAlias().add(seat.getSeatAlias());
                        selected = true;
                    } else {
                        int selectedIndex = -1;
                        for (int i = 0; i < viewModel.getSelectedSeatsAlias().size(); i++) {
                            if (viewModel.getSelectedSeatsAlias().get(i).equals(seat.getSeatAlias())) {
                                selectedIndex = i;
                                break;
                            }
                        }

                        if (selectedIndex >= 0) {
                            viewModel.getSelectedSeatsAlias().remove(selectedIndex);
                            selected = false;
                        }
                    }

                    if (onSeatSelectionListeners != null) {
                        for (int i = 0; i < onSeatSelectionListeners.size(); i++) {
                            onSeatSelectionListeners.get(i).onSeatSelected(seat, selected);
                        }
                        //onSeatSelectionListener.onSeatSelected(seat, selected);
                    }
                } else {
                    if (!seat.isSelected()) {
                        viewModel.getSelectedSeats().add(seat.getNum());
                        selected = true;
                    } else {
                        int selectedIndex = -1;
                        for (int i = 0; i < viewModel.getSelectedSeats().size(); i++) {
                            if (viewModel.getSelectedSeats().get(i) == seat.getNum()) {
                                selectedIndex = i;
                                break;
                            }
                        }

                        if (selectedIndex >= 0) {
                            viewModel.getSelectedSeats().remove(selectedIndex);
                            selected = false;
                        }
                    }

                    if (onSeatSelectionListeners != null) {
                        for (int i = 0; i < onSeatSelectionListeners.size(); i++) {
                            onSeatSelectionListeners.get(i).onSeatSelected(seat, selected);
                        }
                        //onSeatSelectionListener.onSeatSelected(seat, selected);
                    }
                }
            }
        }

        if (seat.isReserved()) {
            if (onReservedSeatClickListener != null)
                onReservedSeatClickListener.onReservedSeatClick(seat);
        }

        /*
        if (seat.getSeatType() == BusSeat.SEAT_TYPE_AVAILABLE ||
            seat.getSeatType() == BusSeat.SEAT_TYPE_PREMIUM) {

            boolean search = true;

            if (viewModel.getIsCustomersView() && seat.getSpecialType() != BusSeat.SPECIAL_SEAT_NONE) search = false;

            if (search) {
                boolean process = true;
                if (maxSelectedSeats > 0) {
                    if (viewModel.getSelectedSeats().size() >= maxSelectedSeats) {
                        process = false;
                        if (onMaxSeatsSelectedListener != null) onMaxSeatsSelectedListener.onMaxSeatSelected();
                    }
                    if (seat.isSelected()) process = true;
                }

                //seat.setSelected(!seat.isSelected());
                if (process) {
                    boolean selected = false;
                    if (!seat.isSelected()) {
                        viewModel.getSelectedSeats().add(seat.getSeatNumber());
                        selected = true;
                    } else {
                        int selectedIndex = -1;
                        for (int i = 0; i < viewModel.getSelectedSeats().size(); i++) {
                            if (viewModel.getSelectedSeats().get(i) == seat.getSeatNumber()) {
                                selectedIndex = i;
                                break;
                            }
                        }

                        if (selectedIndex >= 0) {
                            viewModel.getSelectedSeats().remove(selectedIndex);
                            selected = false;
                        }
                    }

                    if (onSeatSelectionListener != null) {
                        //seat.isSelected()
                        onSeatSelectionListener.onSeatSelected(seat, selected);
                    }
                }
            }
        }

        if (!seat.isReserved() && !seat.isSpace()) {
        } */
    }

    public void onLongClick(View view, int position) {}

    public interface OnSeatSelectionListener {
        void onSeatSelected(GridSeat busSeat, boolean selected);
    }

    public interface OnReservedSeatClickListener {
        void onReservedSeatClick(GridSeat busSeat);
    }

    public interface OnMaxSeatsSelectedListener {
        void onMaxSeatSelected();
    }
}
