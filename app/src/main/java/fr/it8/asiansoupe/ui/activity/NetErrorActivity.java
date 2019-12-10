package fr.it8.asiansoupe.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.json.JSONException;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.base.BaseActivity;

/**
 * 网络失败页面
 */
public class NetErrorActivity extends BaseActivity {

    @SuppressLint("StaticFieldLeak")
    public static NetErrorActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_error);
    }

    @Override
    protected void initView() {
        instance = this;
    }

    @Override
    protected void initData() {

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

    @Override
    public void onBackPressed() {

    }
}
