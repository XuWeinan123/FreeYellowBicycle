package com.aaronxu.freeyellowbicycle;

import cn.bmob.v3.BmobObject;

/**
 * Created by AaronXu on 2016-05-25.
 */
public class YellowBicycle extends BmobObject {
    private String number;
    private String unlock;

    public YellowBicycle(){
    }
    public YellowBicycle(String number,String unlock){
        this.number = number;
        this.unlock = unlock;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnlock() {
        return unlock;
    }

    public void setUnlock(String unlock) {
        this.unlock = unlock;
    }
}
