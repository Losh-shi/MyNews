package com.example.mynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mynews.R;
import com.example.mynews.bean.Person;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * RegisterActivity
 *
 * @author ggz
 * @date 2018/5/3
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private static final int CHECK_ACCOUNT = 0;
    private static final int CHECK_CONFIRM_PASSWORD = 1;
    private static final int CHECK_PHONE = 2;

    private MaterialEditText mRegisterAccountEt;
    private MaterialEditText mRegisterPasswordEt;
    private MaterialEditText mRegisterConfirmPasswordEt;
    private MaterialEditText mRegisterPhoneNumberEt;
    private MaterialEditText mRegisterNameEt;

    private boolean isInputError = true;

    private MyHandler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        // 初始化 Bmob
        Bmob.initialize(this, LoginActivity.BMOB_APP_ID);

        mHandler = new MyHandler(this);

        initView();

    }

    private void initView() {
        ImageView registerGoBackIv = findViewById(R.id.iv_register_go_back);
        registerGoBackIv.setOnClickListener(this);

        mRegisterAccountEt = findViewById(R.id.et_register_account);
        mRegisterAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeMessages(CHECK_ACCOUNT);
                mHandler.sendEmptyMessageDelayed(CHECK_ACCOUNT, 1000);
            }
        });


        mRegisterPasswordEt = findViewById(R.id.et_register_password);


        mRegisterConfirmPasswordEt = findViewById(R.id.et_register_confirm_password);
        mRegisterConfirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeMessages(CHECK_CONFIRM_PASSWORD);
                mHandler.sendEmptyMessageDelayed(CHECK_CONFIRM_PASSWORD, 1000);
            }
        });


        mRegisterPhoneNumberEt = findViewById(R.id.et_register_phone_number);
        mRegisterPhoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeMessages(CHECK_PHONE);
                mHandler.sendEmptyMessageDelayed(CHECK_PHONE, 1000);
            }
        });


        mRegisterNameEt = findViewById(R.id.et_register_name);

        Button registerEnterBtn = findViewById(R.id.btn_register_enter);
        registerEnterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_go_back:
                finish();
                break;

            case R.id.btn_register_enter:
                if (!isInputError) {
                    final String account = mRegisterAccountEt.getText().toString();
                    String password = mRegisterPasswordEt.getText().toString();
                    String phoneNumber = mRegisterPhoneNumberEt.getText().toString();
                    String name = mRegisterNameEt.getText().toString();
                    Person person = new Person(account, password, phoneNumber, name);
                    person.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra(LoginActivity.KEY_ACCOUNT, account);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            default:
        }
    }


    private static class MyHandler extends Handler {

        WeakReference<RegisterActivity> mWeakReference;

        public MyHandler(RegisterActivity activity) {
            super();
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final RegisterActivity registerActivity = mWeakReference.get();

            if (registerActivity != null) {
                switch (msg.what) {
                    case CHECK_ACCOUNT:
                        String account = registerActivity.mRegisterAccountEt.getText().toString();
                        // 检查账号是否已注册
                        BmobQuery<Person> query = new BmobQuery<>();
                        query.addWhereEqualTo("account", account);
                        query.findObjects(new FindListener<Person>() {
                            @Override
                            public void done(List<Person> list, BmobException e) {
                                if (e == null) {
                                    if (list.size() != 0) {
                                        registerActivity.mRegisterAccountEt.setError("账号已被注册");
                                        registerActivity.isInputError = true;
                                    } else {
                                        registerActivity.isInputError = false;
                                    }
                                } else {
                                    Log.e(TAG, "Error : " + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                        break;

                    case CHECK_CONFIRM_PASSWORD:
                        String password = registerActivity.mRegisterPasswordEt.getText().toString();
                        String confirmPassword = registerActivity.mRegisterConfirmPasswordEt.getText().toString();
                        if (!password.equals(confirmPassword)) {
                            registerActivity.mRegisterConfirmPasswordEt.setError("密码不一致");
                            registerActivity.isInputError = true;
                        } else {
                            registerActivity.isInputError = false;
                        }
                        break;

                    case CHECK_PHONE:
                        String phone = registerActivity.mRegisterPhoneNumberEt.getText().toString();
                        if (phone.length() != 11) {
                            registerActivity.mRegisterPhoneNumberEt.setError("输入错误");
                            registerActivity.isInputError = true;
                        } else {
                            registerActivity.isInputError = false;
                        }
                        break;

                    default:
                        break;
                }


            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


}
