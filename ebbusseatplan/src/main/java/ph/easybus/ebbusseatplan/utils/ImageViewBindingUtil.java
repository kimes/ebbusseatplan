package ph.easybus.ebbusseatplan.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebmodels.models.Reservation;

public class ImageViewBindingUtil {

    @BindingAdapter({ "selected", "isCustomersView", "reservation" })
    public static void setReservation(ImageView imageView, boolean selected,
                                      boolean isCustomersView,Reservation reservation) {
        Context context = imageView.getContext();

        imageView.setColorFilter(ContextCompat.getColor(context, R.color.gray10));
        if (reservation != null) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        if (selected) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        if (isCustomersView) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }
    }

    @TargetApi(21)
    @BindingAdapter(value = { "seatType", "specialType", "status", "selected", "isCustomersView" }, requireAll = false)
    public static void setSeatImage(AppCompatImageView imageView, int seatType, int specialType,
                                    int status, boolean selected, boolean isCustomersView) {
        if (selected) {
            imageView.setImageResource(R.drawable.ic_seat_selected);
        } else {
            switch (seatType) {
                case BusSeat.SEAT_TYPE_NONE:
                    imageView.setImageResource(android.R.color.transparent);
                    break;
                case BusSeat.SEAT_TYPE_AVAILABLE:
                    if (isCustomersView && specialType != BusSeat.SPECIAL_SEAT_NONE) {
                        imageView.setImageResource(R.drawable.ic_seat_reserved);
                    } else imageView.setImageResource(R.drawable.ic_seat_available);
                    break;
                case BusSeat.SEAT_TYPE_PREMIUM:
                    imageView.setImageResource(R.drawable.ic_seat_available);
                    break;
                case BusSeat.SEAT_TYPE_BLOCKED:
                    imageView.setImageResource(R.drawable.ic_seat_reserved);
                    /*
                    if (isCustomersView) imageView.setImageResource(R.drawable.ic_seat_reserved);
                    else imageView.setImageResource(R.drawable.ic_seat_blocked); */
                    break;
                case BusSeat.SEAT_TYPE_OCCUPIED:
                    imageView.setImageResource(R.drawable.ic_seat_reserved);
                    break;
            }
            switch (status) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_seat_reserved);
                    /*
                    if (isCustomersView) imageView.setImageResource(R.drawable.ic_seat_reserved);
                    else imageView.setImageResource(R.drawable.ic_seat_blocked); */
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ic_seat_reserved);
                    break;
            }
            /*
            if (reservation != null) {

            } */
        }
    }
}
