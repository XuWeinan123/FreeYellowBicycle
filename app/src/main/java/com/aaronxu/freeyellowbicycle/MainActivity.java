package com.aaronxu.freeyellowbicycle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements KeyEvent.Callback{

    private static final String TAG = "MainActivity";
    private EditText yellowNumber;
    private Button submit_number;
    private String unlock=null;
    private boolean isFind = false;
    private ProgressDialog progressDialog01;
    private ProgressDialog progressDialog02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"b8d46b4652df19d095c3b837505342e2");
        yellowNumber = (EditText) findViewById(R.id.yellowNumber);
        submit_number = (Button) findViewById(R.id.submit_number);
        progressDialog01 = new ProgressDialog(MainActivity.this);
        progressDialog01.setMessage("正在查询中");
        progressDialog02 = new ProgressDialog(MainActivity.this);
        progressDialog02.setMessage("正在提交");
//        Intent deliverUsername = getIntent();
//        Toast.makeText(getApplicationContext(),deliverUsername.getStringExtra("username")+"\n欢迎您",Toast.LENGTH_SHORT).show();
        submit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog01.show();
                BmobQuery<YellowBicycle> query = new BmobQuery<>();
                query.addWhereEqualTo("number",yellowNumber.getText().toString());
                Log.d(TAG, "查询的号码是"+yellowNumber.getText().toString());
                query.findObjects(MainActivity.this, new FindListener<YellowBicycle>() {
                    @Override
                    public void onSuccess(List<YellowBicycle> list) {
                        Log.d(TAG,list.size()+"条数据。");
                        if(list.size()>=1){
                            isFind = true;
                            for (YellowBicycle yellowBicycle:list){
                                Log.d("TAG",yellowBicycle.getNumber());
                                Log.d("TAG",yellowBicycle.getUnlock());
                                unlock = yellowBicycle.getUnlock();
                                unlockDialog(unlock);
                            }
                        }else{
                            isFind = false;
                            cantFindDialog(yellowNumber.getText().toString());
                            Toast.makeText(getApplicationContext(),"无法找到数据",Toast.LENGTH_SHORT).show();
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
    }

    protected void cantFindDialog(final String yellowNumber) {
        AlertDialog.Builder canFindDialog = new AlertDialog.Builder(MainActivity.this);
        canFindDialog.setIcon(android.R.drawable.stat_sys_warning);
        canFindDialog.setMessage("啊哦，这个车牌("+yellowNumber+")还没有密码\n\n你是否愿意在使用完在使租借完这辆车之后分享它的密码呢？");
        canFindDialog.setPositiveButton("愿意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputNumberDialog(yellowNumber);
            }
        });
        canFindDialog.setNegativeButton("取消",null);
        canFindDialog.create().show();
    }
//宽口径
    protected void inputNumberDialog(final String yellowNumberInDialog) {
        final EditText inputNumberInDialog = new EditText(MainActivity.this);
        inputNumberInDialog.setInputType(InputType.TYPE_CLASS_NUMBER);
        //View myView = LayoutInflater.from(getApplication()).inflate(R.layout.input_number_in_dialog,null);
        //final EditText inputNumberInDialog = (EditText) findViewById(R.id.inputNumberInDialog);
        //inputNumberInDialog.setTextColor(Color.BLACK);
        AlertDialog.Builder inputNumberDialog = new AlertDialog.Builder(MainActivity.this);
        //inputNumberDialog.setIcon(R.mipmap.ic_add_black_48dp);
        inputNumberDialog.setTitle("车牌号："+yellowNumberInDialog);
        inputNumberDialog.setView(inputNumberInDialog);
//        inputNumberDialog.setView(myView);
        inputNumberDialog.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog02.show();
                String unlockNumberInDialog = inputNumberInDialog.getText().toString();
                Log.d("TAG", unlockNumberInDialog);
                YellowBicycle yellowBicycle = new YellowBicycle(yellowNumberInDialog,unlockNumberInDialog);
                yellowBicycle.save(MainActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        yellowNumber.setText("");
                        progressDialog02.dismiss();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(MainActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
                        progressDialog02.dismiss();
                    }
                });
            }
        });
        inputNumberDialog.setNegativeButton("取消",null);
        inputNumberDialog.create().show();
    }

    protected void unlockDialog(String unlockMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(unlockMessage);
        builder.setTitle("密码");
        builder.setPositiveButton("确认",null);
//加个标记，你要开始写报错的逻辑了
        builder.setNegativeButton("报错", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("TAG", "接下来开始报错的逻辑");

            }
        });
        builder.create().show();
    }

    private boolean attemptToQuery(String number) {
        BmobQuery<YellowBicycle> query = new BmobQuery<>();
        query.addWhereEqualTo("number",number);
        Log.d("TAG",number+"7777");
        isFind = false;
        Log.d("TAG",number+"7777");
        query.findObjects(getApplicationContext(), new FindListener<YellowBicycle>() {
            @Override
            public void onSuccess(List<YellowBicycle> list) {
                Log.d("TAG",list.size()+"条数据。");
                for (YellowBicycle yellowBicycle:list){
                    Log.d("TAG",yellowBicycle.getNumber());
                    Log.d("TAG",yellowBicycle.getUnlock());
                    unlock = yellowBicycle.getUnlock();
                }
                if (list.size()!=0){
                    isFind = true;
                    Log.d("TAG",isFind+"");
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(getApplicationContext(),"查询失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("TAG",isFind+"34354");
        return isFind;
    }

    //下面是退出时候的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
           dialog();
        }
        return false;
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
               }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               }
        });
        builder.create().show();
    }

}
