package fr.it8.asiansoupe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.app.Constants;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.Product;
import fr.it8.asiansoupe.util.DatabaseHandler;
import fr.it8.asiansoupe.util.L;
import fr.it8.asiansoupe.util.Utils;

/**
 * 产品列表adapter
 * tv_name  iv_pic  tv_price  tv_stock  tv_delete  tv_detail
 */
public class CartProductAdapter extends BaseRecyclerViewAdapter<Product> {

    public static final String TAG = "ProductAdapter";
    public static final int NORMAL = 1;    //普通产品列表
    public static final int FAVORITE = 2;    //我的收藏产品列表

    private OnProductOperationListener onProductOperationListener;
    private int flag;
    private Context context;

    public CartProductAdapter(RecyclerView view, List<Product> dataList, int layoutId, int flag, Context context) {
        super(view, dataList, layoutId);
        this.flag = flag;
        this.context=context;
    }

    @Override
    public void convert(final ViewHolderHelper helper, final Product product, final int position) {
        if (product != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontello.ttf");
            helper.setText(R.id.tv_name, product.getName()).setText(R.id.tv_price, Utils.getPrice(product.getPrice()))
                    .setText(R.id.tv_qty, product.getQty().toString()).setText(R.id.tv_qty_text, "Qty : ").setText(R.id.tv_sum,Utils.getPrice((product.getQty()*Float.parseFloat(product.getPrice()))+""));

            String url = Constants.SERVER_IMAGE_ADDRESS + product.getImg();
            helper.setImageThumb(R.id.iv_pic, url);
            final DatabaseHandler dbHandler = new DatabaseHandler(context);

         /*
            //查看详情
            helper.setOnClickListener(R.id.tv_detail, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProductOperationListener != null)
                        onProductOperationListener.onDetail(product, position);
                }
            });
            //点击图片查看详情
            helper.setOnClickListener(R.id.iv_product, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProductOperationListener != null)
                        onProductOperationListener.onDetail(product, position);
                }
            });

            */
        }
    }

    public void setOnProductOperationListener(OnProductOperationListener onProductOperationListener) {
        this.onProductOperationListener = onProductOperationListener;
    }

    public interface OnProductOperationListener {

        void onDetail(Product product, int position);

        void addFavorite(Product product, int position);

        void deleteFavorite(Product product, int position);
    }
}
