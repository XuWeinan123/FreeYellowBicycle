package com.aaronxu.freeyellowbicycle;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ClickForBicycle extends AppCompatActivity {
    private static final String TAG = "ClickForBicycle";
    Button button;
    private AlertDialog alertDialog;
    private EditText yellowNumber;
    private Button submit_number;
    private boolean isFind = false;
    private ProgressDialog progressDialog01;
    private ProgressDialog progressDialog02;

    private String unlock=null;
    private TextView unlockNumber;
    private AlertDialog alertDialog2;
    private EditText unlockNumberShare;
    private AlertDialog alertDialog3;
    private Button shareButton;
    private TextView titleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_for_bicycle);
        Bmob.initialize(this,"b8d46b4652df19d095c3b837505342e2");

        progressDialog01 = new ProgressDialog(ClickForBicycle.this);
        progressDialog01.setMessage("正在查询中");
        progressDialog02 = new ProgressDialog(ClickForBicycle.this);
        progressDialog02.setMessage("正在提交");

        initDialog();
        button = (Button) findViewById(R.id.buttonToNext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }
    private void initDialog(){
        //初始化第一个dialog
        AlertDialog.Builder aDialog = new AlertDialog.Builder(ClickForBicycle.this);
        View view = LayoutInflater.from(ClickForBicycle.this).inflate(R.layout.dialog_view,null);
        yellowNumber = (EditText) view.findViewById(R.id.yellowNumber);
// 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("请输入车牌号");
// 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);
// 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        yellowNumber.setHint(ss);
        submit_number = (Button) view.findViewById(R.id.submit_number);
        submit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+yellowNumber.getText().toString().length());
                if (yellowNumber.getText().toString().length()>7||yellowNumber.getText().toString().length()<5) {
                    yellowNumber.setText("");
                    Toast.makeText(ClickForBicycle.this,"车牌输入格式不对",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog01.show();
                BmobQuery<YellowBicycle> query = new BmobQuery<>();
                query.addWhereEqualTo("number",yellowNumber.getText().toString());
                Log.d(TAG, "查询的号码是"+yellowNumber.getText().toString());
                query.findObjects(ClickForBicycle.this, new FindListener<YellowBicycle>() {
                    @Override
                    public void onSuccess(List<YellowBicycle> list) {
                        Log.d(TAG,list.size()+"条数据。");
                        if(list.size()>=1){
                            isFind = true;
                            for (YellowBicycle yellowBicycle:list){
                                Log.d("TAG",yellowBicycle.getNumber());
                                Log.d("TAG",yellowBicycle.getUnlock());
                                unlock = yellowBicycle.getUnlock();
                                Log.d(TAG, "onSuccess: "+unlock);

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(yellowNumber.getWindowToken(), 0) ;

                                unlockNumber.setText(unlock);
                                alertDialog2.show();
                                alertDialog.dismiss();
                            }
                        }else{
                            isFind = false;
                            //cantFindDialog(yellowNumber.getText().toString());
                            Toast.makeText(getApplicationContext(),"无法找到数据",Toast.LENGTH_SHORT).show();
                            initDialog3(yellowNumber.getText().toString()).show();
                        }
                        Log.d(TAG, isFind+"");
                        //unlockView.setText(unlock);
                        progressDialog01.dismiss();
                    }
                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG, "onError: 出现错误"+s);
                        progressDialog01.dismiss();
                    }
                });
            }
        });
        aDialog.setView(view);
        alertDialog = aDialog.create();

        //初始化第二个对话框
        AlertDialog.Builder bDialog = new AlertDialog.Builder(ClickForBicycle.this);
        View view2 = LayoutInflater.from(ClickForBicycle.this).inflate(R.layout.dialog_view_2,null);
        unlockNumber = (TextView) view2.findViewById(R.id.unlockNumber);
        bDialog.setView(view2);
        alertDialog2 = bDialog.create();

        //初始化第三个对话框
        ;
    }

    private AlertDialog initDialog3(final String yellowNumberInDialog) {
        AlertDialog.Builder cDialog = new AlertDialog.Builder(ClickForBicycle.this);
        View view3 = LayoutInflater.from(ClickForBicycle.this).inflate(R.layout.dialog_view_3,null);
        unlockNumberShare = (EditText) view3.findViewById(R.id.unlockNumberShare);
        titleNumber = (TextView) view3.findViewById(R.id.title_number);
        titleNumber.setText("车牌号："+yellowNumberInDialog+" 暂时没有密码");
        shareButton = (Button) view3.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unlockNumberShare.getText().toString().length()!=4){
                    unlockNumberShare.setText("");
                    Toast.makeText(ClickForBicycle.this,"密码格式不对",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog02.show();
                String unlockNumberInDialog = unlockNumberShare.getText().toString();
                Log.d("TAG", unlockNumberInDialog);
                YellowBicycle yellowBicycle = new YellowBicycle(yellowNumberInDialog,unlockNumberInDialog);
                yellowBicycle.save(ClickForBicycle.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ClickForBicycle.this,"提交成功",Toast.LENGTH_SHORT).show();
                        yellowNumber.setText("");
                        progressDialog02.dismiss();
                        alertDialog3.dismiss();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ClickForBicycle.this,"提交失败",Toast.LENGTH_SHORT).show();
                        progressDialog02.dismiss();
                        alertDialog3.dismiss();
                    }
                });
            }
        });
        cDialog.setView(view3);
        alertDialog3 = cDialog.create();
        return alertDialog3;
    }
}
