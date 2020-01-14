package fr.ws.reader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.adapter.FeedAdapter;
import fr.ws.reader.adapter.RecommendAdapter;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Feed;
import fr.ws.reader.bean.Trier;
import fr.ws.reader.view.MyPopupWindow;
import fr.ws.reader.view.MySwipeRefreshLayout;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class RecommendFragment extends BaseFragment{

    private static final String TAG = "RecommendFragment";

    @BindView(R.id.tv_trier)
    IconTextView tvTrier;
    @BindView(R.id.lv_recommend)
    RecyclerView lvRecommend;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout refreshLayout;

    private List<Feed> feeds;
    private FeedAdapter adapter;
    private List<Trier> triers;
    private MyPopupWindow trierWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.lv_recommend);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // this is data fro recycler view
      //  int entity_id, String title, String link, String description, String content, String image, String categories_string, List<String> categories
                ArrayList<Feed> data = new ArrayList<Feed> ();
                data.add (new Feed(1,"Indigo","https://www.androidauthority.com/feed","description","","categories"));
        data.add (new Feed(2,"Red", "https://www.androidauthority.com/feed","description","","categories"));
        data.add (new Feed(3,"Blue","https://www.androidauthority.com/feed","description","","categories"));
        data.add (new Feed(4,"Green","https://www.androidauthority.com/feed","description", "","categories"));
        data.add (new Feed(5,"Amber","https://www.androidauthority.com/feed","description", "","categories"));
        data.add (new Feed(6,"Deep Orange","https://www.androidauthority.com/feed","description", "","categories"));



        // 3. create an adapter
        RecommendAdapter mAdapter = new RecommendAdapter(data,R.layout.feed_row,getContext (),getFragmentManager ());
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator ());

        return rootView;
    }

    @Override
    protected void initView() {
        getTitleBar().setCustomTitle(getString(R.string.title_recommend), false, null).setViewLine(false);
      //  refreshLayout.setOnRefreshListener(refreshListener);
        tvTrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createTrierWindow();
            }
        });
        lvRecommend.setLayoutManager(new LinearLayoutManager (getActivity()));
        triers = MainApplication.app.getTriers();
    }


    @Override
    protected void initData() {
        adapter = new FeedAdapter (new ArrayList<Feed> (), R.layout.item_product,getContext (),getFragmentManager ());
//        adapter.setOnItemClickListener(this);
//        adapter.setOnProductOperationListener(this);
    }

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
