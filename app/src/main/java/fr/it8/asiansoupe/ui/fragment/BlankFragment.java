package fr.it8.asiansoupe.ui.fragment;

import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.base.BaseFragment;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class BlankFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blank;
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
