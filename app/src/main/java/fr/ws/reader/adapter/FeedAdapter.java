package fr.ws.reader.adapter;

import android.support.v7.widget.RecyclerView;
import java.util.List;
import fr.ws.reader.base.BaseRecyclerViewAdapter;
import fr.ws.reader.base.ViewHolderHelper;
import fr.ws.reader.bean.Feed;

public class FeedAdapter extends BaseRecyclerViewAdapter<Feed> {

    public static final String TAG = "FeedAdapter";
    private OnFeedOperationListener onFeedOperationListener;

    public FeedAdapter(RecyclerView view, List<Feed> dataList, int layoutId) {
        super (view, dataList, layoutId);
    }

    @Override
    public void convert(ViewHolderHelper helper, Feed feed, int position) {
        if (feed != null) {

        }
    }

    public void setOnProductOperationListener(OnFeedOperationListener onFeedOperationListener) {
        this.onFeedOperationListener = onFeedOperationListener;
    }

    public interface OnFeedOperationListener {

        void onDetail(Feed feed, int position);

        void addFavorite(Feed feed, int position);

        void deleteFavorite(Feed feed, int position);
    }
}
