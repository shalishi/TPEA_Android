package fr.ws.reader.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import fr.ws.reader.R;
import fr.ws.reader.app.Constants;
import fr.ws.reader.base.BaseRecyclerViewAdapter;
import fr.ws.reader.base.ViewHolderHelper;
import fr.ws.reader.bean.Product;
import fr.ws.reader.util.DatabaseHandler;
import fr.ws.reader.util.Utils;

/**
 * 产品列表adapter
 * tv_name  iv_pic  tv_price  tv_stock  tv_delete  tv_detail
 */
public class ProductAdapter extends BaseRecyclerViewAdapter<Product> {

    public static final String TAG = "ProductAdapter";
    public static final int NORMAL = 1;    //普通产品列表
    public static final int FAVORITE = 2;    //我的收藏产品列表

    private OnProductOperationListener onProductOperationListener;
    private int flag;
    private Context context;
    private DatabaseHandler dbHandler;
    public ProductAdapter(RecyclerView view, List<Product> dataList, int layoutId, int flag, Context context) {
        super(view, dataList, layoutId);
        this.flag = flag;
        this.context=context;
    }

    @Override
    public void convert(final ViewHolderHelper helper, final Product product, final int position) {
        if (product != null) {
            dbHandler = new DatabaseHandler(context);
            List<Product> cart_products=dbHandler.getAllProducts();
            for(int i=0;i<cart_products.size(); i++){
                if(cart_products.get(i).getEntity_id()==product.getEntity_id()){
                    product.setQty(cart_products.get(i).getQty());
                }
            }


            TextView btn_minus=helper.getView(R.id.tv_minus);
            TextView btn_add=helper.getView(R.id.tv_add);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontello.ttf");
            btn_minus.setTypeface(font);
            btn_minus.setText("\ue834");
            btn_add.setTypeface(font);
            btn_add.setText("\ue831");

            helper.setText(R.id.tv_name, product.getName()).setText(R.id.tv_price, Utils.getPrice(product.getPrice()))
                    .setText(R.id.tv_qty, product.getQty()+"").setText(R.id.tv_qty_text, "Qty : ");

            String url = Constants.SERVER_IMAGE_ADDRESS + product.getImg();
            helper.setImageThumb(R.id.iv_pic, url);
            helper.setOnClickListener(R.id.tv_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setQty(1);
                    dbHandler.addProductToCart(product);
                    TextView qty=(TextView)helper.getView(R.id.tv_qty);
                    int old_qty=Integer.parseInt(qty.getText().toString());
                    helper.setText(R.id.tv_qty,(old_qty+1)+"");
                    if (onProductOperationListener != null)
                        onProductOperationListener.updateQty(product, position);
                }
            });

            helper.setOnClickListener(R.id.tv_minus, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView qty = (TextView) helper.getView(R.id.tv_qty);
                    int old_qty = Integer.parseInt(qty.getText().toString());
                    if (old_qty > 0) {
                        product.setQty(1);
                        dbHandler.MinusProductToCart(product);
                        helper.setText(R.id.tv_qty, (old_qty - 1) + "");
                        if (onProductOperationListener != null)
                            onProductOperationListener.updateQty(product, position);
                    }
                }
            });

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

        void updateQty(Product product, int position);

    }


}
