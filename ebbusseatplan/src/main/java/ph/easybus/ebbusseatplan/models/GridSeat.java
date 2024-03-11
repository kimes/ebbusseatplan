package ph.easybus.ebbusseatplan.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import ph.easybus.ebbusseatplan.viewmodels.BusSeatPlanViewModel;
import ph.easybus.ebmodels.models.Reservation;

public class GridSeat extends BaseObservable {

    private boolean selectable = true, numberable = false, showSeat = true;

    @Bindable
    private boolean selected = false, reserved = false;

    private int x = 0, y = 0, w = 1, h = 1, side = 0;
    
    private int num = -1;

    private String type = "A";

    @Bindable
    private Reservation reservation;

    @Bindable
    private BusSeatPlanViewModel seatPlan;

    public GridSeat() {}

    public GridSeat(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public GridSeat(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public boolean isSelectable() { return selectable; }
    public boolean isNumberable() { return numberable; }
    public boolean isShowSeat() { return showSeat; }
    public boolean isSelected() { return selected; }
    public boolean isReserved() { return reserved; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    public int getSide() { return side; }
    public int getNum() { return num; }
    public String getType() { return type; }
    public Reservation getReservation() { return reservation; }
    public BusSeatPlanViewModel getSeatPlan() { return seatPlan; }

    public void setSelectable(boolean selectable) { this.selectable = selectable; }
    public void setNumberable(boolean numberable) { this.numberable = numberable; }
    public void setShowSeat(boolean showSeat) { this.showSeat = showSeat; }
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
        notifyPropertyChanged(BR.reserved);
    }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setW(int w) { this.w = w; }
    public void setH(int h) { this.h = h; }
    public void setSide(int side) { this.side = side; }
    public void setNum(int num) { this.num = num; }
    public void setType(String type) { this.type = type; }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        notifyPropertyChanged(BR.reservation);
    }
    public void setSeatPlan(BusSeatPlanViewModel seatPlan) {
        this.seatPlan = seatPlan;
        notifyPropertyChanged(BR.seatPlan);
    }
}
