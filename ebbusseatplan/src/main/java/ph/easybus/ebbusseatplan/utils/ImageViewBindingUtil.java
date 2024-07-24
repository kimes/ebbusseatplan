package ph.easybus.ebbusseatplan.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.models.BusSeat;
import ph.easybus.ebmodels.models.Reservation;

public class ImageViewBindingUtil {

    @BindingAdapter({ "colorSelected", "colorReserved", "isCustomersView" })
    public static void setReservation(ImageView imageView, boolean selected, boolean reserved,
                                      boolean isCustomersView) {
        Context context = imageView.getContext();

        imageView.setColorFilter(ContextCompat.getColor(context, R.color.gray10));

        if (selected) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        if (reserved) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        if (isCustomersView) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }
    }

    @BindingAdapter(value = { "seatType", "reserved", "selected", "side", "isCustomersView" }, requireAll = false)
    public static void setBedSeat(AppCompatImageView view,
                                  String seatType, boolean reserved, boolean selected, int side,
                                  boolean isCustomersView) {
        Resources res = view.getResources();
        Resources.Theme theme = view.getContext().getTheme();

        int seatId = R.drawable.ic_seat_available;
        if ("A".equals(seatType)) {
            seatId = R.drawable.ic_seat_available;
            if (selected) seatId = R.drawable.ic_seat_selected;
            if (reserved) seatId = R.drawable.ic_seat_reserved;
        } else if ("D".equals(seatType) || "/".equals(seatType)) {
            if (isCustomersView) seatId = R.drawable.ic_seat_reserved;
            else seatId = R.drawable.ic_seat_available;

            if (selected) seatId = R.drawable.ic_seat_selected;
            if (reserved) seatId = R.drawable.ic_seat_reserved;
        } else if ("X".equals(seatType)) {
            seatId = R.drawable.ic_seat_reserved;
        } else if ("C".equals(seatType)) {
            seatId = R.drawable.ic_seat_premium;

            if (selected) seatId = R.drawable.ic_seat_selected;
            if (reserved) seatId = R.drawable.ic_seat_reserved;
        } else if ("U".equals(seatType)) {
            seatId = R.drawable.ic_bed_upper_available;
            if (selected) seatId = R.drawable.ic_bed_upper_selected;
            if (reserved) seatId = R.drawable.ic_bed_upper_blocked;

            if (side == 0) {
                seatId = R.drawable.ic_bed_upper_available_landscape;
                if (selected) seatId = R.drawable.ic_bed_upper_selected_landscape;
                if (reserved) seatId = R.drawable.ic_bed_upper_blocked_landscape;
            }
        } else if ("L".equals(seatType)) {
            seatId = R.drawable.ic_bed_lower_available;
            if (selected) seatId = R.drawable.ic_bed_lower_selected;
            if (reserved) seatId = R.drawable.ic_bed_lower_blocked;

            if (side == 0) {
                seatId = R.drawable.ic_bed_lower_available_landscape;
                if (selected) seatId = R.drawable.ic_bed_lower_selected_landscape;
                if (reserved) seatId = R.drawable.ic_bed_lower_blocked_landscape;
            }
        } else if ("u".equals(seatType)) {
            seatId = R.drawable.ic_bed_upper_blocked;
            if (side == 0) {
                seatId = R.drawable.ic_bed_upper_blocked_landscape;
            }
        } else if ("l".equals(seatType)) {
            seatId = R.drawable.ic_bed_lower_blocked;
            if (side == 0) {
                seatId = R.drawable.ic_bed_lower_blocked_landscape;
            }
        } else if ("R".equals(seatType)) {
            seatId = R.drawable.bg_cr;
        }

        //view.setBackground(ResourcesCompat.getDrawable(res, seatId, theme));
        view.setImageDrawable(ResourcesCompat.getDrawable(res, seatId, theme));
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
                    imageView.setImageResource(R.drawable.ic_seat_premium);
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
