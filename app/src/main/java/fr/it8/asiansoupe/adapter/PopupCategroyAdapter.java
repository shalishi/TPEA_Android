package fr.it8.asiansoupe.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.Category;

/**
 * 首页排序下拉菜单adapter
 * tv_content
 * iv_choose
 */
public class PopupCategroyAdapter extends BaseRecyclerViewAdapter<Category> {

    public PopupCategroyAdapter(RecyclerView view, List<Category> dataList, int layoutId) {
        super(view, dataList, layoutId);
    }

    @Override
    public void convert(ViewHolderHelper helper, Category category, int position) {
        helper.setText(R.id.tv_content, category.getName());
    }
}
