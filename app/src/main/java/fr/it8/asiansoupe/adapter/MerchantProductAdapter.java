package fr.it8.asiansoupe.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;

import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.app.Constants;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.Product;
import fr.it8.asiansoupe.request.QUrlName;
import fr.it8.asiansoupe.util.Utils;

/**
 * 单个商家产品列表adapter
 */
public class MerchantProductAdapter extends BaseRecyclerViewAdapter<Product> {

    private ProductAdapter.OnProductOperationListener listener;

    public MerchantProductAdapter(RecyclerView view, List<Product> dataList, int layoutId, ProductAdapter.OnProductOperationListener listener) {
        super(view, dataList, layoutId);
        this.listener = listener;
    }

    @Override
    public void convert(ViewHolderHelper helper, final Product product, final int position) {
        if (product != null) {
            /*helper.setText(R.id.tv_name, product.getName()).setText(R.id.tv_price, Utils.getPrice(product.getPrice()))
                    .setText(R.id.tv_stock, "Stock: " + product.getQty());
            //产品图片
            if (product.getImages().size() > 0) {
                String cate = QUrlName.PRODUCT_PIC + product.getImages().get(0).getValue();
                String base = Base64.encodeToString(cate.getBytes(), Base64.DEFAULT).replaceAll("\\r|\\n", "");
                String url = Constants.SERVER_IMAGE_ADDRESS + base + "/160/160";
                helper.setImageThumb(R.id.iv_pic, url);
            }
            //收藏，查看
            helper.setText(R.id.tv_favorite, "{icon-heart} " + product.getWishlist())
                    .setText(R.id.tv_see, "{icon-eye} " + product.getVisited());
            //查看详情
            helper.setOnClickListener(R.id.tv_detail, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDetail(product, position);
                }
            });
            //点击图片查看详情
            helper.setOnClickListener(R.id.iv_pic, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDetail(product, position);
                }
            });
            */
        }
    }
}
