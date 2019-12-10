package fr.ws.reader.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import fr.ws.reader.R;
import fr.ws.reader.base.BaseRecyclerViewAdapter;
import fr.ws.reader.base.ViewHolderHelper;
import fr.ws.reader.bean.Feed;

public class SubscriptionAdapter extends BaseRecyclerViewAdapter<Feed>{

    public SubscriptionAdapter(RecyclerView view, List<Feed> dataList, int layoutId) {
        super (view, dataList, layoutId);
    }

    @Override
    public void convert(ViewHolderHelper helper, Feed feed, int position) {
        helper.setText(R.id.title,feed.getTitle ());
        helper.setText(R.id.link,feed.getLink ());
    }
}
