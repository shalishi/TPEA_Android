package fr.it8.asiansoupe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.Category;
import fr.it8.asiansoupe.bean.Product;
import fr.it8.asiansoupe.util.SwipeToDeleteCallback;

/**
 * 分类列表adapter（发布商品中）
 */
public class CategoryListAdapter extends BaseRecyclerViewAdapter<Category> implements ProductAdapter.OnProductOperationListener{

    private ProductAdapter adapter;
    private Context context;
    private RecyclerView lv_product_type;
    private OnProductOperationListener onProductOperationListener;
    public CategoryListAdapter(RecyclerView view, List<Category> dataList, int layoutId, Context context) {
        super(view, dataList, layoutId);
        this.context=context;
    }

    @Override
    public void convert(ViewHolderHelper helper, Category category, int position) {

        helper.setText(R.id.tv_category, category.getName());
        helper.setVisible(R.id.iv_right, false);
        helper.setVisible(R.id.iv_choose, category.isChoose());
        lv_product_type = (RecyclerView)helper.getView(R.id.lv_product_type);
        adapter = new ProductAdapter(lv_product_type, category.getList(), R.layout.item_product,ProductAdapter.NORMAL,context);
        adapter.setOnProductOperationListener(this);
        lv_product_type.setLayoutManager(new LinearLayoutManager(context));

    }

    public void setOnProductOperationListener(OnProductOperationListener onProductOperationListener) {
        this.onProductOperationListener = onProductOperationListener;
    }

    @Override
    public void updateQty(Product product, int position) {
        if (onProductOperationListener != null)
            onProductOperationListener.updateQty_C(product, position);
    }

    public interface OnProductOperationListener {

        void updateQty_C(Product product, int position);

    }


}
