package com.example.mynews.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mynews.R;
import com.example.mynews.bean.News;
import com.example.mynews.manager.NewsManager;

import java.util.List;

/**
 * NewsListAdapter
 *
 * @author ggz
 * @date 2018/1/14
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ONE = 1;
    private static final int VIEW_TYPE_TWO = 2;
    private static final int VIEW_TYPE_THREE = 3;


    private Context mContext;
    private List<News> mList;
    private News mNews;
    private PopupWindow mPopupWindow;

    private OnItemClickListener mOnItemClickListener;
    private OnDataChangeListener mOnDataChangeListener;

    private int mOperationType = 0;

    private static class ViewHolderOne extends RecyclerView.ViewHolder {
        View itemView;
        TextView titleTv;
        TextView pubTv;
        TextView dateTv;
        ImageView setIv;

        private ViewHolderOne(View view) {
            super(view);
            itemView = view;
            titleTv = view.findViewById(R.id.tv_type_one_title);
            pubTv = view.findViewById(R.id.tv_type_one_pub);
            dateTv = view.findViewById(R.id.tv_type_one_date);
            setIv = view.findViewById(R.id.iv_type_one_set);
        }
    }

    private static class ViewHolderTwo extends RecyclerView.ViewHolder {
        View itemView;
        TextView titleTv;
        TextView pubTv;
        TextView dateTv;
        ImageView imageIv;
        ImageView setIv;

        private ViewHolderTwo(View view) {
            super(view);
            itemView = view;
            titleTv = view.findViewById(R.id.tv_type_two_title);
            pubTv = view.findViewById(R.id.tv_type_two_pub);
            dateTv = view.findViewById(R.id.tv_type_two_date);
            imageIv = view.findViewById(R.id.iv_type_two_image);
            setIv = view.findViewById(R.id.iv_type_two_set);
        }
    }

    private static class ViewHolderThree extends RecyclerView.ViewHolder {
        View itemView;
        TextView titleTv;
        TextView pubTv;
        TextView dateTv;
        ImageView image1Iv;
        ImageView image2Iv;
        ImageView image3Iv;
        ImageView setIv;

        private ViewHolderThree(View view) {
            super(view);
            itemView = view;
            titleTv = view.findViewById(R.id.tv_type_three_title);
            pubTv = view.findViewById(R.id.tv_type_three_pub);
            dateTv = view.findViewById(R.id.tv_type_three_date);
            image1Iv = view.findViewById(R.id.iv_type_three_image1);
            image2Iv = view.findViewById(R.id.iv_type_three_image2);
            image3Iv = view.findViewById(R.id.iv_type_three_image3);
            setIv = view.findViewById(R.id.iv_type_three_set);
        }
    }


    public NewsListAdapter(Context context, List<News> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mList.get(position).getImageList().size();
        if (type <= 0) {
            return VIEW_TYPE_ONE;
        } else if (type < 3) {
            return VIEW_TYPE_TWO;
        } else {
            return VIEW_TYPE_THREE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ONE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_type_one, parent, false);
            return new ViewHolderOne(view);
        }

        if (viewType == VIEW_TYPE_TWO) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_type_two, parent, false);
            return new ViewHolderTwo(view);
        }

        if (viewType == VIEW_TYPE_THREE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_type_three, parent, false);
            return new ViewHolderThree(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        mNews = mList.get(position);

        if (holder instanceof ViewHolderOne) {
            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.titleTv.setText(mNews.getTitle());
            viewHolderOne.pubTv.setText(mNews.getPubName());
            viewHolderOne.dateTv.setText(mNews.getPubDate());
            viewHolderOne.setIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolderOne.getAdapterPosition();
                    showPW(v, position);
                }
            });
            viewHolderOne.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = viewHolderOne.getAdapterPosition();
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }

        if (holder instanceof ViewHolderTwo) {
            final ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.titleTv.setText(mNews.getTitle());
            viewHolderTwo.pubTv.setText(mNews.getPubName());
            viewHolderTwo.dateTv.setText(mNews.getPubDate());
            Glide.with(mContext)
                    .load(mNews.getImageList().get(0))
                    .placeholder(R.color.colorGrey)
                    .error(R.color.colorBlack)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)// 禁用缓存功能
//                .override(100, 100)// 指定图片大小
                    .into(viewHolderTwo.imageIv);
            viewHolderTwo.setIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolderTwo.getAdapterPosition();
                    showPW(v, position);
                }
            });
            viewHolderTwo.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = viewHolderTwo.getAdapterPosition();
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

        }

        if (holder instanceof ViewHolderThree) {
            final ViewHolderThree viewHolderThree = (ViewHolderThree) holder;
            viewHolderThree.titleTv.setText(mNews.getTitle());
            viewHolderThree.pubTv.setText(mNews.getPubName());
            viewHolderThree.dateTv.setText(mNews.getPubDate());
            Glide.with(mContext)
                    .load(mNews.getImageList().get(0))
                    .placeholder(R.color.colorGrey)
                    .error(R.color.colorBlack)
                    .into(viewHolderThree.image1Iv);
            Glide.with(mContext)
                    .load(mNews.getImageList().get(1))
                    .placeholder(R.color.colorGrey)
                    .error(R.color.colorBlack)
                    .into(viewHolderThree.image2Iv);
            Glide.with(mContext)
                    .load(mNews.getImageList().get(2))
                    .placeholder(R.color.colorGrey)
                    .error(R.color.colorBlack)
                    .into(viewHolderThree.image3Iv);
            viewHolderThree.setIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolderThree.getAdapterPosition();
                    showPW(v, position);
                }
            });
            viewHolderThree.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = viewHolderThree.getAdapterPosition();
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    private void showPW(View v, int position) {

        mNews = mList.get(position);
        final boolean isFavourite = mNews.getIsFavourite();

        View pwView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_item_set, null);
        mPopupWindow = new PopupWindow(
                pwView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        final ImageView pwFavIv = pwView.findViewById(R.id.iv_pw_fav);
        if (isFavourite) {
            pwFavIv.setSelected(true);
        } else {
            pwFavIv.setSelected(false);
        }

        LinearLayout pwFavLl = pwView.findViewById(R.id.ll_pw_fav);
        pwFavLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = pwFavIv.isSelected();
                pwFavIv.setSelected(!status);
                mNews.setIsFavourite(!status);

                // 判断状态是否改变
                if (mNews.getIsFavourite() != isFavourite) {
                    mOperationType = 1;
                } else {
                    mOperationType = 0;
                }
            }
        });

        LinearLayout pwDeleteLl = pwView.findViewById(R.id.ll_pw_delete);
        pwDeleteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(mNews);
                notifyDataSetChanged();

                mOperationType = 2;
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(v, 0, -50);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChange(mOperationType, mNews);
                    mOperationType = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    /**
     * 接口: OnItemClickListener
     */
    public interface OnItemClickListener {
        /**
         * onItemClick
         *
         * @param view     itemView
         * @param position item position
         */
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    /**
     * 接口: OnDataChangeListener
     */
    public interface OnDataChangeListener {
        /**
         * onDataChange
         *
         * @param operationType 操作类型
         * @param news          数据
         */
        void onDataChange(int operationType, News news);
    }

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        mOnDataChangeListener = listener;
    }
}
