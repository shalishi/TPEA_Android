package fr.ws.reader.ui.fragment;

import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.adapter.CategoryAdapter;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Article;
import fr.ws.reader.bean.Category;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.view.MyPopupWindow;
import fr.ws.reader.view.MySwipeRefreshLayout;

/**
 * 首页fragment
 */
public class ReadLaterFragment extends BaseFragment{

    private static final String TAG = "HomeFragment";

    @BindView(R.id.lv_home_type)
    RecyclerView lvHomeType;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout refreshLayout;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.tv_Feed)
    TextView tv_Feed;
    @BindView(R.id.tv_cart)
    TextView tv_cart;
    @BindView(R.id.btn_cart)
    RelativeLayout btn_cart;
    private MyPopupWindow categoriesWindow;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private List<Article> articles;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_readlater;
    }

    @Override
    protected void initView() {
        //refreshLayout.setOnRefreshListener(refreshListener);
        lvHomeType.setLayoutManager (new LinearLayoutManager (getActivity ()));
        Typeface font = Typeface.createFromAsset (getActivity ().getAssets (), "fontello.ttf");
        tv_Feed.setTypeface (font);
        tv_Feed.setText ("\ue822");
        tv_cart.setTypeface (font);
        tv_cart.setText ("\ue86a");
    }

    @Override
    public void onResume() {
        super.onResume ();
        //refreshLayout.startRefresh(refreshListener);
        refreshLayout.stopRefresh ();
    }

    @Override
    protected void initData() {
//        adapter = new CategoryAdapter (lvHomeType, new ArrayList<Category> (), R.layout.item_home_type);
//        //refreshLayout.startRefresh(refreshListener);
//        isFirstLoad = false;
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        JSONObject js = new JSONObject (data);
        switch (id) {
            default:
                break;
        }
        refreshLayout.stopRefresh ();
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {
        showError (mRootView, msg);
        refreshLayout.stopRefresh ();
    }

    @Override
    public void onNetError(int id) {
        showNetError (mRootView);
        refreshLayout.stopRefresh ();
    }

    /**
     * 刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener () {
        @Override
        public void onRefresh() {
            QRequest.productTypeList (callback);
            QRequest.productList (callback);
        }
    };

}
