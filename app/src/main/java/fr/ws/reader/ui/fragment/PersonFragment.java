package fr.ws.reader.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.app.Config;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseFragment;
import fr.ws.reader.bean.Account;
import fr.ws.reader.bean.User;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.ui.activity.HistoryActivity;
import fr.ws.reader.ui.activity.LoginFirstActivity;
import fr.ws.reader.ui.activity.PersonDataActivity;
import fr.ws.reader.util.Utils;

/**
 * 个人fragment
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.layout_person_data)
    RelativeLayout layoutPersonData;
    @BindView(R.id.layout_password_data)
    RelativeLayout layout_password_data;
    @BindView(R.id.layout_code_data)
    RelativeLayout layout_code_data;
    @BindView(R.id.layout_history)
    RelativeLayout layout_history;
    @BindView(R.id.layout_logout)
    LinearLayout layoutLogout;
    @BindView(R.id.layout_person)
    LinearLayout layoutPerson;
    @BindView(R.id.credit)
    TextView credit;
    private User user;
    private Float amount=null;
    private Account account;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initView() {
        getTitleBar().setCustomTitle(getString(R.string.title_person), false, null);
        layoutPersonData.setOnClickListener(this);
        layout_password_data.setOnClickListener(this);
        layout_code_data.setOnClickListener(this);
        layout_history.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        account = MainApplication.app.getAccount();
        if (account != null) {
            //QRequest.getWallet(String.valueOf(account.getEntityId()),account.getTokenPay(),callback);
           // showLoading().show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //QRequest.getWallet(String.valueOf(account.getEntityId()),account.getTokenPay(),callback);
        //showLoading().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人信息
            case R.id.layout_person_data:
                startActivity(PersonDataActivity.class);
                break;
            case R.id.layout_password_data:
                //startActivity(ChangePwdActivity.class);
                break;
            case R.id.layout_code_data:
                //startActivity(ChangeCodeActivity.class);
                break;
            case R.id.layout_history:
                startActivity(HistoryActivity.class);
                break;
            //退出登录
            case R.id.layout_logout:
                Utils.clearInfo(getActivity());
                getBaseActivity().sendBroadcast(Config.ACTION_LOGOUT);
                startActivity(LoginFirstActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void lazyLoad() {
      //  refreshUserInfo();
    }

    @Override
    protected void userVisible() {
        refreshUserInfo();
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        JSONObject js = new JSONObject(data);
        switch (id) {
            //刷新个人数据信息
            case QRequest.LOGIN:
                User user = getGson().fromJson(js.optString("user"), User.class);
                MainApplication.app.setUser(user);
                break;

            case QRequest.WALLET:
                if(!js.isNull("amount")){
                    amount = Float.parseFloat(js.optString("amount"));
                    credit.setText(Utils.getPrice(String.valueOf(amount)));
                }

                break;
            default:
                break;
        }
        dismissLoading();
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {

    }

    @Override
    public void onNetError(int id) {

    }

    /**
     * 刷新个人信息数据
     */
    public void refreshUserInfo() {
        user = MainApplication.app.getUser();
        Account account = MainApplication.app.getAccount();
        if (account != null) {
            //
            //QRequest.login(account.getEmail(), account.getPassword(), callback);
            layoutPerson.setVisibility(View.VISIBLE);
        } else {
            layoutPerson.setVisibility(View.GONE);
        }
    }
}
