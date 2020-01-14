package fr.ws.reader.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import fr.ws.reader.bean.Account;
import fr.ws.reader.bean.Country;
import fr.ws.reader.bean.Feed;
import fr.ws.reader.bean.User;
import fr.ws.reader.bean.Category;
import fr.ws.reader.bean.Trier;
import fr.ws.reader.util.D;
import fr.ws.reader.util.T;
import fr.ws.reader.util.Utils;
import okhttp3.OkHttpClient;

/**
 * MainApplication 配置初始化
 */
public class MainApplication extends Application {

    public static MainApplication app;
    public Context mContext;
    public Account account;
    public User user;
    public List<Feed> subscribedfeeds;
    public List<Trier> triers;
    public List<Category> categories;
    public Country country;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
        initConfig();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        //根据系统语言选择语言包
        config.locale = Locale.getDefault();
        resources.updateConfiguration(config, dm);
        //初始化OkHttp网络请求
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor("OkHttpUtils"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        //初始化Toast
        T.initWithApplication(this);
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 判断用户的登录状态
     *
     * @return 是否登录
     */
    public boolean isLogin() {
        return Utils.getUToken(this) != null && !Utils.getUToken(this).equals("");
    }

    /**
     * 设置个人信息(存储)
     *
     * @param account 个人数据体
     */
    public void setAccount(Account account) {
        this.account = account;
        D.getInstance(app).putString(Constants.ACCOUNT_INFO, new Gson().toJson(account));
    }

    /**
     * 获取个人信息（如果为空新建，不为空取）
     *
     * @return 个人数据体
     */
    public Account getAccount() {
        if (account == null) {
            String strUserInfo = D.getInstance(app).getString(Constants.ACCOUNT_INFO, "");
            //本地取不到个人信息时，重新创建一个
            if (strUserInfo.equals("")) {
                return null;
            } else {
                //取到时，将数据存入实体中方便获取
                setAccount(new Gson().fromJson(strUserInfo, Account.class));
            }
        }
        return account;
    }

    /**
     * 设置买家信息
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        D.getInstance(app).putString(Constants.USER_INFO, new Gson().toJson(user));
    }

    /**
     * 获取买家信息
     *
     * @return
     */
    public User getUser() {
        if (user == null) {
            String str = D.getInstance(app).getString(Constants.USER_INFO, "");
            if (str.isEmpty()) {
                return null;
            } else {
                //取到时，将数据存入实体中方便获取
                setUser(new Gson().fromJson(str, User.class));
            }
        }
        return user;
    }


    /**
     * get subscribed feeds
     *
     * @return
     */
    public List<Feed> getSubscribedFeeds() {
        /*String str = D.getInstance(app).getString(Constants.CATEGORIES_INFO, "");
        if (str.isEmpty()) {
            return null;
        } else {
            subscribedfeeds = new Gson().fromJson(str, new TypeToken<List<Feed>>() {
            }.getType());
        }*/
        subscribedfeeds = new ArrayList<Feed>();
        subscribedfeeds.add(new Feed(1,"Aweber blog","http://www.aweber.com/blog/feed/","","",""));
        subscribedfeeds.add(new Feed(2,"Android authority","https://www.androidauthority.com/feed","","",""));
        return subscribedfeeds;
    }

    /**
     * 获取排序列表
     *
     * @return
     */
    public List<Trier> getTriers() {
        if (triers == null) {
            String str = D.getInstance(app).getString(Constants.TRIER_INFO, "");
            if (str.isEmpty()) {
                return null;
            } else {
                triers = new Gson().fromJson(str, new TypeToken<List<Trier>>() {
                }.getType());
                setTriers(triers);
            }
        }
        return triers;
    }

    /**
     * 获取排序列表
     *
     * @return
     */
    public List<Category> getCategories() {
        /*if (categories == null) {
            String str = D.getInstance(app).getString(Constants.CATEGORIES_INFO, "");
            if (str.isEmpty()) {
                return null;
            } else {
                categories = new Gson().fromJson(str, new TypeToken<List<Category>>() {
                }.getType());
                setCategories (categories);
            }
        }*/
        categories=new ArrayList<Category>();
        categories.add (new Category ("123","fiance"));
        categories.add (new Category ("-1","all"));
        return categories;
    }

    /**
     * 存储排序列表
     *
     * @param categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        D.getInstance(app).putString(Constants.CATEGORIES_INFO, new Gson().toJson(categories));
    }

    /**
     * 存储排序列表
     *
     * @param triers
     */
    public void setTriers(List<Trier> triers) {
        this.triers = triers;
        D.getInstance(app).putString(Constants.TRIER_INFO, new Gson().toJson(triers));
    }

    /**
     * 获取国家列表
     *
     * @return
     */
    public Country getCountries() {
        if (country == null) {
            String str = D.getInstance(app).getString(Constants.COUNTRY_INFO, "");
            if (str.isEmpty()) {
                return null;
            } else {
                country = new Gson().fromJson(str, new TypeToken<List<Country>>() {
                }.getType());
                setCountries(country);
            }
        }
        return country;
    }

    /**
     * 存储国家列表
     *
     * @param country
     */
    public void setCountries(Country country) {
        D.getInstance(app).putString(Constants.COUNTRY_INFO, new Gson().toJson(country));
        this.country = country;
    }
}
