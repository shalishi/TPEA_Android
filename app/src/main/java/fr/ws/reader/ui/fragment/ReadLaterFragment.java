package fr.ws.reader.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.adapter.ArticleAdapter;
import fr.ws.reader.adapter.ArticlesCursorRecyclerViewAdapter;
import fr.ws.reader.adapter.FeedSimpleAdapter;
import fr.ws.reader.adapter.PopupFeedsAdapter;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Article;
import fr.ws.reader.bean.Feed;
import fr.ws.reader.interfaces.OnItemClickListener;
import fr.ws.reader.ui.dialog.deleteArticleDialogFragment;
import fr.ws.reader.util.Parser;
import fr.ws.reader.util.mplrssProvider;
import fr.ws.reader.view.MyPopupWindow;
import fr.ws.reader.view.MySwipeRefreshLayout;


/**
 * 首页fragment
 */
public class ReadLaterFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, deleteArticleDialogFragment.DeleteArticleDialogListener,DeleteArticlesByDateDialogFragment.DeleteArticlesByDateDialogListener{

        private static final String TAG = "MydownloadsActivity";

    @BindView(R.id.lv_readerlater)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.find_text)
    EditText find_text;
    @BindView(R.id.iv_cart)
    ImageView tv_cart;
    @BindView(R.id.btn_Feed)
    LinearLayout btn_Feed;
    @BindView(R.id.btn_cart)
    RelativeLayout btn_cart;
    private MyPopupWindow feedsWindow;
    private FeedSimpleAdapter FeedListAdapter;
    private List<Feed> feeds;
    private List<Article> articles;
    private ArticleAdapter articleAdapter;
    public ContentResolver resolver;
    private Context mcontext;
    protected DeleteArticlesByDateDialogFragment deleteArticlesByDateDialogFragment_m;
    public final int offset = 30;
    private int page = 0;
    private boolean loadingMore = false;
    private Toast shortToast;

    /**
     * 初始化layout的id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_readlater;
    }

    @Override
        protected void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
//        ArticlesCursorRecyclerViewAdapter mAdapter = new ArticlesCursorRecyclerViewAdapter(mcontext, null,fm);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//
//        int itemsCountLocal = getItemsCountLocal();
//        if (itemsCountLocal == 0) {
//            //fillTestElements();
//        }

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                int maxPositions = layoutManager.getItemCount();
//
//                if (lastVisibleItemPosition == maxPositions - 1) {
//                    if (loadingMore)
//                        return;
//
//                    loadingMore = true;
//                    page++;
//                    getActivity ().getSupportLoaderManager().restartLoader(0, null, (LoaderManager.LoaderCallbacks<Object>) getActivity ());
//                }
//            }
//        });

//        getActivity().getSupportLoaderManager().restartLoader(0, null, this);


        }

        @Override
        public void onResume() {
            super.onResume();

        }

        private int getItemsCountLocal() {
            int itemsCount = 0;

            Cursor query = mcontext.getContentResolver().query(mplrssProvider.urlForItems(Article.NAME,0), null, null, null, null);
            if (query != null) {
                itemsCount = query.getCount();
                query.close();
            }
            return itemsCount;
        }


        @Override
        protected void initData() {
            FeedListAdapter = new FeedSimpleAdapter(mRecyclerView, new ArrayList<Feed>(), R.layout.item_feed,getContext());
            btn_Feed.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    createFeedWindow();
                }
            });
            btn_cart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });

            ArrayList<Article> articles = new ArrayList<>();
            articleAdapter  = new ArticleAdapter(articles,R.layout.article_row,getActivity(),getFragmentManager());
            mRecyclerView.setAdapter(articleAdapter);
            isFirstLoad = false;
            loadFeed("http://www.aweber.com/blog/feed/");
        }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
            switch (id) {
                case 0:
                    return new CursorLoader (mcontext, mplrssProvider.urlForItems(Article.NAME,offset * page), null, null, null, null);
                default:
                    throw new IllegalArgumentException("no id handled!");
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//            switch (loader.getId()) {
//                case 0:
//                    Log.d(TAG, "onLoadFinished: loading MORE");
//                    shortToast.setText("Loading..." );
//                    shortToast.show();
//
//                    Cursor cursor = ((ArticlesCursorRecyclerViewAdapter) mRecyclerView.getAdapter()).getCursor();
//
//                    //fill all exisitng in adapter
//                    MatrixCursor mx = new MatrixCursor(new String[]{Article._ID,Article.TITLE,Article.DESCIRPTION,Article.CATEGORY,Article.PUBDATE,Article.IMAGE,Article.LINK});
//                    fillMx(cursor, mx);
//
//                    //fill with additional result
//                    fillMx(data, mx);
//
//                    ((ArticlesCursorRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(mx);
//
//
//                    handlerToWait.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            loadingMore = false;
//                        }
//                    }, 2000);
//
//                    break;
//                default:
//                    throw new IllegalArgumentException("no loader id handled!");
//            }
        }



        private Handler handlerToWait = new Handler();

        private void fillMx(Cursor data, MatrixCursor mx) {
            if (data == null)
                return;

            data.moveToPosition(-1);
            while (data.moveToNext()) {
                mx.addRow(new Object[]{
                        data.getString(data.getColumnIndex(Article._ID)),
                        data.getString(data.getColumnIndex(Article.TITLE)),
                        data.getString(data.getColumnIndex(Article.DESCIRPTION)),
                        data.getString(data.getColumnIndex(Article.CATEGORY)),
                        data.getString(data.getColumnIndex(Article.PUBDATE)),
                        data.getString(data.getColumnIndex(Article.IMAGE)),
                        data.getString(data.getColumnIndex(Article.LINK))
                });
            }
        }


        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }

        @Override
        public void onFinishEditDialog(int article_id) {
            if(article_id>0){
                String  [] arguments = new String [1];
                arguments[0] = article_id+"";
                String selectionclause = "_ID = ?";
                mcontext.getContentResolver().delete(mplrssProvider.urlForItems(Article.NAME,0),selectionclause, arguments);
                Log.d(TAG,"Supression of article id : "+article_id);
//                restart();
            }

        }

        public void deleteArticleByDate(String date_b, String date_e) throws ParseException {
            String  [] arguments = new String [2];
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
            SimpleDateFormat Date_begin = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat Date_end = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            String date_b_m = sdfDate.format(Date_begin.parse(date_b));
            String date_e_m = sdfDate.format(sdfDate.parse(date_e+" 23:59:59"));
            arguments[0] = date_b_m;
            arguments[1]= date_e_m;
            String selectionclause = "date > ? AND date < ?";
            mcontext.getContentResolver().delete(mplrssProvider.urlForItems(Article.NAME,0),selectionclause, arguments);
            Log.d(TAG,"Supression of article date between : "+date_b+" and "+date_b);
        }

        public void cancelEditDate(View v){
            deleteArticlesByDateDialogFragment_m.dismiss();
        }

        public void finishEditDate(View v) throws ParseException {
            EditText date_b = (EditText) deleteArticlesByDateDialogFragment_m.getDialog().findViewById(R.id.date_b);
            EditText date_e = (EditText) deleteArticlesByDateDialogFragment_m.getDialog().findViewById(R.id.date_e);
            if (date_b != null &&date_e!=null) {
                deleteArticleByDate(date_b.getText().toString(),date_e.getText().toString());
            }
            deleteArticlesByDateDialogFragment_m.dismiss();
//            restart();
        }

        @Override
        public void onFinishEditDateByDateDialog(String inputText) {

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

    public void loadFeed(String urlString) {
        //refreshLayout.startRefresh(refreshListener);
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                //list is an Array List with all article's information
                //set the adapter to recycler view
                FragmentManager fm = getFragmentManager();
                articleAdapter = new ArticleAdapter(list, R.layout.article_row, getContext(),fm);
                mRecyclerView.setAdapter(articleAdapter);
                mSwipeRefreshLayout.stopRefresh();

            }

            //what to do in case of error
            @Override
            public void onError() {
                mSwipeRefreshLayout.stopRefresh();
                showError(mRootView, "Unable to load data.");
                Log.i("Unable to load ", "articles");
            }
        });
    }

    private void createFeedWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_popup_array, null);
        RecyclerView lvArray = popupView.findViewById(R.id.lv_array);
        lvArray.setLayoutManager(new LinearLayoutManager(getContext()));
        View outside = popupView.findViewById(R.id.layout_outside);
        final LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        final PopupFeedsAdapter adapter = new PopupFeedsAdapter(lvArray, MainApplication.app.getSubscribedFeeds(), R.layout.item_popup_array);
        adapter.setOnItemClickListener(new OnItemClickListener<Feed> () {
            @Override
            public void onItemClick(ViewGroup parent, View view, Feed Feed, int position) {
                feedsWindow.dismiss();
                loadFeed(Feed.getLink());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Feed Feed, int position) {
                return false;
            }
        });
        if (feedsWindow == null) {
            feedsWindow = new MyPopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            feedsWindow.setBackgroundDrawable(new ColorDrawable (Color.parseColor("#50000000")));
            feedsWindow.setFocusable(true);
            feedsWindow.setOutsideTouchable(true);
            feedsWindow.update();
            feedsWindow.showAsDropDown(view_line);
        } else {
            feedsWindow.showAsDropDown(view_line);
        }
        //dismiss PopupWindow
        outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedsWindow.isShowing())
                    feedsWindow.dismiss();
            }
        });
    }
}
