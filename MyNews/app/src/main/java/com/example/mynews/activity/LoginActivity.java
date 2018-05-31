package com.example.mynews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynews.R;
import com.example.mynews.bean.Person;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.mynews.activity.MainActivity.KEY_PERSON;

/**
 * LoginActivity
 *
 * @author ggz
 * @date 2018/5/3
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    public static final String BMOB_APP_ID = "93140762c2381a342a5e7c6aa7e25872";

    public static final int REQUEST_CODE_REGISTER = 0;
    public static final int REQUEST_CODE_FORGET = 1;
    public static final String KEY_ACCOUNT = "account";

    private MaterialEditText mAccountEt;
    private MaterialEditText mPasswordEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        // 初始化 Bmob
        Bmob.initialize(this, BMOB_APP_ID);

        initView();

        // 获取账号值
        String accountStr = getIntent().getStringExtra(KEY_ACCOUNT);
        if (accountStr != null) {
            mAccountEt.setText(accountStr);
        }
    }

    private void initView() {
        ImageView loginGoBackIv = findViewById(R.id.iv_login_goback);
        loginGoBackIv.setOnClickListener(this);

        mAccountEt = findViewById(R.id.login_account);
        mPasswordEt = findViewById(R.id.login_password);

        TextView loginRegisterTv = findViewById(R.id.tv_login_register);
        loginRegisterTv.setOnClickListener(this);

        Button loginForgetBtn = findViewById(R.id.btn_login_forget);
        loginForgetBtn.setOnClickListener(this);

        Button loginEnterBtn = findViewById(R.id.btn_login_enter);
        loginEnterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_goback:
                finish();
                break;

            case R.id.tv_login_register:
                Intent intent0 = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent0, REQUEST_CODE_REGISTER);
                break;

            case R.id.btn_login_forget:
                Intent intent1 = new Intent(this, ForgetActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_FORGET);
                break;

            case R.id.btn_login_enter:
                String account = mAccountEt.getText().toString();
                String password = mPasswordEt.getText().toString();
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                    BmobQuery<Person> eq1 = new BmobQuery<>();
                    eq1.addWhereEqualTo("account", account);
                    BmobQuery<Person> eq2 = new BmobQuery<>();
                    eq2.addWhereEqualTo("password", password);
                    List<BmobQuery<Person>> andQueries = new ArrayList<>();
                    andQueries.add(eq1);
                    andQueries.add(eq2);

                    BmobQuery<Person> query = new BmobQuery<>();
                    query.and(andQueries);
                    query.findObjects(new FindListener<Person>() {
                        @Override
                        public void done(List<Person> list, BmobException e) {
                            if (e == null) {
                                if (list.size() != 0) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                    Person person = list.get(0);
                                    String personJson = new Gson().toJson(person);
                                    Intent intent = new Intent();
                                    intent.putExtra(KEY_PERSON, personJson);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e(TAG, "Error : " + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_REGISTER:
                if (resultCode == RESULT_OK) {
                    String str = data.getStringExtra(KEY_ACCOUNT);
                    mAccountEt.setText(str);
                }
                break;

            case REQUEST_CODE_FORGET:
                if (resultCode == RESULT_OK) {
                    String str = data.getStringExtra(KEY_ACCOUNT);
                    mAccountEt.setText(str);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
