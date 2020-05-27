package com.example.my.renews.person;

import com.example.my.renews.base.BaseFragment;
import com.example.my.renews.main.MainActivity;
import com.example.my.renews.R;

public class PersonFragment extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_person;
    }
}
