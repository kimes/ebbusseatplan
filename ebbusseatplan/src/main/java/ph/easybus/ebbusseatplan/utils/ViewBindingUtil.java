package ph.easybus.ebbusseatplan.utils;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import ph.easybus.ebbusseatplan.R;
import ph.easybus.ebbusseatplan.models.BusSeat;

public class ViewBindingUtil {

    @BindingAdapter({ "shadowSeatType", "shadowReserved" })
    public static void setShadowSeatType(View view, String type, boolean reserved) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)view.getLayoutParams();

        int visibility = View.VISIBLE;
        int topMargin = 0;
        if ("A".equals(type)) {
            topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                    view.getResources().getDisplayMetrics());

            if (reserved) visibility = View.GONE;
        } else if ("U".equals(type) || "L".equals(type)) {
            topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    view.getResources().getDisplayMetrics());

            if (reserved) visibility = View.GONE;
        } else {
            topMargin = 0;
            visibility = View.GONE;
            view.setVisibility(View.GONE);
        }

        params.topMargin = topMargin;
        view.setVisibility(visibility);
    }

    @BindingAdapter({ "shadowSeatType", "shadowSelected" })
    public static void setShadowSeat(View view, int seatType, boolean selected) {
        int visibility = View.GONE;

        if (seatType == BusSeat.SEAT_TYPE_AVAILABLE || seatType == BusSeat.SEAT_TYPE_PREMIUM)
            visibility = View.VISIBLE;

        if (selected) visibility = View.VISIBLE;

        view.setVisibility(visibility);
    }

    @BindingAdapter({ "specialType", "specialTypeMode", "isCustomersView" })
    public static void setSpecialTypeVisibility(View view, int specialType,
                                                int specialTypeMode, boolean isCustomersView) {
        int visibility = View.VISIBLE;

        if (!isCustomersView) {
            if (specialType == specialTypeMode) visibility = View.VISIBLE;
            else visibility = View.GONE;
        } else {
            if (specialTypeMode != 0) visibility = View.GONE;
        }
        view.setVisibility(visibility);
    }

    @BindingAdapter({ "badgeReservation", "badgeReservationOwnOffice", "badgeReservationOffice" })
    public static void setBadgeReservation(View view, int badgeReservation,
                                            String badgeReservationOwnOffice,
                                            String badgeReservationOffice) {
        int badgeDrawable = R.drawable.badge_pending;

        String badgeOwnOffice = badgeReservationOwnOffice != null ? badgeReservationOwnOffice : "",
                badgeOffice =  badgeReservationOffice != null ? badgeReservationOffice : "";
        switch (badgeReservation) {
            case 0:
                if (badgeOwnOffice.equals(badgeOffice)) {
                    badgeDrawable = R.drawable.badge_pending;
                } else {
                    badgeDrawable = R.drawable.badge_pending_other;
                }
                break;
            case 1:
                if (badgeOffice.equals(badgeOwnOffice)) {
                    badgeDrawable = R.drawable.badge_reserved;
                } else if (badgeOffice.equals("O")) {
                    badgeDrawable = R.drawable.badge_reserved_online;
                } else if (badgeOffice.equals("F")) {
                    badgeDrawable = R.drawable.badge_reserved_filemaker;
                } else {
                    badgeDrawable = R.drawable.badge_reserved_other;
                }
                break;
        }
        view.setBackground(ResourcesCompat.getDrawable(view.getResources(),
                badgeDrawable, view.getContext().getTheme()));
    }

    @BindingAdapter({ "badgeReservationValue", "badgeReservationValueMode",
                        "badgeReservationValueOwnOffice", "badgeReservationValueOffice" })
    public static void setBadgeReservationValue(View view, int badgeReservation,
                                                int badgeReservationValueMode,
                                                String badgeReservationValueOwnOffice,
                                                String badgeReservationValueOffice) {
        int status = badgeReservation;

        int visibility = View.VISIBLE;

        String badgeOwnOffice = badgeReservationValueOwnOffice != null ? badgeReservationValueOwnOffice : "",
                badgeOffice = badgeReservationValueOffice != null ? badgeReservationValueOffice : "";

        if (badgeOwnOffice.equals(badgeOffice)) {
            if (status == badgeReservationValueMode) visibility = View.VISIBLE;
            else visibility = View.GONE;
        } else {
            if (badgeReservationValueMode == 1) visibility = View.VISIBLE;
            else visibility = View.GONE;
        }

        view.setVisibility(visibility);
        /*
        if (badgeReservation != null) {
            int status = badgeReservation.getStatus();

            int visibility = View.VISIBLE;

            if (status == badgeReservationValueMode) visibility = View.VISIBLE;
            else visibility = View.GONE;

            view.setVisibility(visibility);
        } */
    }

    @BindingAdapter({ "badgeSpecialType", "isCustomersView" })
    public static void setBadgeSpecialType(View view, int badgeSpecialType, boolean isCustomersView) {
        if (isCustomersView) view.setVisibility(View.GONE);
        else {
            if (badgeSpecialType != 0) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({ "toView", "isCustomersView" })
    public static void setToViewVisibility(View view, boolean toView, boolean isCustomersView) {
        if (isCustomersView) view.setVisibility(View.GONE);
        else {
            if (toView) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.GONE);
        }
    }
}
