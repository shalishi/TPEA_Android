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
import fr.ws.reader.base.BaseActivity;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.util.EditTextWatcher;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.et_telephone)
    EditText etTelephone;
    @BindView(R.id.tv_return)
    TextView tv_return;
    private String phone = "";
    private String password = "";
    private String repassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        new EditTextWatcher(etTelephone);
        new EditTextWatcher(etPassword);
        new EditTextWatcher(etRepassword);
        Typeface font = Typeface.createFromAsset(getAssets(), "fontello.ttf");
        tv_return.setTypeface(font);
        tv_return.setText("\ue806");
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onSuccess(String data, int id) throws JSONException {
        switch (id) {
            //普通用户注册提交
            case QRequest.REGISTER:
                JSONObject js = new JSONObject(data);
                if(!js.isNull("user")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);
                    bundle.putString("password", password);
                    //startActivity(LoginFirstActivity.class, bundle);
                    finish();
                }else if(!js.isNull("error")){
                    showError(etPassword,js.optString("error"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {
        Integer error_code = Integer.parseInt(msg);
        String message="";
        switch (id) {
            //event list
            case QRequest.REGISTER:
                switch (error_code) {
                    case 1:
                        message = getString(R.string.register_user_error1);
                        break;
                    case 2:
                        message = getString(R.string.register_user_error2);
                        break;
                    case 3:
                        message = getString(R.string.register_user_error3);
                        break;
                    case 4:
                        message = getString(R.string.error4);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        showError(etPassword, message);
    }

    @Override
    public void onNetError(int id) {
        showNetError(etPassword);
    }


    /**
     * 注册提交
     *
     * @param view
     */
    public void onRegister(View view) {
        if (checkContent()) {
            //普通用户注册提交接口
            QRequest.Register(phone,password, repassword, callback);
            showLoading().show();
        }
    }

    /**
     * 检查文案
     *
     * @return
     */
    public boolean checkContent() {
        phone = etTelephone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        repassword = etRepassword.getText().toString().trim();
        //判断是否为空
        if (phone.isEmpty())
            return false;
        if (password.isEmpty())
            return false;
        if (repassword.isEmpty())
            return false;
        //密码位数
        if (password.length() < 6)
            return false;
        if (repassword.length() < 6)
            return false;
        return true;
    }
}
