package ph.easybus.ebbusseatplan.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebmodels.models.Reservation;

public class TextViewBindingUtil {

    @BindingAdapter({ "seatType", "specialType", "selected", "isCustomersView", "reservation" })
    public static void setReservation(TextView textView, int seatType, int specialType,
                                      boolean selected, boolean isCustomersView,
                                      Reservation reservation) {
        Context context = textView.getContext();
        textView.setTextColor(context.getResources().getColor(R.color.gray70));

        if (reservation != null) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (selected) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (seatType == BusSeat.SEAT_TYPE_BLOCKED) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (isCustomersView && specialType != BusSeat.SPECIAL_SEAT_NONE) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }
    }
}
