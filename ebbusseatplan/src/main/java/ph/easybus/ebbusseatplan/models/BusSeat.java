package ph.easybus.ebbusseatplan.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import ph.easybus.ebbusseatplan.BR;
import ph.easybus.ebbusseatplan.viewmodels.BusSeatPlanViewModel;
import ph.easybus.ebmodels.models.Reservation;

public class BusSeat extends BaseObservable {

    public static final int SEAT_TYPE_NONE = 0, SEAT_TYPE_AVAILABLE = 1,
            SEAT_TYPE_OCCUPIED = 2, SEAT_TYPE_BLOCKED = 3, SEAT_TYPE_PREMIUM = 4;
    public static final int SPECIAL_SEAT_NONE = 0, SPECIAL_SEAT_DISABLE = 1,
            SPECIAL_SEAT_ALLOCATED = 2;
    @Bindable
    private int seatNumber = 0, seatType = 0, specialType = 0;

    @Bindable
    private boolean selected = false, reserved = false, space = false;
    @Bindable
    private Reservation reservation;
    @Bindable
    private BusSeatPlanViewModel seatPlanViewModel;

    public boolean isSelected() { return selected; }
    public boolean isReserved() { return reserved; }
    public boolean isSpace() { return space; }
    public int getSeatNumber() { return seatNumber; }
    public int getSeatType() { return seatType; }
    public int getSpecialType() { return specialType; }
    public Reservation getReservation() { return reservation; }
    public BusSeatPlanViewModel getSeatPlanViewModel() { return seatPlanViewModel; }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
        notifyPropertyChanged(BR.reserved);
    }
    public void setSpace(boolean space) {
        this.space = space;
        notifyPropertyChanged(BR.space);
    }
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
        notifyPropertyChanged(BR.seatNumber);
    }
    public void setSeatType(int seatType) {
        this.seatType = seatType;
        notifyPropertyChanged(BR.seatType);
    }
    public void setSpecialType(int specialType) {
        this.specialType = specialType;
        notifyPropertyChanged(BR.specialType);
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        notifyPropertyChanged(BR.reservation);
    }
    public void setSeatPlanViewModel(BusSeatPlanViewModel seatPlanViewModel) {
        this.seatPlanViewModel = seatPlanViewModel;
        notifyPropertyChanged(BR.seatPlanViewModel);
    }
}
