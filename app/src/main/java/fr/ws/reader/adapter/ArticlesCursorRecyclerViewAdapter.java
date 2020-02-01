package fr.ws.reader.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;


import fr.ws.reader.R;
import fr.ws.reader.bean.Article;
import fr.ws.reader.view.ArticlesViewHolder;


public class ArticlesCursorRecyclerViewAdapter extends CursorRecyclerViewAdapter {
    private WebView articleView;
    private Cursor mc;
    public ArticlesCursorRecyclerViewAdapter(Context context, Cursor cursor, FragmentManager fm) {
        super(context, cursor,fm);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.article_favorite_row, parent, false);
        return new ArticlesViewHolder (v,mfm,mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final Cursor cursor, final int position) {

        ArticlesViewHolder holder = (ArticlesViewHolder) viewHolder;
        //cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                //show article content inside a dialog
                articleView = new WebView (mContext);

                articleView.getSettings().setLoadWithOverviewMode(true);

                String title = cursor.getString(cursor.getColumnIndex(Article.TITLE));
                String content = cursor.getString(cursor.getColumnIndex(Article.DESCIRPTION));
                String link = cursor.getString(cursor.getColumnIndex(Article.LINK));
                articleView.getSettings().setJavaScriptEnabled(true);
                articleView.setHorizontalScrollBarEnabled(false);
                articleView.setWebChromeClient(new WebChromeClient ());
                articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + "</br><a href="+link +">Lire l'article sur une page web</a></br>"+content, null, "utf-8", null);

                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
                alertDialog.setTitle(title);
                alertDialog.setView(articleView);
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}