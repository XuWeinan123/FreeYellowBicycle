package com.aaronxu.freeyellowbicycle;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final int REQUEST_READ_CONTACTS = 0;

    private LinearLayout sign_up_layout;
    private AutoCompleteTextView sign_up_email;
    private EditText sign_up_password;
    private Button email_sign_up_button;
    private EditText sign_up_password_confirm;
    private ScrollView sign_up_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up_layout = (LinearLayout) findViewById(R.id.sign_up_layout);
        sign_up_form = (ScrollView) findViewById(R.id.sign_up_form);
        sign_up_email = (AutoCompleteTextView) findViewById(R.id.sign_up_email);
        sign_up_password = (EditText) findViewById(R.id.sign_up_password);
        email_sign_up_button = (Button) findViewById(R.id.email_sign_up_button);
        sign_up_password_confirm = (EditText) findViewById(R.id.sign_up_password_confirm);

        email_sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
    }

    private void attemptSignUp() {

        sign_up_email.setError(null);
        sign_up_password.setError(null);
        sign_up_password_confirm.setError(null);

        String email = sign_up_email.getText().toString();
        String password = sign_up_password.getText().toString();
        String password_confirm = sign_up_password_confirm.getText().toString();
        Log.d(TAG, password +"\n"+ password_confirm );

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            sign_up_email.setError(getString(R.string.error_field_required));
            focusView = sign_up_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            sign_up_email.setError(getString(R.string.error_invalid_email));
            focusView = sign_up_email;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            sign_up_password.setError(getString(R.string.error_invalid_password));
            focusView = sign_up_password;
            cancel = true;
        }
        if(!password_confirm.equals(password)){
            sign_up_password_confirm.setError(getString(R.string.error_unequal_password));
            focusView = sign_up_password_confirm;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            sign_up_layout.setVisibility(View.VISIBLE);
            sign_up_form.setVisibility(View.INVISIBLE);
            BmobUser signUpUser = new BmobUser();
            signUpUser.setUsername(email);
            signUpUser.setEmail(email);
            signUpUser.setPassword(password);
            signUpUser.signUp(getApplicationContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
                    sign_up_layout.setVisibility(View.GONE);
                    sign_up_form.setVisibility(View.VISIBLE);
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(getApplicationContext(),"注册失败\n"+s,Toast.LENGTH_LONG).show();
                    sign_up_layout.setVisibility(View.GONE);
                    sign_up_form.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "SignUpActivity is Destroied");
        super.onDestroy();
    }
}
