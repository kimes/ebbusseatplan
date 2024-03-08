package ph.easybus.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ph.easybus.ebbusseatplan.models.GridSeat;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Integer>> numbers = new MutableLiveData<>(new ArrayList<>());
    public LiveData<ArrayList<Integer>> getNumbers() { return numbers; }
    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers.setValue(numbers);
    }

    private MutableLiveData<ArrayList<GridSeat>> seats = new MutableLiveData<>(new ArrayList<>());
    public LiveData<ArrayList<GridSeat>> getSeats() { return seats; }
    public void setSeats(ArrayList<GridSeat> seats) { this.seats.postValue(seats); }
}
