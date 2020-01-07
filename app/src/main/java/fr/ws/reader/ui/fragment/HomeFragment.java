package fr.ws.reader.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.adapter.ArticleAdapter;
import fr.ws.reader.adapter.CategoryAdapter;
import fr.ws.reader.adapter.CategoryListAdapter;
import fr.ws.reader.adapter.PopupCategroyAdapter;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Article;
import fr.ws.reader.bean.Category;
import fr.ws.reader.bean.Product;
import fr.ws.reader.interfaces.OnItemClickListener;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.ui.activity.HistoryActivity;
import fr.ws.reader.util.DatabaseHandler;
import fr.ws.reader.util.L;
import fr.ws.reader.view.MyPopupWindow;
import fr.ws.reader.view.MySwipeRefreshLayout;

/**
 * 首页fragment
 */
public class HomeFragment extends BaseFragment implements CategoryListAdapter.OnProductOperationListener {

    private static final String TAG = "HomeFragment";

    @BindView(R.id.lv_home_type)
    RecyclerView lvHomeType;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout refreshLayout;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_cart)
    TextView tv_cart;
    @BindView(R.id.cart_badge)
    TextView cart_badge;
    @BindView(R.id.btn_category)
    LinearLayout btn_category;
    @BindView(R.id.btn_cart)
    RelativeLayout btn_cart;
    private MyPopupWindow categoriesWindow;
    private CategoryAdapter adapter;
    private CategoryListAdapter categoryListAdapter;
    private List<Category> categories;
    private List<Article> articles;
    private DatabaseHandler dbHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        dbHandler = new DatabaseHandler(getContext());
        //refreshLayout.setOnRefreshListener(refreshListener);


        lvHomeType.setLayoutManager(new LinearLayoutManager(getActivity()));

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontello.ttf");
        tv_category.setTypeface(font);
        tv_category.setText("\ue822");
        tv_cart.setTypeface(font);
        tv_cart.setText("\ue86a");
    }

    @Override
    public void onResume() {
        super.onResume();
        //refreshLayout.startRefresh(refreshListener);
        refreshLayout.stopRefresh();
    }

    @Override
    protected void initData() {
        adapter = new CategoryAdapter(lvHomeType, new ArrayList<Category>(), R.layout.item_home_type);
        //categoryListAdapter = new CategoryListAdapter(lvHomeType, new ArrayList<Category>(), R.layout.item_category,getContext());
        //categoryListAdapter.setOnProductOperationListener(this);
        btn_category.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createCategoryWindow();
            }
        });
        btn_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(HistoryActivity.class);
            }
        });

        ArrayList<Article> articles = new ArrayList<>();
        ArticleAdapter articleAdapterAdapter = new ArticleAdapter(articles,R.layout.article_row,getActivity(),getFragmentManager());
        lvHomeType.setAdapter(articleAdapterAdapter);
        //refreshLayout.startRefresh(refreshListener);
        isFirstLoad = false;
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        JSONObject js = new JSONObject(data);
        switch (id) {
            //分类列表
            case QRequest.GOOD_TYPE_LIST:
                L.d(TAG, data);
                categories = getGson().fromJson(
                        js.optString("list"), new TypeToken<List<Category>>() {
                        }.getType());

                if (isListNotNull(categories)) {
                    adapter.replaceAll(categories);
                    MainApplication.app.setCategories(categories);
                }
                break;
                //product list
            case QRequest.PRODUCT_LIST:
                L.d(TAG, data);
                categories = getGson().fromJson(
                        js.optString("list"), new TypeToken<List<Category>>() {
                        }.getType());
                if (isListNotNull(categories)) {
                    categoryListAdapter.replaceAll(categories);
                    MainApplication.app.setCategories(categories);
                }
                break;
            default:
                break;
        }
        refreshLayout.stopRefresh();
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {
        showError(mRootView, msg);
        refreshLayout.stopRefresh();
    }

    @Override
    public void onNetError(int id) {
        showNetError(mRootView);
        refreshLayout.stopRefresh();
    }

    /**
     * 刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            QRequest.productTypeList(callback);
            QRequest.productList(callback);
        }
    };

    /**
     * 子分类列表显示popup
     */
    private void createCategoryWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_popup_array, null);
        RecyclerView lvArray = popupView.findViewById(R.id.lv_array);
        lvArray.setLayoutManager(new LinearLayoutManager(getContext()));
        View outside = popupView.findViewById(R.id.layout_outside);
        final LinearLayoutManager llm = (LinearLayoutManager) lvHomeType.getLayoutManager();
        final PopupCategroyAdapter adapter = new PopupCategroyAdapter(lvArray, MainApplication.app.getCategories(), R.layout.item_popup_array);
        adapter.setOnItemClickListener(new OnItemClickListener<Category>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Category category, int position) {
                categoriesWindow.dismiss();
                llm.scrollToPositionWithOffset(position, 0);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Category category, int position) {
                return false;
            }
        });
        if (categoriesWindow == null) {
            categoriesWindow = new MyPopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            categoriesWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
            categoriesWindow.setFocusable(true);
            categoriesWindow.setOutsideTouchable(true);
            categoriesWindow.update();
            categoriesWindow.showAsDropDown(view_line);
        } else {
            categoriesWindow.showAsDropDown(view_line);
        }
        //点击其他地方消去PopupWindow
        outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoriesWindow.isShowing())
                    categoriesWindow.dismiss();
            }
        });
    }


    @Override
    public void updateQty_C(Product product, int position) {

    }

}
