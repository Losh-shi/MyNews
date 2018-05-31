package com.example.mynews.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mynews.R;
import com.example.mynews.bean.Person;
import com.example.mynews.fragment.WeatherFragment;
import com.example.mynews.fragment.IndexFragment;
import com.example.mynews.fragment.PersonFragment;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * MainActivity
 *
 * @author ggz
 * @date 2018/1/14
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    public static final int REQUEST_CODE_LOGIN = 0;
    public static final int REQUEST_CODE_FAVOURITE = 1;
    public static final int REQUEST_CODE_RESET_PASSWORD = 2;
    public static final int REQUEST_CODE_SEARCH = 3;

    public static final String KEY_FIRST_START = "firstStart";
    public static final String KEY_IS_LOGIN = "isLogin";
    public static final String KEY_PERSON = "person";
    public static final String KEY_WEATHER = "weather";
    public static final String KEY_AIR = "air";

    private static final int INDEX_FRAGMENT = 0;
    private static final int WEATHER_FRAGMENT = 1;
    private static final int PERSON_FRAGMENT = 2;
    private static final int TWO_SECOND = 2000;


    private boolean mIsLogin = false;
    private Person mPerson = null;

    private ImageView mNavIndexIv, mNavWeatherIv, mNavPersonIv;
    private TextView mNavIndexTv, mNavWeatherTv, mNavPersonTv;

    private DrawerLayout mDrawerLayout;
    private ImageView mUserCardBgIv;
    private CircleImageView mUserChatHeadIv;
    private TextView mUserNetNameTv;

    int fPosition;
    private FragmentManager mFragmentManager;
    private IndexFragment mIndexFragment;
    private WeatherFragment mWeatherFragment;
    private PersonFragment mPersonFragment;
    private String[] tags = new String[]{"indexFragment", "weatherFragment", "personFragment"};

    private long mExitTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActivityCollector
        ActivityCollector.addActivity(this);

        // 查询 SharedPreferences 的属性
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        // 首次打开
        boolean firstStart = preferences.getBoolean(KEY_FIRST_START, true);
        if (firstStart) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_START, false);
            editor.apply();

            Intent intent = new Intent(this, WelcomeActivity.class);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            startActivity(intent);
        }
        // 登录状态
        mIsLogin = preferences.getBoolean(KEY_IS_LOGIN, false);
        // 用户数据
        String personJson = preferences.getString(KEY_PERSON, null);
        if (personJson != null) {
            mPerson = new Gson().fromJson(personJson, Person.class);
        }

        // 初始化控件
        initView();

        // 更新登录状态和控件
        updateLoginStatus(mIsLogin);

        // 默认显示模块: 新闻模块
        fPosition = INDEX_FRAGMENT;
        resetSelected(INDEX_FRAGMENT);
        showContent(INDEX_FRAGMENT);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 初始化底部导航栏
        LinearLayout navIndexLl = findViewById(R.id.ll_nav_index);
        LinearLayout navWeatherLl = findViewById(R.id.ll_nav_weather);
        LinearLayout navPersonLl = findViewById(R.id.ll_nav_person);
        navIndexLl.setOnClickListener(this);
        navWeatherLl.setOnClickListener(this);
        navPersonLl.setOnClickListener(this);

        mNavIndexIv = findViewById(R.id.iv_nav_index);
        mNavWeatherIv = findViewById(R.id.iv_nav_weather);
        mNavPersonIv = findViewById(R.id.iv_nav_person);

        mNavIndexTv = findViewById(R.id.tv_nav_index);
        mNavWeatherTv = findViewById(R.id.tv_nav_weather);
        mNavPersonTv = findViewById(R.id.tv_nav_person);


        // 初始化侧滑菜单栏用户资料
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mUserCardBgIv = findViewById(R.id.iv_user_card_bg);
        mUserChatHeadIv = findViewById(R.id.iv_user_chat_head);
        mUserChatHeadIv.setOnClickListener(this);
        mUserNetNameTv = findViewById(R.id.tv_user_net_name);

        // 初始化侧滑菜单栏选项
        LinearLayout optionIndexLl = findViewById(R.id.ll_option_index);
        LinearLayout optionWeatherLl = findViewById(R.id.ll_option_weather);
        LinearLayout optionPersonLl = findViewById(R.id.ll_option_person);
        LinearLayout optionFavouriteLl = findViewById(R.id.ll_option_favourite);
        LinearLayout optionAboutLl = findViewById(R.id.ll_option_about);
        LinearLayout optionQuitLl = findViewById(R.id.ll_option_quit);
        optionIndexLl.setOnClickListener(this);
        optionWeatherLl.setOnClickListener(this);
        optionPersonLl.setOnClickListener(this);
        optionFavouriteLl.setOnClickListener(this);
        optionAboutLl.setOnClickListener(this);
        optionQuitLl.setOnClickListener(this);
    }

    /**
     * 重置底部导航栏状态
     */
    private void resetSelected(int position) {
        mNavIndexIv.setSelected(false);
        mNavWeatherIv.setSelected(false);
        mNavPersonIv.setSelected(false);

        mNavIndexTv.setSelected(false);
        mNavWeatherTv.setSelected(false);
        mNavPersonTv.setSelected(false);

        switch (position) {
            case INDEX_FRAGMENT:
                mNavIndexIv.setSelected(true);
                mNavIndexTv.setSelected(true);
                break;

            case WEATHER_FRAGMENT:
                mNavWeatherIv.setSelected(true);
                mNavWeatherTv.setSelected(true);
                break;

            case PERSON_FRAGMENT:
                mNavPersonIv.setSelected(true);
                mNavPersonTv.setSelected(true);
                break;

            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nav_index:
                resetSelected(INDEX_FRAGMENT);
                showContent(INDEX_FRAGMENT);
                break;

            case R.id.ll_nav_weather:
                resetSelected(WEATHER_FRAGMENT);
                showContent(WEATHER_FRAGMENT);
                break;

            case R.id.ll_nav_person:
                resetSelected(PERSON_FRAGMENT);
                showContent(PERSON_FRAGMENT);
                break;

            case R.id.iv_user_chat_head:
                if (!mIsLogin) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    if (mPerson != null) {
                        intent.putExtra(LoginActivity.KEY_ACCOUNT, mPerson.getAccount());
                    }
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_LOGIN);
                }
                break;

            case R.id.ll_option_index:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                resetSelected(INDEX_FRAGMENT);
                showContent(INDEX_FRAGMENT);
                break;

            case R.id.ll_option_weather:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                resetSelected(WEATHER_FRAGMENT);
                showContent(WEATHER_FRAGMENT);
                break;

            case R.id.ll_option_person:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                resetSelected(PERSON_FRAGMENT);
                showContent(PERSON_FRAGMENT);
                break;

            case R.id.ll_option_favourite:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivityForResult(intent1, MainActivity.REQUEST_CODE_FAVOURITE);
                break;

            case R.id.ll_option_about:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                showAboutPw();
                break;

            case R.id.ll_option_quit:
                ActivityCollector.finishAll();
                break;

            default:
        }
    }

    /**
     * FragmentManager
     */
    private void showContent(int to) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        hideFragment(ft);
        showFragment(ft, to);

//        mFragmentTransaction.replace(R.id.fl_content, getFragments(to), tags[to]);
//        mFragmentTransaction.addToBackStack(null);

        ft.commit();
    }

    /**
     * Fragment 隐藏
     */
    private void hideFragment(FragmentTransaction ft) {
        if (mIndexFragment != null) {
            ft.hide(mIndexFragment);
        }
        if (mWeatherFragment != null) {
            ft.hide(mWeatherFragment);
        }
        if (mPersonFragment != null) {
            ft.hide(mPersonFragment);
        }
    }

    /**
     * Fragment 显示
     */
    private void showFragment(FragmentTransaction ft, int to) {
        switch (to) {
            case INDEX_FRAGMENT:
                fPosition = INDEX_FRAGMENT;
                if (mIndexFragment == null) {
                    mIndexFragment = new IndexFragment();
                    ft.add(R.id.fl_content, mIndexFragment, tags[to]);
                } else {
                    ft.show(mIndexFragment);
                }
                break;

            case WEATHER_FRAGMENT:
                fPosition = WEATHER_FRAGMENT;
                if (mWeatherFragment == null) {
                    mWeatherFragment = new WeatherFragment();
                    ft.add(R.id.fl_content, mWeatherFragment, tags[to]);
                } else {
                    ft.show(mWeatherFragment);
                }
                break;

            case PERSON_FRAGMENT:
                fPosition = PERSON_FRAGMENT;
                if (mPersonFragment == null) {
                    mPersonFragment = new PersonFragment();
                    ft.add(R.id.fl_content, mPersonFragment, tags[to]);
                } else {
                    ft.show(mPersonFragment);
                }
                break;

            default:
        }
    }

    /**
     * Fragment 替换
     */
    private Fragment getFragments(int to) {
        Fragment fragment;
        switch (to) {
            case INDEX_FRAGMENT:
                fPosition = INDEX_FRAGMENT;
                mIndexFragment = new IndexFragment();
                fragment = mIndexFragment;
                break;

            case WEATHER_FRAGMENT:
                fPosition = WEATHER_FRAGMENT;
                mWeatherFragment = new WeatherFragment();
                fragment = mWeatherFragment;
                break;

            case PERSON_FRAGMENT:
                fPosition = PERSON_FRAGMENT;
                mPersonFragment = new PersonFragment();
                fragment = mPersonFragment;
                break;

            default:
                fragment = new Fragment();
        }

        return fragment;
    }

    /**
     * Activity 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                // 登录成功回调
                if (resultCode == RESULT_OK) {
                    String personJson = data.getStringExtra(KEY_PERSON);
                    mPerson = new Gson().fromJson(personJson, Person.class);

                    // 更新登录状态的 UI 和 SP
                    updateLoginStatus(true);

                    // 用户数据存入 SharedPreferences
                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putString(KEY_PERSON, personJson);
                    editor.apply();
                }
                break;

            case REQUEST_CODE_RESET_PASSWORD:
                // 重置密码成功回调
                if (resultCode == RESULT_OK) {
                    updateLoginStatus(false);

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    if (mPerson != null) {
                        intent.putExtra(LoginActivity.KEY_ACCOUNT, mPerson.getAccount());
                    }
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_LOGIN);
                }
                break;

            case REQUEST_CODE_FAVOURITE:
                // 我的收藏回调
                if (mIndexFragment != null) {
                    mIndexFragment.updateData();
                }
                break;

            case REQUEST_CODE_SEARCH:
                // 搜索回调
                if (mIndexFragment != null) {
                    mIndexFragment.updateData();
                }
                break;

            default:
        }
    }

    /**
     * 更新登录状态
     */
    public void updateLoginStatus(boolean isLogin) {
        mIsLogin = isLogin;

        if (isLogin) {
            Glide.with(this).load(R.drawable.user_card_bg).into(mUserCardBgIv);
            if (mPerson != null) {
                mUserNetNameTv.setText(mPerson.getName());
            }
        } else {
            mUserCardBgIv.setImageResource(R.color.colorBlack);
            mUserNetNameTv.setText(getResources().getString(R.string.person_tv_login));
        }

        if (mPersonFragment != null) {
            mPersonFragment.updateUI(isLogin);
        }

        // 保存登录状态
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_LOGIN, isLogin);
        editor.apply();
    }

    public boolean getIsLogin() {
        return mIsLogin;
    }

    public Person getPerson() {
        return mPerson;
    }

    public void showAboutPw() {
        //  获取屏幕的宽高像素
        Display display = this.getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        View pwView = LayoutInflater.from(this).inflate(R.layout.popupwindow_about, null);
        PopupWindow popupWindow = new PopupWindow(pwView,
                screenWidth / 5 * 4, screenHeight / 5 * 2, true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        View locationView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(locationView, Gravity.CENTER, 0, 0);
    }

    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 双击返回键退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > TWO_SECOND) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            } else {
                // 销毁所有 Activity
                ActivityCollector.finishAll();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
