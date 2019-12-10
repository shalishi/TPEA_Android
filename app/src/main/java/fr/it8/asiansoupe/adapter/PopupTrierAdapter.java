package fr.it8.asiansoupe.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.Trier;

/**
 * 筛选adapter
 */
public class PopupTrierAdapter extends BaseRecyclerViewAdapter<Trier> {

    public PopupTrierAdapter(RecyclerView view, List<Trier> dataList, int layoutId) {
        super(view, dataList, layoutId);
    }

    @Override
    public void convert(ViewHolderHelper helper, Trier trier, int position) {
        helper.setText(R.id.tv_content, trier.getName());
    }
}
