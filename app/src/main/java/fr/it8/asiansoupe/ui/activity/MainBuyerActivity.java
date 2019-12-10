package fr.it8.asiansoupe.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import fr.it8.asiansoupe.R;
import fr.it8.asiansoupe.adapter.PagerAdapter;
import fr.it8.asiansoupe.adapter.PopupCategroyAdapter;
import fr.it8.asiansoupe.app.Config;
import fr.it8.asiansoupe.app.Constants;
import fr.it8.asiansoupe.app.MainApplication;
import fr.it8.asiansoupe.base.BaseActivity;
import fr.it8.asiansoupe.bean.Account;
import fr.it8.asiansoupe.bean.Category;
import fr.it8.asiansoupe.bean.User;
import fr.it8.asiansoupe.interfaces.OnItemClickListener;
import fr.it8.asiansoupe.interfaces.OnLoginStatusChangeListener;
import fr.it8.asiansoupe.request.QRequest;
import fr.it8.asiansoupe.ui.fragment.HomeFragment;
import fr.it8.asiansoupe.ui.fragment.PersonFragment;
import fr.it8.asiansoupe.util.NetUtils;
import fr.it8.asiansoupe.util.OnMessageReceiver;
import fr.it8.asiansoupe.view.MyPopupWindow;
import fr.it8.asiansoupe.view.MyViewPager;

/**
 * 买家首页
 */
public class MainBuyerActivity extends BaseActivity implements OnLoginStatusChangeListener, OnTabSelectListener {

    @BindView(R.id.view_pager)
    MyViewPager viewPager;
    @BindView(R.id.navigation_bar)
    JPTabBar navigationBar;

    private ArrayList<Fragment> fragments;
    private long mTimeStampFirst = 0;
    private Account account;
    private int mPosition = 0;
    private OnMessageReceiver receiver;
    private MyPopupWindow categoriesWindow;
    @BindView(R.id.view_line)
    View view_line;
    @Titles
    private int[] title = {R.string.tab_home, R.string.tab_person};
    @NorIcons
    private int[] iconSel = {R.mipmap.tab_home,  R.mipmap.tab_user};
    @SeleIcons
    private int[] iconUnSel = {R.mipmap.tab_home_sel, R.mipmap.tab_user_sel};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buyer);
    }

    @Override
    protected void initView() {
        fragments = getFragments();
        initNavigationBarAndViewPager();
        receiver = new OnMessageReceiver(this);
        registerReceiver(receiver, Config.ACTION_LOGIN, Config.ACTION_LOGOUT);
    }




    @Override
    protected void initData() {
        account = MainApplication.app.getAccount();
        if (account != null) {
            //买家信息
            //QRequest.userInfoMore(account.getEmail(), account.getPassword(), callback);
        }
    }

    private void initNavigationBarAndViewPager() {
        navigationBar.setTitles(title).setNormalIcons(iconUnSel).setSelectedIcons(iconSel).generate();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(4);
        navigationBar.setContainer(viewPager);
        navigationBar.setTabListener(this);
    }

    @Override
    public void onSuccess(String data, int id) throws JSONException {
        JSONObject js = new JSONObject(data);
        switch (id) {
            //个人信息
            case QRequest.USER_INFO:
                User user = getGson().fromJson(js.optString("user"), User.class);
                MainApplication.app.setUser(user);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String msg, String code, int id) throws JSONException {

    }

    @Override
    public void onNetError(int id) {

    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new PersonFragment());
        return fragments;
    }

    @Override
    public void onBackPressed() {
        long iCurTime = System.currentTimeMillis();
        if (mTimeStampFirst == 0 || iCurTime - mTimeStampFirst > 1500) {
            mTimeStampFirst = iCurTime;
            showWarning(viewPager, "Click again to exit Delice House");
        } else if (iCurTime - mTimeStampFirst <= 1500) {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //登录返回
            case Constants.LOGIN:
                if (resultCode == RESULT_OK) {
                    //登录成功
                    account = MainApplication.app.getAccount();
                } else {
                    //登录失败
                    viewPager.setCurrentItem(mPosition, false);
                    navigationBar.setSelectTab(mPosition);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogin() {
        ((PersonFragment) fragments.get(1)).refreshUserInfo();
    }

    @Override
    public void onLogout() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onTabSelect(int index) {
        if (index == 1) {
            this.mPosition = index;
        } else if (index == 3) {
           /* //个人fragment
            if (account == null) {
                startActivityForResult(LoginInsideActivity.class, Constants.LOGIN);
                return;
            }
            */
            this.mPosition = index;
        } else {
            this.mPosition = index;
        }
    }

    @Override
    public void onClickMiddle(View middleBtn) {

    }
}
