package fr.ws.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forgot_password)
    IconTextView tvForgotPassword;


    private OnMessageReceiver receiver;
    private Account account;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        loadBanner();      //轮播图
        account = MainApplication.app.getAccount();
        if (account != null) {
            et_email.setText(account.getEmail());
            etPassword.setText(account.getPassword());
        }
    }

    /**
     * 初始化Banner
     */
    private void loadBanner() {
        final List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.background);
        images.add(R.mipmap.backgroud1);
        images.add(R.mipmap.background2);
        banner.setAdapter(this);
        banner.setData(images, null);
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {
    }

    @Override
    public void onNetError(int id) {
        SnackbarUtil.shortSnackbar(et_email, "Net Error.", SnackbarUtil.Alert).show();
        showNetError(et_email);
    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {
        String email = et_email.getText().toString();
        String pass = etPassword.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) {
            return;
        }
        D.getInstance(this).putString("password",pass);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("authentification", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Account acc = new Account();
                            acc.setIsActive(1);
                            acc.setEmail(user.getEmail());
                            MainApplication.app.setAccount(acc);
                            account = MainApplication.app.getAccount();
                            if (account.getIsActive() == 1) {
                                startActivity(MainActivity.class);
                            }
                            sendBroadcast(Config.ACTION_LOGIN);
                            startActivity(MainActivity.class);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("authentification", "signInWithEmail:failure", task.getException());
                            showError(etPassword,"Authentication failed");
                            //updateUI(null);
                        }
                    }
                });
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
