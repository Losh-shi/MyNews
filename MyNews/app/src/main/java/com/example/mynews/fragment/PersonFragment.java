package com.example.mynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mynews.activity.FavouriteActivity;
import com.example.mynews.activity.ForgetActivity;
import com.example.mynews.activity.LoginActivity;
import com.example.mynews.activity.MainActivity;
import com.example.mynews.R;
import com.example.mynews.bean.Person;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author ggz
 * @date 2018/5/3
 */
public class PersonFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private MainActivity mActivity;

    private boolean mIsLogin = false;

    private ImageView mPersonCardBgIv;
    private TextView mPersonNetNameTv;
    private LinearLayout mPersonForgetLl;
    private Button mPersonLogoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_person, container, false);
        mActivity = (MainActivity) getActivity();

        initView(mView);

        // 获取登录状态， 更新 UI
        mIsLogin = mActivity.getIsLogin();
        updateUI(mIsLogin);

        return mView;
    }

    private void initView(View v) {

        mPersonCardBgIv = v.findViewById(R.id.iv_person_card_bg);

        CircleImageView personNetPicIv = v.findViewById(R.id.iv_person_chat_head);
        personNetPicIv.setOnClickListener(this);

        mPersonNetNameTv = v.findViewById(R.id.tv_person_net_name);

        LinearLayout personFavouriteLl = v.findViewById(R.id.ll_person_favourite);
        personFavouriteLl.setOnClickListener(this);

        mPersonForgetLl = v.findViewById(R.id.ll_person_forget);
        mPersonForgetLl.setOnClickListener(this);

        LinearLayout personAboutLl = v.findViewById(R.id.ll_person_about);
        personAboutLl.setOnClickListener(this);

        mPersonLogoutBtn = v.findViewById(R.id.btn_person_logout);
        mPersonLogoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_chat_head:
                if (!mIsLogin) {
                    Person person = mActivity.getPerson();
                    Intent intent1 = new Intent(mActivity, LoginActivity.class);
                    if (person != null) {
                        intent1.putExtra(LoginActivity.KEY_ACCOUNT, person.getAccount());
                    }
                    mActivity.startActivityForResult(intent1, MainActivity.REQUEST_CODE_LOGIN);
                }
                break;

            case R.id.ll_person_favourite:
                Intent intent2 = new Intent(mActivity, FavouriteActivity.class);
                mActivity.startActivityForResult(intent2, MainActivity.REQUEST_CODE_FAVOURITE);
                break;

            case R.id.ll_person_forget:
                Intent intent3 = new Intent(mActivity, ForgetActivity.class);
                mActivity.startActivityForResult(intent3, MainActivity.REQUEST_CODE_RESET_PASSWORD);
                break;

            case R.id.ll_person_about:
                mActivity.showAboutPw();
                break;

            case R.id.btn_person_logout:
                mActivity.updateLoginStatus(false);
                break;

            default:
        }
    }

    public void updateUI(boolean isLogin) {
        mIsLogin = isLogin;
        if (isLogin) {
            Person person = mActivity.getPerson();
            Glide.with(this).load(R.drawable.user_card_bg).into(mPersonCardBgIv);
            if (person != null) {
                mPersonNetNameTv.setText(person.getName());
            }
            mPersonForgetLl.setVisibility(View.VISIBLE);
            mPersonLogoutBtn.setVisibility(View.VISIBLE);
        } else {
            mPersonCardBgIv.setImageResource(R.color.colorBlack);
            mPersonNetNameTv.setText(getResources().getString(R.string.person_tv_login));
            mPersonForgetLl.setVisibility(View.GONE);
            mPersonLogoutBtn.setVisibility(View.GONE);
        }
    }
}
