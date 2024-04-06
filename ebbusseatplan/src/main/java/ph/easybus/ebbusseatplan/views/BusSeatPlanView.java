package ph.easybus.ebbusseatplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.databinding.ViewBusSeatPlanBinding;
import ph.easybus.ebbusseatplan.listeners.RecyclerTouchListener;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.ebbusseatplan.viewmodels.BusSeatPlanViewModel;

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

    private OnSeatSelectionListener onSeatSelectionListener;
    public void setOnSeatSelectionListener(OnSeatSelectionListener listener) {
        onSeatSelectionListener = listener;
    }

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

        GridSeat seat = viewModel.getSeats().get(position);

        if (seat.isSelectable()) {

            boolean process = true;
            if (seat.isReserved()) process = false;

            if (maxSelectedSeats > 0) {
                if (viewModel.getSelectedSeats().size() >= maxSelectedSeats) {
                    process = false;
                    if (onMaxSeatsSelectedListener != null) onMaxSeatsSelectedListener.onMaxSeatSelected();
                }

                if (seat.isSelected()) process = true;
            }

            if (process) {
                boolean selected = false;
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

                if (onSeatSelectionListener != null) onSeatSelectionListener.onSeatSelected(seat, selected);
            }
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

    public interface OnMaxSeatsSelectedListener {
        void onMaxSeatSelected();
    }
}
