package com.aaronxu.freeyellowbicycle;

import cn.bmob.v3.BmobObject;

/**
 * Created by AaronXu on 2016-05-25.
 */
public class ControlBack extends BmobObject {
    private boolean isLogin;
    private boolean isSignUp;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isSignUp() {
        return isSignUp;
    }

    public void setSignUp(boolean signUp) {
        isSignUp = signUp;
    }
}
