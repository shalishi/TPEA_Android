package fr.it8.asiansoupe.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import java.util.List;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.app.Constants;
import fr.it8.asiansoupe.base.BaseRecyclerViewAdapter;
import fr.it8.asiansoupe.base.ViewHolderHelper;
import fr.it8.asiansoupe.bean.ProductImage;
import fr.it8.asiansoupe.request.QUrlName;
import fr.it8.asiansoupe.util.L;

/**
 * 产品发布图片列表adapter
 */
public class ProductReleasePicAdapter extends BaseRecyclerViewAdapter<ProductImage> {

    public static final int PRODUCT_URL = 1;
    public static final int PRODUCT_PATH = 2;

    private int mFlag;

    public ProductReleasePicAdapter(RecyclerView view, List<ProductImage> dataList, int layoutId, int flag) {
        super(view, dataList, layoutId);
        this.mFlag = flag;
    }

    @Override
    public void convert(ViewHolderHelper helper, ProductImage productImage, int position) {
        if (mFlag == PRODUCT_URL) {
            //网络图片
            if (!productImage.getUrl().isEmpty()) {
                L.d(productImage.getUrl());
                String cate = QUrlName.PRODUCT_PIC + productImage.getUrl();
                String base = Base64.encodeToString(cate.getBytes(), Base64.DEFAULT).replaceAll("\\r|\\n", "");
                String url = Constants.SERVER_IMAGE_ADDRESS + base + "/200/200";
                helper.setImageThumb(R.id.iv_product_picture, url);
            }
        } else if (mFlag == PRODUCT_PATH) {
            helper.setImageThumb(R.id.iv_product_picture, productImage.getPath());
        }
    }
}
