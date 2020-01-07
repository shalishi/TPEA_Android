package fr.ws.reader.ui.fragment;

import org.json.JSONException;

import fr.ws.reader.R;
import fr.ws.reader.base.BaseFragment;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class RecommendeFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {

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
}
