package ph.easybus.ebbusseatplan.layoutmanagers;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpannedGridLayoutManager extends RecyclerView.LayoutManager {

    private Context context;

    private int sizeInDp = 0;
    private int cols = 0, rows = 0;
    private GridSpanLookup gridSpanLookup;

    public SpannedGridLayoutManager(Context context, int sizeOfCell, int cols, int rows,
                                    GridSpanLookup gridSpanLookup) {
        this.context = context;
        this.cols = cols;
        this.rows = rows;
        this.gridSpanLookup = gridSpanLookup;

        sizeInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                sizeOfCell, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler,
                          @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);

        setMeasuredDimension(cols * sizeInDp, rows * sizeInDp);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //super.onLayoutChildren(recycler, state);

        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < getItemCount(); i++) {
            ViewGroup view = (ViewGroup)recycler.getViewForPosition(i);
            addView(view);

            SpanInfo span = gridSpanLookup.getSpanInfo(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            int width = span.getColumnSpan() * sizeInDp, height = span.getRowSpan() * sizeInDp;
            params.width = width;
            params.height = height;

            int l = span.getX() * sizeInDp, t = span.getY() * sizeInDp;
            int r = l + width, b = t + height;

            measureChildWithMargins(view, width, height);
            layoutDecoratedWithMargins(view, l, t, r, b);

            /*
            layoutDecoratedWithMargins(view, span.getX() * sizeInDp, span.getY() * sizeInDp,
                    span.getX() * sizeInDp + (span.getColumnSpan() * sizeInDp),
                    span.getY() * sizeInDp + (span.getRowSpan() * sizeInDp));
            */
        }

        List<RecyclerView.ViewHolder> scrapListCopy = recycler.getScrapList();
        for (int i = 0; i < scrapListCopy.size(); i++) {
            recycler.recycleView(scrapListCopy.get(i).itemView);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public interface GridSpanLookup {
        SpanInfo getSpanInfo(int position);
    }

    public static class SpanInfo {

        private int x, y;
        private int columnSpan, rowSpan;

        public SpanInfo(int x, int y, int columnSpan, int rowSpan) {
            this.x = x;
            this.y = y;
            this.columnSpan = columnSpan;
            this.rowSpan = rowSpan;
        }

        public static final SpanInfo SINGLE_CELL = new SpanInfo(0, 0, 1, 1);

        public int getX() { return x; }
        public int getY() { return y; }
        public int getColumnSpan() { return columnSpan; }
        public int getRowSpan() { return rowSpan; }

        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
        public void setColumnSpan(int columnSpan) { this.columnSpan = columnSpan; }
        public void setRowSpan(int rowSpan) { this.rowSpan = rowSpan; }
    }
}
