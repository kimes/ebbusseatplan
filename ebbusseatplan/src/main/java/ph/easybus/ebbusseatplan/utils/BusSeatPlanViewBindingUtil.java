package ph.easybus.ebbusseatplan.utils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ObservableArrayList;

import ph.easybus.ebbusseatplan.viewmodels.BusSeatPlanViewModel;
import ph.easybus.ebbusseatplan.views.BusSeatPlanView;
import ph.easybus.ebmodels.models.Reservation;
import ph.easybus.ebmodels.models.Trip;

public class BusSeatPlanViewBindingUtil {

    @BindingAdapter("onSeatPlanEventListener")
    public static void setSeatPlanEventListener(BusSeatPlanView seatPlanView,
                                                final BusSeatPlanViewModel.OnSeatPlanEventListener listener) {
        seatPlanView.setOnSeatPlanEventListener(() -> {
            if (listener != null) listener.onSeatPlanFinish();
        });
    }

    @BindingAdapter("office")
    public static void setOffice(BusSeatPlanView seatPlanView, String office) {
        seatPlanView.getViewModel().setOffice(office);
    }

    @BindingAdapter("trip")
    public static void setTrip(BusSeatPlanView seatPlanView, Trip trip) {
        seatPlanView.getViewModel().setTrip(trip);
    }

    @BindingAdapter("isCustomersView")
    public static void setCustomersView(BusSeatPlanView seatPlanView, boolean customersView) {
        seatPlanView.getViewModel().setIsCustomersView(customersView);
    }

    @BindingAdapter("reservations")
    public static void setReservations(BusSeatPlanView seatPlanView, ObservableArrayList<Reservation> reservations) {
        if (reservations != null) {
            seatPlanView.getViewModel().setReservations(reservations);
        }
    }

    @BindingAdapter("maxSelectedSeats")
    public static void setMaxSelectedSeats(BusSeatPlanView seatPlanView, int maxSeats) {
        seatPlanView.setMaxSelectedSeats(maxSeats);
    }

    @BindingAdapter("enabled")
    public static void setEnabled(BusSeatPlanView seatPlanView, boolean enabled) {
        seatPlanView.setEnabled(enabled);
    }

    @BindingAdapter(value = { "onSelectedSeatsChanged", "selectedSeatsAttrChanged" }, requireAll = false)
    public static void setSelectedSeatsListener(BusSeatPlanView seatPlanView,
                                                final BusSeatPlanView.OnSeatSelectionListener listener,
                                                final InverseBindingListener bindingListener) {
        seatPlanView.setOnSeatSelectionListener((seatNumber, selected) -> {
            if (bindingListener != null) bindingListener.onChange();
            if (listener != null) listener.onSeatSelected(seatNumber, selected);
        });
    }

    @BindingAdapter("selectedSeats")
    public static void setSelectedSeats(BusSeatPlanView seatPlanView, ObservableArrayList<Integer> selectedSeats) {
        if (selectedSeats != null) {
            seatPlanView.getViewModel().setSelectedSeats(selectedSeats);
        }
    }

    @InverseBindingAdapter( attribute = "selectedSeats", event = "selectedSeatsAttrChanged")
    public static ObservableArrayList<Integer> getSelectedSeats(BusSeatPlanView seatPlanView) {
        return seatPlanView.getViewModel().getSelectedSeats();
    }

}
