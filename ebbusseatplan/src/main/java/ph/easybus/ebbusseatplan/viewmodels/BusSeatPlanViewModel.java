package ph.easybus.ebbusseatplan.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.BR;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebmodels.models.Bus;
import ph.easybus.ebmodels.models.Reservation;
import ph.easybus.ebmodels.models.Trip;

public class BusSeatPlanViewModel extends BaseObservable {

    @Bindable
    private int columns;
    public int getColumns() { return columns; }

    private boolean isCustomersView = false;
    @Bindable
    public boolean getIsCustomersView() { return isCustomersView; }
    public void setIsCustomersView(boolean isCustomersView) {
        this.isCustomersView = isCustomersView;
        notifyPropertyChanged(BR.isCustomersView);
    }

    private Trip trip;
    @Bindable
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) {
        this.trip = trip;
        notifyPropertyChanged(BR.trip);

        calculateSeatPlan();

        // DELAY CALCULATING OF SEAT PLAN, SO IT WILL PROCESS AT INITIAL DRAWING OF FRAGMENT/ACTIVITY
        // MAKING LOADING OF FRAGMENT/ACTIVITY FASTER
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                calculateSeatPlan();
            }
        }, 200); */
    }

    private String office;
    @Bindable
    public String getOffice() { return office; }
    public void setOffice(String office) {
        this.office = office;
        notifyPropertyChanged(BR.office);
    }

    private ArrayList<BusSeat> busSeats;
    @Bindable
    public ArrayList<BusSeat> getBusSeats() { return busSeats; }

    private ObservableArrayList<Integer> selectedSeats = new ObservableArrayList<>();
    @Bindable
    public ObservableArrayList<Integer> getSelectedSeats() { return selectedSeats; }

    private ObservableList.OnListChangedCallback<ObservableList<Integer>> selectedSeatsCallback =
            new ObservableList.OnListChangedCallback<ObservableList<Integer>>() {
                public void onChanged(ObservableList<Integer> sender) {}
                public void onItemRangeChanged(ObservableList<Integer> sender, int positionStart, int itemCount) {}
                public void onItemRangeMoved(ObservableList<Integer> sender, int fromPosition, int toPosition, int itemCount) {}

                public void onItemRangeInserted(ObservableList<Integer> sender,
                                                int positionStart, int itemCount) {
                    for (int i = positionStart; i < itemCount; i++) {
                        int selectedSeat = sender.get(i);
                        for (int j = 0; j < busSeats.size(); j++) {
                            BusSeat seat = busSeats.get(j);
                            if (seat.getSeatNumber() == selectedSeat) {
                                seat.setSelected(true);
                            }
                        }
                    }
                }

                public void onItemRangeRemoved(ObservableList<Integer> sender,
                                               int positionStart, int itemCount) {
                    for (int i = 0; i < busSeats.size(); i++) {
                        BusSeat seat = busSeats.get(i);
                        if (seat.isSelected()) {
                            if (sender.contains(seat.getSeatNumber())) seat.setSelected(false);
                            else seat.setSelected(false);
                        }
                    }
                }
            };

    public void setSelectedSeats(ObservableArrayList<Integer> selectedSeats) {
        if (this.selectedSeats != null) this.selectedSeats.removeOnListChangedCallback(selectedSeatsCallback);

        this.selectedSeats = selectedSeats;
        if (this.selectedSeats != null) this.selectedSeats.addOnListChangedCallback(selectedSeatsCallback);
        notifyPropertyChanged(BR.selectedSeats);
        calculateSelectedSeats();
    }

    private ObservableArrayList<Reservation> reservations = new ObservableArrayList<>();
    @Bindable
    public ObservableArrayList<Reservation> getReservations() { return reservations; };

    private ObservableList.OnListChangedCallback<ObservableList<Reservation>> reservationsCallback =
            new ObservableList.OnListChangedCallback<ObservableList<Reservation>>() {
                public void onChanged(ObservableList<Reservation> sender) {}

                public void onItemRangeChanged(ObservableList<Reservation> sender,
                                               int positionStart, int itemCount) {}

                public void onItemRangeMoved(ObservableList<Reservation> sender,
                                             int fromPosition, int toPosition, int itemCount) {}

                public void onItemRangeRemoved(ObservableList<Reservation> sender,
                                                int positionStart, int itemCount) {
                    for (int i = 0; i < busSeats.size(); i++) {
                        BusSeat seat = busSeats.get(i);
                        if (seat.isReserved()) {
                            boolean existOnList = false;
                            for (int j = 0; j < sender.size(); j++) {
                                Reservation reservation = sender.get(j);
                                if (seat.getReservation() != null) {
                                    if (seat.getReservation().getMongoId()
                                        .equals(reservation.getMongoId())) {
                                        existOnList = true;
                                        break;
                                    }
                                }
                            }
                            if (!existOnList) {
                                seat.setSeatType(BusSeat.SEAT_TYPE_AVAILABLE);
                                seat.setReserved(false);
                                seat.setReservation(null);
                            }
                        }
                    }
                }

                public void onItemRangeInserted(ObservableList<Reservation> sender,
                                                int positionStart, int itemCount) {
                    for (int i = positionStart; i < itemCount; i++) {
                        Reservation reservation = sender.get(i);
                        for (int j = 0; j < reservation.getReservedSeats().size(); j++) {
                            for (int k = 0; k < busSeats.size(); k++) {
                                BusSeat seat = busSeats.get(k);
                                if (seat.getSeatNumber() == reservation.getReservedSeats().get(j)) {
                                    seat.setSeatType(BusSeat.SEAT_TYPE_OCCUPIED);
                                    seat.setReserved(true);
                                    seat.setReservation(reservation);
                                }
                            }
                        }
                    }
                }
            };

    public void setReservations(ObservableArrayList<Reservation> reservations) {
        if (this.reservations != null) this.reservations.removeOnListChangedCallback(reservationsCallback);

        this.reservations = reservations;
        if (this.reservations != null) this.reservations.addOnListChangedCallback(reservationsCallback);
        notifyPropertyChanged(BR.reservations);

        calculateReservedSeats();
    }

    private boolean seatPlanFinished = false;
    public boolean isSeatPlanFinished() { return seatPlanFinished; }

    private OnSeatPlanEventListener onSeatPlanEventListener;
    public void setOnSeatPlanEventListener(OnSeatPlanEventListener listener) {
        onSeatPlanEventListener = listener;
        if (seatPlanFinished) {
            if (onSeatPlanEventListener != null) onSeatPlanEventListener.onSeatPlanFinish();
        }
    }

    private void calculateReservedSeats() {
        new Thread(() -> {
            if (busSeats != null && reservations != null) {
                for (int i = 0; i < reservations.size(); i++) {
                    Reservation reservation = reservations.get(i);
                    for (int j = 0; j < reservation.getReservedSeats().size(); j++) {
                        for (int k = 0; k < busSeats.size(); k++) {
                            BusSeat seat = busSeats.get(k);
                            if (seat.getSeatNumber() == reservation.getReservedSeats().get(j)) {
                                seat.setSeatType(BusSeat.SEAT_TYPE_OCCUPIED);
                                seat.setReserved(true);
                                seat.setReservation(reservation);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void calculateSelectedSeats() {
        new Thread(() -> {
            if (busSeats != null && selectedSeats != null) {
                for (int i = 0; i < selectedSeats.size(); i++) {
                    for (int j = 0; j < busSeats.size(); j++) {
                        BusSeat seat = busSeats.get(j);
                        if (seat.getSeatNumber() == selectedSeats.get(i))
                            seat.setSelected(true);
                    }
                }
            }
        }).start();
    }

    private void calculateSeatPlan() {
        new Thread(() -> {
            seatPlanFinished = false;
            Trip currTrip = BusSeatPlanViewModel.this.trip;
            if (currTrip == null) return;
            Bus bus = currTrip.getBus();
            String layout = bus.getLayout().isEmpty() ? "d" : bus.getLayout();
            columns = bus.getSeatMap().get(0).length();
            notifyPropertyChanged(BR.columns);

            int seatCounter = 1;
            ArrayList<BusSeat> busSeats = new ArrayList<>();
            if (layout.equals("d")) {
                for (int i = 0; i < bus.getSeatMap().size(); i++) {
                    String row = bus.getSeatMap().get(i);
                    for (int j = 0; j < row.length(); j++) {
                        int actualSeatNumber = 0;
                        if (row.charAt(j) != '_') {
                            actualSeatNumber = seatCounter;
                            seatCounter++;
                        }

                        BusSeat seat = createBusSeat(actualSeatNumber, row.charAt(j));
                        if (selectedSeats != null) {
                            for (int k = 0; k < selectedSeats.size(); k++) {
                                if (selectedSeats.get(k) == actualSeatNumber) {
                                    seat.setSelected(true);
                                }
                            }
                        }

                        /*
                        if (reservations != null) {
                            for (int k = 0; k < reservations.size(); k++) {
                                for (int l = 0; l < reservations.get(k).getReservedSeats().size(); l++) {
                                    if (reservations.get(k).getReservedSeats().get(l) == actualSeatNumber) {
                                        seat.setSeatType(BusSeat.SEAT_TYPE_OCCUPIED);
                                        seat.setReserved(true);
                                        seat.setReservation(reservations.get(k));
                                    }
                                }
                            }
                        } */
                        busSeats.add(seat);
                    }
                }
            } else if (layout.equals("r")) {
                for (int i = 0; i < bus.getSeatMap().size(); i++) {
                    int colMaxSeats = 0, currColSeat = 1;
                    String row = bus.getSeatMap().get(i);
                    for (int j = 0; j < row.length(); j++) {
                        if (row.charAt(j) != '_') colMaxSeats++;
                    }

                    for (int j = 0; j < row.length(); j++) {
                        int actualSeatNumber = 0;
                        if (row.charAt(j) != '_') {
                            actualSeatNumber = seatCounter + colMaxSeats - currColSeat;
                            currColSeat++;
                        }

                        BusSeat seat = createBusSeat(actualSeatNumber, row.charAt(j));
                        if (selectedSeats != null) {
                            for (int k = 0; k < selectedSeats.size(); k++) {
                                if (selectedSeats.get(k) == actualSeatNumber) {
                                    seat.setSelected(true);
                                }
                            }
                        }

                        /*
                        if (reservations != null) {
                            for (int k = 0; k < reservations.size(); k++) {
                                for (int l = 0; l < reservations.get(k).getReservedSeats().size(); l++) {
                                    if (reservations.get(k).getReservedSeats().get(l) == actualSeatNumber) {
                                        seat.setSeatType(BusSeat.SEAT_TYPE_OCCUPIED);
                                        seat.setReserved(true);
                                        seat.setReservation(reservations.get(k));
                                    }
                                }
                            }
                        } */
                        busSeats.add(seat);
                    }
                    seatCounter += colMaxSeats;
                }
            }

            for (int i = 0; i < currTrip.getChoiceSeats().size(); i++) {
                for (int j = 0; j < busSeats.size(); i++) {
                    if (currTrip.getChoiceSeats().get(i) == busSeats.get(j).getSeatNumber()) {
                        busSeats.get(j).setSeatType(BusSeat.SEAT_TYPE_PREMIUM);
                        break;
                    }
                }
            }

            BusSeatPlanViewModel.this.busSeats = busSeats;
            notifyPropertyChanged(BR.busSeats);
            calculateReservedSeats();

            seatPlanFinished = true;
            if (onSeatPlanEventListener != null) onSeatPlanEventListener.onSeatPlanFinish();
        }).start();
    }

    private BusSeat createBusSeat(int _SeatNumber, char _SeatType) {
        BusSeat seat = new BusSeat();
        seat.setSeatPlanViewModel(this);
        if (_SeatType == '_') {
            seat.setSeatType(BusSeat.SEAT_TYPE_NONE);
            seat.setSpace(true);
        } else {
            seat.setSeatNumber(_SeatNumber);
            switch (_SeatType) {
                case 'A':
                    seat.setSeatType(BusSeat.SEAT_TYPE_AVAILABLE);
                    break;
                case 'C':
                    seat.setSeatType(BusSeat.SEAT_TYPE_PREMIUM);
                    break;
                case 'X':
                    seat.setSeatType(BusSeat.SEAT_TYPE_BLOCKED);
                    break;
                case 'D':
                    seat.setSeatType(BusSeat.SEAT_TYPE_AVAILABLE);
                    seat.setSpecialType(BusSeat.SPECIAL_SEAT_DISABLE);
                    break;
                case '/':
                    seat.setSeatType(BusSeat.SEAT_TYPE_AVAILABLE);
                    seat.setSpecialType(BusSeat.SPECIAL_SEAT_ALLOCATED);
                    break;
            }
        }
        return seat;
    }

    public interface OnSeatPlanEventListener {
        void onSeatPlanFinish();
    }
}
