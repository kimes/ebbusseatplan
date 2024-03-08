package ph.easybus.app.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.ebmodels.models.Reservation;
import ph.easybus.ebmodels.models.Trip;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<ObservableArrayList<Integer>> selectedSeats =
            new MutableLiveData<>(new ObservableArrayList<>());
    public MutableLiveData<ObservableArrayList<Integer>> getSelectedSeats() { return selectedSeats; }

    private MutableLiveData<ObservableArrayList<Reservation>> reservations =
            new MutableLiveData<>(new ObservableArrayList<>());
    public LiveData<ObservableArrayList<Reservation>> getReservations() { return reservations; }
    public void setReservations(ObservableArrayList<Reservation> reservations) {
        this.reservations.postValue(reservations);
    }

    private MutableLiveData<Trip> trip = new MutableLiveData<>();
    public LiveData<Trip> getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip.setValue(trip); }
}
