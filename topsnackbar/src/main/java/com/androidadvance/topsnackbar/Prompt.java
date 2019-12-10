package com.androidadvance.topsnackbar;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.icon_error, R.color.text_color_white),

    /**
     * 红色,警告
     */
    WARNING(R.drawable.icon_warning, R.color.text_color_white),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.icon_successful, R.color.text_color_white);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
