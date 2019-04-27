package com.matrixhub.vaibhav.merabyke;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout input_layout_mobile, input_layout_OTP;
    EditText et_mobile, et_OTP;
    Button btn_submit, btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findAllIds();
        submit();
        reset();
    }

    private void findAllIds() {

        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        input_layout_OTP = findViewById(R.id.input_layout_OTP);

        et_mobile = findViewById(R.id.et_mobile);
        et_OTP = findViewById(R.id.et_OTP);

        btn_submit = findViewById(R.id.btn_submit);
        btn_reset = findViewById(R.id.btn_reset);
    }
    public void submit() {
        // vibrator.vibrate(38);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateMobile()){
                    et_mobile.setError(getString(R.string.err_msg_mobile));
                } else if (!validateOTP()) {
                    et_OTP.setError(getString(R.string.err_msg_OTP));
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }
    private boolean validateMobile() {
        if (et_mobile.getText().toString().trim().isEmpty()) {
            //input_layout_mobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(et_mobile);
            return false;
        } else {
            input_layout_mobile.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateOTP() {
        if (et_OTP.getText().toString().trim().isEmpty()) {
            //input_layout_OTP.setError(getString(R.string.err_msg_OTP));
            requestFocus(et_OTP);
            return false;
        } else {
            input_layout_OTP.setErrorEnabled(false);
        }
        return true;
    }
    public void reset() {
        btn_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //vibrator.vibrate(38);
                et_mobile.setText("");
                et_OTP.setText("");
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}


