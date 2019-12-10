package fr.it8.asiansoupe.interfaces;

/**
 * 登录状态改变的监听
 */
public interface OnLoginStatusChangeListener {
    //登录
    void onLogin();
    //退出登录
    void onLogout();
}
