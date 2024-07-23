package ph.easybus.ebbusseatplan.viewmodels;

import android.os.Build;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.BR;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebbusseatplan.models.GridSeat;
import ph.easybus.ebmodels.models.Bus;
import ph.easybus.ebmodels.models.Reservation;
import ph.easybus.ebmodels.models.Trip;

public class BusSeatPlanViewModel extends BaseObservable {

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

    @Bindable
    private int columns;
    public int getColumns() { return columns; }

    @Bindable
    private int rows;
    public int getRows() { return rows; }

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

    /*
    private ArrayList<BusSeat> busSeats;
    @Bindable
    public ArrayList<BusSeat> getBusSeats() { return busSeats; } */

    private ArrayList<GridSeat> seats;
    @Bindable
    public ArrayList<GridSeat> getSeats() { return seats; }

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
                        for (int j = 0; j < seats.size(); j++) {
                            GridSeat seat = seats.get(j);
                            if (seat.getNum() == selectedSeat) {
                                seat.setSelected(true);
                            }
                        }
                    }
                }

                public void onItemRangeRemoved(ObservableList<Integer> sender,
                                               int positionStart, int itemCount) {
                    for (int i = 0; i < seats.size(); i++) {
                        GridSeat seat = seats.get(i);
                        if (seat.isSelected()) {
                            if (sender.contains(seat.getNum())) seat.setSelected(false);
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
                    for (int i = 0; i < seats.size(); i++) {
                        GridSeat seat = seats.get(i);
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
                            for (int k = 0; k < seats.size(); k++) {
                                GridSeat seat = seats.get(k);
                                if (seat.getNum() == reservation.getReservedSeats().get(j)) {
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
            if (seats != null && reservations != null) {
                for (int i = 0; i < reservations.size(); i++) {
                    Reservation reservation = reservations.get(i);
                    for (int j = 0; j < reservation.getReservedSeats().size(); j++) {
                        for (int k = 0; k < seats.size(); k++) {
                            GridSeat seat = seats.get(k);
                            if (seat.getNum() == reservation.getReservedSeats().get(j)) {
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
            if (seats != null && selectedSeats != null) {
                for (int i = 0; i < selectedSeats.size(); i++) {
                    for (int j = 0; j < seats.size(); j++) {
                        GridSeat seat = seats.get(j);
                        if (seat.getNum() == selectedSeats.get(i)) seat.setSelected(true);
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

            int r = bus.getSeatMap().size(),
                c = bus.getSeatMap().get(0).length();

            columns = c;
            rows = r;
            notifyPropertyChanged(BR.columns);
            notifyPropertyChanged(BR.rows);

            String[][] seatMapMatrix = new String[r][c];
            for (int i = 0; i < bus.getSeatMap().size(); i++) {
                char[] seatMapCols = bus.getSeatMap().get(i).toCharArray();
                //System.arraycopy(seatMapCols, 0, seatMapMatrix[i], 0, seatMapCols.length);

                String[] columns = new String[seatMapCols.length];
                for (int j = 0; j < seatMapCols.length; j++) {
                    columns[j] = seatMapCols[j] + "";
                }
                seatMapMatrix[i] = columns;
            }

            int currSeat = 0;

            ArrayList<GridSeat> seats = new ArrayList<>(), colsSeat;
            ArrayList<Integer> usedCell = new ArrayList<>();

            int seatCounter = 1;
            for (int i = 0; i < seatMapMatrix.length; i++) {
                colsSeat = new ArrayList<>();

                for (int j = 0; j < c; j++) {
                    int id = ((i * c) + j);

                    if (!usedCell.contains(id)) {
                        String type = seatMapMatrix[i][j];
                        GridSeat seat = new GridSeat(j, i, type);
                        seat.setSeatPlan(this);
                        seat.setSelectable("A".equals(type) || "U".equals(type) ||
                                "L".equals(type) || "C".equals(type) ||
                                "D".equals(type) || "/".equals(type));

                        if ("A".equals(type) || "C".equals(type) ||
                            "X".equals(type) || "D".equals(type) || "/".equals(type)) {
                            seat.setNum(seatCounter);
                            seat.setNumberable(true);
                            seatCounter++;
                        }

                        if (isCustomersView) {
                            if ("X".equals(type) || "D".equals(type) || "/".equals(type)) {
                                seat.setSelectable(false);
                                seat.setReserved(true);
                            }
                        }

                        if ("U".equals(type) || "L".equals(type) || "u".equals(type) || "l".equals(type)) {
                            ArrayList<GridSeat> n = getOrientationNeighbours(j, i, type, seatMapMatrix);
                            n.add(new GridSeat(j, i, 0));

                            int minX = j, maxX = 0, minY = i, maxY = 0;

                            for (int k = 0; k < n.size(); k++) {
                                if (n.get(k).getX() < minX) { minX = n.get(k).getX(); }
                                if (n.get(k).getX() > maxX) { maxX = n.get(k).getX(); }
                                if (n.get(k).getY() < minY) { minY = n.get(k).getY(); }
                                if (n.get(k).getY() > maxY) { maxY = n.get(k).getY(); }

                                usedCell.add((n.get(k).getY() * c) + n.get(k).getX());
                            }

                            if (n.size() > 1) {
                                seat.setSide(n.get(0).getSide());
                            }

                            seat.setNum(seatCounter);
                            seat.setNumberable(true);
                            seat.setX(minX);
                            seat.setY(minY);
                            seat.setW(maxX - minX + 1);
                            seat.setH(maxY - minY + 1);

                            seatCounter++;
                        }

                        if ("R".equals(type)) {
                            ArrayList<GridSeat> n = getRectangleNeighbours(j, i, type,
                                    seatMapMatrix, true, usedCell);

                            int minX = j, maxX = 0, minY = i, maxY = 0;
                            for (int k = 0; k < n.size(); k++) {
                                if (n.get(k).getX() < minX) minX = n.get(k).getX();
                                if (n.get(k).getY() < minY) minY = n.get(k).getY();
                                if (n.get(k).getX() > maxX) maxX = n.get(k).getX();
                                if (n.get(k).getY() > maxY) maxY = n.get(k).getY();

                                usedCell.add((n.get(k).getY() * c) + n.get(k).getX());
                            }

                            seat.setX(minX);
                            seat.setY(minY);
                            seat.setW(maxX - minX + 1);
                            seat.setH(maxY - minY + 1);
                            seat.setNum(-1);
                            seat.setShowSeat(false);
                        }

                        if (!"_".equals(type)) {
                            colsSeat.add(seat);
                            seats.add(seat);
                        }
                    }
                }

                if ("d".equals(layout)) {

                } else if ("r".equals(layout)) {
                    ArrayList<GridSeat> seatsNumberable = new ArrayList<>();
                    for (int j = 0; j < colsSeat.size(); j++) {
                        if (colsSeat.get(j).isNumberable()) {
                            seatsNumberable.add(colsSeat.get(j));
                        }
                    }

                    for (int j = 0; j < seatsNumberable.size(); j++) {
                        seatsNumberable.get(j).setNum((currSeat + seatsNumberable.size()) - j);
                    }
                    currSeat += seatsNumberable.size();
                }
            }

            if ("c".equals(layout)) {
                for (int i = 0; i < seats.size(); i++) {
                    GridSeat currGridSeat = seats.get(i);
                    currGridSeat.setNum(bus.getSeatNumbers()
                                        .get(currGridSeat.getY())
                                        .get(currGridSeat.getX()));
                }
            }

            BusSeatPlanViewModel.this.seats = seats;

            notifyPropertyChanged(BR.seats);
            calculateReservedSeats();

            seatPlanFinished = true;
            if (onSeatPlanEventListener != null) onSeatPlanEventListener.onSeatPlanFinish();
        }).start();
    }

    private ArrayList<GridSeat> getOrientationNeighbours(int x, int y,
                                                         String type, String[][] matrix) {
        ArrayList<GridSeat> neighbours = new ArrayList<>();

        for (int i = 0; i < SQUARE_DIR.length; i++) {
            GridSeat side = SQUARE_DIR[i];

            if ((x + side.getX()) >= 0 && (y + side.getY()) >= 0 &&
                    (x + side.getX()) < matrix[y].length && (y + side.getY()) < matrix.length) {

                if (type.equals(matrix[y + side.getY()][x + side.getX()])) {
                    neighbours.add(new GridSeat(x + side.getX(), y + side.getY(), i));
                }
            }
        }

        if (neighbours.size() > 0) {
            if (neighbours.size() == 1) {
                if (neighbours.get(0).getSide() == SE_INDEX) {
                    return new ArrayList<>();
                }
            }

            if (neighbours.size() == 2) {
                ArrayList<GridSeat> finalNeighbours = new ArrayList<>();
                finalNeighbours.add(neighbours.get(0));
                return neighbours;
            }

            if (neighbours.size() == 3) {
                ArrayList<GridSeat> finalNeighbours = new ArrayList<>();
                finalNeighbours.add(neighbours.get(2));
                return neighbours;
            }
        }

        return neighbours;
    }

    private ArrayList<GridSeat> getRectangleNeighbours(int x, int y, String type,
                                                       String[][] matrix, boolean primary,
                                                       ArrayList<Integer> usedCell) {
        ArrayList<GridSeat> neighbours = new ArrayList<>();

        for (int i = 0; i < SQUARE_DIR.length; i++) {
            GridSeat side = SQUARE_DIR[i];

            if ((x + side.getX()) >= 0 && (y + side.getY()) >= 0 &&
                (x + side.getX()) < matrix[y].length && (y + side.getY()) < matrix.length) {
                if (type.equals(matrix[y + side.getY()][x + side.getX()])) {
                    int cellId = ((y + side.getY()) * matrix[y].length) + (x + side.getX());
                    if (!usedCell.contains(cellId)) {
                        neighbours.add(new GridSeat(x + side.getX(), y + side.getY(), i));
                    }
                }
            }
        }

        if (neighbours.size() > 0) {
            for (int i = 0; i < neighbours.size(); i++) {
                ArrayList<GridSeat> n = getRectangleNeighbours(neighbours.get(i).getX(),
                        neighbours.get(i).getY(), type, matrix, false, usedCell);
                neighbours.addAll(n);
            }
        }

        if (primary) {
            neighbours.add(new GridSeat(x, y, 0));

            int minX = 1000, minY = 1000, maxX = 0, maxY = 0;
            for (int i = 0; i < neighbours.size(); i++) {
                GridSeat s = neighbours.get(i);
                if (s.getX() < minX) minX = s.getX();
                if (s.getY() < minY) minY = s.getY();
                if (s.getX() > maxX) maxX = s.getX();
                if (s.getY() > maxY) maxY = s.getY();
            }

            ArrayList<Integer> rowHood = new ArrayList<>();
            ArrayList<GridSeat> row;

            ArrayList<Integer> neighboursToRow = new ArrayList<>();
            for (int i = minY; i <= maxY; i++) {
                row = new ArrayList<>();

                for (int j = 0; j < neighbours.size(); j++) {
                    int cellId = (neighbours.get(j).getY() * matrix[y].length) +
                            (x + neighbours.get(j).getX());

                    if (neighbours.get(j).getY() == i) {
                        if (!rowHood.contains(cellId)) {
                            rowHood.add(cellId);
                            row.add(neighbours.get(j));
                        }
                    }
                }

                if (row.size() > 0) {
                    /*
                    row.sort((a, b) -> (a.getX() < b.getX()) ? -1 : 1);

                    if (row.get(0).getX() == x) {
                        // ADD NEIGHBOURS TO ROW
                    } */
                    for (int j = 0; j < row.size(); j++) {
                        neighboursToRow.add(row.get(j).getX());
                    }
                }
            }

            int colMax = 0;
            for (int i = 0; i < neighboursToRow.size(); i++) {
                if (neighboursToRow.get(i) > colMax) colMax = neighboursToRow.get(i);
            }

            rowHood = new ArrayList<>();
            neighboursToRow = new ArrayList<>();
            for (int i = minX; i <= maxX; i++) {
                row = new ArrayList<>();

                for (int j = 0; j < neighbours.size(); j++) {
                    int cellId = (neighbours.get(j).getY() * matrix[y].length) + (x + neighbours.get(j).getX());

                    if (neighbours.get(j).getX() == i) {
                        if (!rowHood.contains(cellId)) {
                            rowHood.add(cellId);
                            row.add(neighbours.get(j));
                        }
                    }
                }

                if (row.size() > 0) {
                    /*
                    row.sort((a, b) -> (a.getX() < b.getX()) ? -1 : 1);

                    if (row.get(0).getY() == y) {
                        // ADD NEIGHBOURS TO ROW
                    } */

                    for (int j = 0; j < row.size(); j++) {
                        neighboursToRow.add(row.get(j).getY());
                    }
                }
            }

            int rowMax = 0;
            for (int i = 0; i < neighboursToRow.size(); i++) {
                if (neighboursToRow.get(i) > rowMax) rowMax = neighboursToRow.get(i);
            }

            ArrayList<GridSeat> finalNeighbours = new ArrayList<>();
            rowHood = new ArrayList<>();
            for (int i = 0; i < neighbours.size(); i++) {
                if (neighbours.get(i).getX() >= x && neighbours.get(i).getX() <= colMax &&
                    neighbours.get(i).getY() >= y && neighbours.get(i).getY() <= rowMax) {
                    int cellId = (neighbours.get(i).getY() * matrix[y].length) + (x + neighbours.get(i).getX());
                    if (!rowHood.contains(cellId)) {
                        rowHood.add(cellId);
                        finalNeighbours.add(neighbours.get(i));
                    }
                }
            }

            return finalNeighbours;
        }

        return neighbours;
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
