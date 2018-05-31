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
import cn.bmob.v3.listener.UpdateListener;


/**
 * ForgetActivity
 *
 * @author ggz
 * @date 2018/5/3
 */
public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ForgetActivity";

    private static final int CHECK_ACCOUNT = 0;
    private static final int CHECK_PHONE = 1;
    private static final int CHECK_CONFIRM_PASSWORD = 2;

    private MaterialEditText mForgetAccountEt;
    private MaterialEditText mForgetPhoneNumberEt;
    private MaterialEditText mForgetPasswordEt;
    private MaterialEditText mForgetConfirmPasswordEt;


    private boolean isInputError = true;

    private MyHandler mHandler;

    private Person mPerson;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        // 初始化 Bmob
        Bmob.initialize(this, LoginActivity.BMOB_APP_ID);

        mHandler = new MyHandler(this);

        initView();

    }

    private void initView() {
        ImageView forgetGoBackIv = findViewById(R.id.iv_forget_go_back);
        forgetGoBackIv.setOnClickListener(this);

        mForgetAccountEt = findViewById(R.id.et_forget_account);
        mForgetAccountEt.addTextChangedListener(new TextWatcher() {
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


        mForgetPhoneNumberEt = findViewById(R.id.et_forget_phone_number);
        mForgetPhoneNumberEt.addTextChangedListener(new TextWatcher() {
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


        mForgetPasswordEt = findViewById(R.id.et_forget_password);


        mForgetConfirmPasswordEt = findViewById(R.id.et_forget_confirm_password);
        mForgetConfirmPasswordEt.addTextChangedListener(new TextWatcher() {
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


        Button registerEnterBtn = findViewById(R.id.btn_forget_enter);
        registerEnterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_forget_go_back:
                finish();
                break;

            case R.id.btn_forget_enter:
                if (mPerson != null) {
                    if (!isInputError) {
                        String objectId = mPerson.getObjectId();
                        final String account = mForgetAccountEt.getText().toString();
                        String password = mForgetPasswordEt.getText().toString();

                        Person person = new Person();
                        person.setPassword(password);
                        person.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra(LoginActivity.KEY_ACCOUNT, account);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(ForgetActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "修改失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                }
                break;
            default:
        }
    }


    private static class MyHandler extends Handler {

        WeakReference<ForgetActivity> mWeakReference;

        public MyHandler(ForgetActivity activity) {
            super();
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final ForgetActivity activity = mWeakReference.get();

            if (activity != null) {
                switch (msg.what) {
                    case CHECK_ACCOUNT:
                        String account = activity.mForgetAccountEt.getText().toString();
                        BmobQuery<Person> query = new BmobQuery<>();
                        query.addWhereEqualTo("account", account);
                        query.findObjects(new FindListener<Person>() {
                            @Override
                            public void done(List<Person> list, BmobException e) {
                                if (e == null) {
                                    if (list.size() != 0) {
                                        activity.mPerson = list.get(0);
                                        activity.isInputError = false;
                                    } else {
                                        activity.mForgetAccountEt.setError("账号不存在");
                                        activity.mPerson = null;
                                        activity.isInputError = true;
                                    }
                                } else {
                                    Log.e(TAG, "Error : " + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                        break;

                    case CHECK_PHONE:
                        String phone = activity.mForgetPhoneNumberEt.getText().toString();
                        if (activity.mPerson != null) {
                            if (!phone.equals(activity.mPerson.getPhoneNumber())) {
                                activity.mForgetPhoneNumberEt.setError("手机号码验证错误");
                                activity.isInputError = true;
                            } else {
                                activity.isInputError = false;
                            }
                        } else {
                            activity.mForgetPhoneNumberEt.setError("请输入正确账号");
                        }
                        break;

                    case CHECK_CONFIRM_PASSWORD:
                        String password = activity.mForgetPasswordEt.getText().toString();
                        String confirmPassword = activity.mForgetConfirmPasswordEt.getText().toString();
                        if (!password.equals(confirmPassword)) {
                            activity.mForgetConfirmPasswordEt.setError("密码不一致");
                            activity.isInputError = true;
                        } else {
                            activity.isInputError = false;
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
