package ph.easybus.ebbusseatplan.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kimuel on 6/29/2016.
 */
public class RecyclerTouchListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener {

    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;
    private OnItemClickListener itemClickListener;

    public RecyclerTouchListener(Context context, RecyclerView recyclerView,
                                 OnItemClickListener itemClickListener) {
        this.recyclerView = recyclerView;
        this.itemClickListener = itemClickListener;

        gestureDetector = new GestureDetector(context, this);
        recyclerView.addOnItemTouchListener(this);
    }

    public boolean onSingleTapUp(MotionEvent e) { return true; }

    public void onLongPress(MotionEvent e) {
        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null && itemClickListener != null) {
            itemClickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
        }
    }

    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && itemClickListener != null && gestureDetector.onTouchEvent(e)) {
            itemClickListener.onClick(child, rv.getChildLayoutPosition(child));
        }
        return false;
    }

    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    public static void setup(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        new RecyclerTouchListener(context, recyclerView, listener);
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
