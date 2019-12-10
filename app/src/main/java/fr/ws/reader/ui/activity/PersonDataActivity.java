package fr.ws.reader.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import fr.ws.reader.R;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseActivity;
import fr.ws.reader.bean.Account;
import fr.ws.reader.bean.User;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.util.EditTextWatcher;
import fr.ws.reader.util.StringUtils;
import fr.ws.reader.view.ObservableScrollView;

/**
 * 个人资料页
 */
public class PersonDataActivity extends BaseActivity {

    private static final String TAG = "PersonDataActivity";

    @BindView(R.id.et_lastname)
    EditText etLastname;
    @BindView(R.id.et_firstname)
    EditText etFirstname;
    @BindView(R.id.et_telephone)
    EditText etTelephone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_zip)
    EditText etZip;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.scroll_view)
    ObservableScrollView scrollView;
    @BindView(R.id.tv_return)
    TextView tv_return;
    private User user;

    private String lastName = "";
    private String firstName = "";
    private String phone = "";
    private String address = "";
    private String zip = "";
    private String city = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
    }

    @Override
    protected void initView() {
        new EditTextWatcher(etLastname);
        new EditTextWatcher(etFirstname);
        new EditTextWatcher(etTelephone);
        new EditTextWatcher(etAddress);
        new EditTextWatcher(etZip);
        new EditTextWatcher(etCity);
        new EditTextWatcher(etEmail, EditTextWatcher.WATCHER_EMAIL);
        Typeface font = Typeface.createFromAsset(getAssets(), "fontello.ttf");
        tv_return.setTypeface(font);
        tv_return.setText("\ue819");
    }

    @Override
    protected void initData() {
        Account account = MainApplication.app.getAccount();
        if (account != null) {
            //获取个人信息
            QRequest.login(account.getTelephone(), account.getPassword(), callback);
            showLoading().show();
        }
    }


    /**
     * 绑定用户数据
     *
     * @param user 用户数据
     */
    private void bindData(User user) {
        etLastname.setText(user.getLastname());    //名
        etFirstname.setText(user.getFirstname());   //姓
        etTelephone.setText(user.getTelephone());     //电话
        etAddress.setText(user.getStreet());     //地址，街道
        etZip.setText(user.getZip());       //邮编
        etCity.setText(user.getCity());       //城市
        etEmail.setText(user.getEmail());     //邮箱
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        JSONObject js = new JSONObject(data);
        switch (id) {
            //个人信息
            case QRequest.LOGIN:
                user = getGson().fromJson(js.optString("user"), User.class);
                MainApplication.app.setUser(user);
                bindData(user);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {
        showError(etAddress, msg);
    }

    @Override
    public void onNetError(int id) {
        showNetError(etAddress);
    }


    /**
     * 提交修改信息
     *
     * @param view
     */
    public void onSubmitData(View view) {
        if (checkContent()) {
            //修改个人信息
           /* QRequest.updateUserInfo(user.getEntity_id(), lastName, firstName, phone, company, address, zip, city, countryId, invoice, email, password,
                    repassword, callback);
            showLoading().show();*/
        }
    }


    /**
     * 检查文案
     *
     * @return
     */
    public boolean checkContent() {
        lastName = etLastname.getText().toString().trim();
        firstName = etFirstname.getText().toString().trim();
        phone = etTelephone.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        city = etCity.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        //判断是否为空
        /*if (lastName.isEmpty())
            return false;
        if (firstName.isEmpty())
            return false;
            */
        if (phone.isEmpty())
            return false;
        /*
        if (address.isEmpty())
            return false;
        if (zip.isEmpty())
            return false;
        if (city.isEmpty())
            return false;
            */
        if (email.isEmpty())
            return false;
        //邮箱格式
        if (!StringUtils.isEmail(email))
            return false;
        return true;
    }
}
