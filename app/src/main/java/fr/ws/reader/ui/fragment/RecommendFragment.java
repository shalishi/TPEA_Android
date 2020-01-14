package fr.ws.reader.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.adapter.ArticleAdapter;
import fr.ws.reader.adapter.CategoryAdapter;
import fr.ws.reader.adapter.FeedAdapter;
import fr.ws.reader.adapter.PopupCategroyAdapter;
import fr.ws.reader.adapter.RecommendAdapter;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Article;
import fr.ws.reader.bean.Category;
import fr.ws.reader.bean.Feed;
import fr.ws.reader.bean.Trier;
import fr.ws.reader.interfaces.OnItemClickListener;
import fr.ws.reader.util.Parser;
import fr.ws.reader.view.MyPopupWindow;
import fr.ws.reader.view.MySwipeRefreshLayout;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class RecommendFragment extends BaseFragment{

    private static final String TAG = "RecommendFragment";

    @BindView(R.id.btn_category)
    IconButton btnCategory;
    @BindView(R.id.lv_recommend)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout refreshLayout;
    @BindView(R.id.view_line)
    View view_line;

    private List<Feed> feeds;
    private FeedAdapter adapter;
    private List<Category> categories;
    private MyPopupWindow categoryWindow;
    private CategoryAdapter categoryAdapter;
    private RecommendAdapter mAdapter;
    private ArrayList<Feed> data;
    private String categoryType = "-1";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }


    public void initlist(){
            recyclerView.setLayoutManager (new LinearLayoutManager (getActivity ()));

            // this is data fro recycler view
            //  int entity_id, String title, String link, String description, String content, String image, String categories_string, List<String> categories
            data = new ArrayList<Feed> ();
            data.add (new Feed (1, "Indigo", "https://www.androidauthority.com/feed", "fiance", "", "fiance"));
            data.add (new Feed (2, "Red", "https://www.androidauthority.com/feed", "arts", "", "arts"));
            data.add (new Feed (3, "Blue", "https://www.androidauthority.com/feed", "education", "", "education"));
            data.add (new Feed (4, "Green", "https://www.androidauthority.com/feed", "fiance", "", "fiance"));
            data.add (new Feed (5, "Amber", "https://www.androidauthority.com/feed", "fiance", "", "fiance"));
            data.add (new Feed (6, "Deep Orange", "https://www.androidauthority.com/feed", "arts", "", "arts"));

            // 3. create an adapter
            mAdapter = new RecommendAdapter (data, R.layout.feed_row, getContext (), getFragmentManager ());
            // 4. set adapter
            recyclerView.setAdapter (mAdapter);
            // 5. set item animator to DefaultAnimator
            recyclerView.setItemAnimator (new DefaultItemAnimator ());
            refreshLayout.stopRefresh ();
    }

    @Override
    protected void initView() {
        getTitleBar().setCustomTitle(getString(R.string.title_recommend), false, null).setViewLine(false);
      //  refreshLayout.setOnRefreshListener(refreshListener);
        btnCategory.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategoryWindow();
            }
        });
        initlist ();
    }

    private void createCategoryWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_popup_array, null);
        RecyclerView lvArray = popupView.findViewById(R.id.lv_array);
        lvArray.setLayoutManager(new LinearLayoutManager(getContext()));
        View outside = popupView.findViewById(R.id.layout_outside);
        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        final PopupCategroyAdapter adapter = new PopupCategroyAdapter(lvArray, MainApplication.app.getCategories (), R.layout.item_popup_array);
        adapter.setOnItemClickListener(new OnItemClickListener<Category> () {
            @Override
            public void onItemClick(ViewGroup parent, View view, Category category, int position) {
                categoryWindow.dismiss();
                categoryType = category.getEntity_id ();
                loadFeedByCategory(category.getEntity_id ());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Category category, int position) {
                return false;
            }
        });
        if (categoryWindow == null) {
            categoryWindow = new MyPopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            categoryWindow.setBackgroundDrawable(new ColorDrawable (Color.parseColor("#50000000")));
            categoryWindow.setFocusable(true);
            categoryWindow.setOutsideTouchable(true);
            categoryWindow.update();
            categoryWindow.showAsDropDown(view_line);
        } else {
            categoryWindow.showAsDropDown(view_line);
        }
        //dismiss PopupWindow
        outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryWindow.isShowing())
                    categoryWindow.dismiss();
            }
        });
    }

    private void loadFeedByCategory(String idCategory) {
        data.clear ();
        data.add (new Feed(3,"Blue","https://www.androidauthority.com/feed","description","",idCategory));
        data.add (new Feed(5,"Amber","https://www.androidauthority.com/feed","description", "",idCategory));
        data.add (new Feed(6,"Deep Orange","https://www.androidauthority.com/feed","description", "",idCategory));
        mAdapter.notifyDataSetChanged ();
    }



    @Override
    protected void initData() {
        adapter = new FeedAdapter (new ArrayList<Feed> (), R.layout.item_product,getContext (),getFragmentManager ());
//        adapter.setOnItemClickListener(this);
//        adapter.setOnProductOperationListener(this);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(categoryType.equals ("-1")){
                initlist ();
            }else{
                loadFeedByCategory(categoryType);
            }
        }
    };

    @Override
    public void onSuccess(String data, int id) throws JSONException {

    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {

    }

    @Override
    public void onNetError(int id) {

    }
}
