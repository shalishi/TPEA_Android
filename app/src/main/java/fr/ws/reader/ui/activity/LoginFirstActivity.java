package fr.ws.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import fr.ws.reader.R;
import fr.ws.reader.app.Config;
import fr.ws.reader.app.MainApplication;
import fr.ws.reader.base.BaseActivity;
import fr.ws.reader.bean.Account;
import fr.ws.reader.interfaces.OnLoginStatusChangeListener;
import fr.ws.reader.request.QRequest;
import fr.ws.reader.util.D;
import fr.ws.reader.util.OnMessageReceiver;
import fr.ws.reader.util.SnackbarUtil;
import fr.ws.reader.util.StringUtils;

/**
 * 第一个登录页
 */
public class LoginFirstActivity extends BaseActivity implements OnLoginStatusChangeListener, BGABanner.Adapter<ImageView, Integer> {


    @BindView(R.id.banner)
    BGABanner banner;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forgot_password)
    IconTextView tvForgotPassword;


    private OnMessageReceiver receiver;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_first);
    }

    @Override
    protected void initView() {
        receiver = new OnMessageReceiver(this);
        registerReceiver(receiver, Config.ACTION_LOGIN, Config.ACTION_LOGOUT);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPassword(mContext).show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        /*String phone = getIntent().getExtras().getString("phone",null);
        String password = getIntent().getExtras().getString("password",null);
        if(phone!=null && password!=null){
            et_phone.setText(phone);
            etPassword.setText(password);
        }*/
    }
    @Override
    protected void initData() {
        loadBanner();      //轮播图
        account = MainApplication.app.getAccount();
        if (account != null) {
            et_phone.setText(account.getTelephone());
            etPassword.setText(account.getPassword());
        }
    }

    /**
     * 初始化Banner
     */
    private void loadBanner() {
        final List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.slide1);
        images.add(R.mipmap.slide2);
        images.add(R.mipmap.slide3);
        banner.setAdapter(this);
        banner.setData(images, null);
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        switch (id) {
            //登录接口
            case QRequest.LOGIN:
                //L.d(data);
                JSONObject js = new JSONObject(data);
                if(!js.isNull("user")) {
                    Account account = getGson().fromJson(js.optString("user"), Account.class);
                    account.setPassword(D.getInstance(this).getString("password", ""));
                    MainApplication.app.setAccount(account);
                    if (account.getIsActive() == 1) {
                        startActivity(MainActivity.class);
                    }
                    sendBroadcast(Config.ACTION_LOGIN);
                    finish();
                }else{
                    showError(et_phone,js.optString("response"));
                }
                break;
            //忘记密码
            case QRequest.FORGOT_PASSWORD:
                JSONObject js3 = new JSONObject(data);
                showSuccess(et_phone, js3.optString("ok"));
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
            case QRequest.FORGOT_PASSWORD:
            switch (error_code) {
                case 1:
                    message = getString(R.string.resetpwd_error1);
                    break;
                case 4:
                    message = getString(R.string.error4);
                    break;
                default:
                    break;
            }break;
            default:
                break;
        }
        showError(et_phone, message);
    }

    @Override
    public void onNetError(int id) {
        SnackbarUtil.shortSnackbar(et_phone, "Net Error.", SnackbarUtil.Alert).show();
        showNetError(et_phone);
    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {
        startActivity(MainActivity.class);
        finish();
        String phone = et_phone.getText().toString();
        String pass = etPassword.getText().toString();
        if (phone.isEmpty() || pass.isEmpty()) {
            return;
        }
        D.getInstance(this).putString("password",pass);
        QRequest.login(phone, pass, callback);
        showLoading().show();
    }

    /**
     * 忘记密码提示框
     *
     * @param context
     * @return
     */
    public AlertDialog showForgotPassword(Context context) {
        final AlertDialog builder = new AlertDialog.Builder(context, R.style.AlertDialog).create();
        final EditText editText = new EditText(context);
        editText.setHint(getString(R.string.hint_fp_email));
        editText.setHintTextColor(editText.getContext().getResources().getColor(R.color.hint_text));
        editText.setTextSize(15);
        builder.setTitle(getString(R.string.dialog_fp_title));
        builder.setMessage(getString(R.string.dialog_fp_msg));
        builder.setView(editText);
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.send), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String email = editText.getText().toString().trim();
                if (StringUtils.isEmail(email) || StringUtils.isNumber(email)) {
                    //忘记密码接口
                    QRequest.forgotPassword(email, callback);
                    showLoading().show();
                }
            }
        });
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);
        return builder;
    }


    @Override
    public void onLogin() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void onLogout() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, Integer model, int position) {
        itemView.setImageResource(model);
    }

    public void onRegister(View v){
        startActivity(RegisterActivity.class);
    }
}
