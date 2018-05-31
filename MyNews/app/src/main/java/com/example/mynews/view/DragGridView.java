package com.example.mynews.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mynews.R;
import com.example.mynews.adapter.DragAdapter;
import com.example.mynews.util.DisplayUtil;


/**
 * DragGridView
 *
 * @author ggz
 * @date 2017/12/27
 */

public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";
    private static final int ITEM_COLUMNS = 4;
    private static final double ITEM_SCALE = 1.10D;

    /**
     * 相对于屏幕的 (x,y)
     */
    private int mRawX;
    private int mRawY;
    /**
     * 相对于 GridView 的 (x,y)
     */
    private int mGvX;
    private int mGvY;
    /**
     * 相对于 item 的 (x,y)
     */
    private int mItemX;
    private int mItemY;

    /**
     * Position
     */
    private int mDragPosition;
    private int mDropPosition;

    /**
     * Item 镜像
     */
    private ImageView mDragImageView = null;

    /**
     * WindowManager
     */
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams = null;

    /**
     * 最后的动画 ID
     */
    private String mLastAnimationID;

    /**
     * Item 的宽、高
     */
    private int mItemWidth;
    private int mItemHeight;
    /**
     * Item 的间距
     */
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;

    /**
     * 震动
     */
    private Vibrator mVibrator;

    /**
     * 编辑模式
     */
    private boolean isEditStatus = false;
    /**
     * 移动状态
     */
    private boolean isMoving = false;


    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        // WindowManager 管理器
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mHorizontalSpacing = DisplayUtil.dip2px(context, mHorizontalSpacing);
        mVerticalSpacing = DisplayUtil.dip2px(context, mVerticalSpacing);

        // 震动器
//        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * onMeasure
     * 重写 GridView 的高度测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 因为在 ScrollView 里嵌套了 GridView ，需重设高度，显示 GridView 全部 item
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // 记录点击坐标
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 相对于父控件的 (x,y) 坐标
            mGvX = (int) ev.getX();
            mGvY = (int) ev.getY();
            // 相对于屏幕的 (x,y) 坐标
            mRawX = (int) ev.getRawX();
            mRawY = (int) ev.getRawY();
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 编辑状态
        if (isEditStatus) {
            // 记录实时移动坐标
            int gx = (int) ev.getX();
            int gy = (int) ev.getY();
            int rx = (int) ev.getRawX();
            int ry = (int) ev.getRawY();

            // 根据事件进行分类
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mDragImageView != null) {
                        mWindowManager.removeView(mDragImageView);
                        mDragImageView = null;
                    }
                    // 准备拖动，创键 mDragImageView
                    prepareDrag();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mDragImageView != null) {
                        // 正在拖动，更新 mDragImageView 的实时位置
                        onDrag(rx, ry);
                        // 判断拖动位置，进行换位动画
                        if (!isMoving) {
                            onMove(gx, gy);
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (mDragImageView != null) {
                        // 结束拖动，删除 mDragImageView
                        stopDrag();
                    }
                    break;

                default:
            }

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 准备拖动
     */
    public void prepareDrag() {
        // 根据相对于 GridView 的 (x,y) 坐标，判断点击 item 的 position
        int position = pointToPosition(mGvX, mGvY);
        Log.d(TAG, " -- prepareDrag() position " + position);

        // 无效位置
        if (position == -1 || position == 0) {
            mDragImageView = null;
            return;
        }

        // ToDo ????
        isMoving = false;

        // ScrollView 的滑动冲突，在准备拖动时，让父容器不处理滑动事件
        requestDisallowInterceptTouchEvent(true);

        // 记录拖动位置
        mDragPosition = position;


        View itemView = getChildAt(mDragPosition - getFirstVisiblePosition());
        // ToDo itemView.setVisibility(INVISIBLE)
        // 隐藏 item
        DragAdapter dragAdapter = (DragAdapter) getAdapter();
        dragAdapter.hideItem(mDragPosition);

        // 设置拖动 item 的状态，用于作为 Bitmap 数据
        itemView.setPressed(true);
        ImageView itemDeleteIv = itemView.findViewById(R.id.iv_grid_view_item_delete);
        itemDeleteIv.setVisibility(View.VISIBLE);

        // 获取 item 的 宽 高 数量
        mItemHeight = itemView.getHeight();
        mItemWidth = itemView.getWidth();
//        int itemTotal = getCount();

        // 获取相对于 item 里面的距离，用于 DragImageView 的位置定位
        mItemX = mGvX - itemView.getLeft();
        mItemY = mGvY - itemView.getTop();

//        // 算出 DragGridView 的行数
//        int row = mItemTotalCount / ITEM_COLUMNS;
//        // 算出最后一行多出的 item 数量
//        mRemainder = (mItemTotalCount % ITEM_COLUMNS);
//        if (mRemainder != 0) {
//            mRows = row + 1;
//        } else {
//            mRows = row;
//        }

        // 获取拖动 item 的 Bitmap 数据
        itemView.destroyDrawingCache();
        itemView.setDrawingCacheEnabled(true);
        Bitmap dragBitmap = Bitmap.createBitmap(itemView.getDrawingCache());

        // 创建拖动镜像 mDragImageView
        createDragImageView(dragBitmap);

        // 震动
//        mVibrator.vibrate(50);
    }

    /**
     * 创建拖动镜像 mDragImageView
     */
    public void createDragImageView(Bitmap dragBitmap) {
        // 设置布局属性
        mWindowParams = new WindowManager.LayoutParams();
        // 定位
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        // 效果一：目标 item 的中间位置
//		mWindowParams.x = rx - (int)((mItemWidth / 2) * ITEM_SCALE);
//		mWindowParams.y = ry - (int)((mItemHeight / 2) * ITEM_SCALE);
        // 效果二：目标 item 的左上角位置
        mWindowParams.x = mRawX - mItemX;
        mWindowParams.y = mRawY - mItemY;
        // 宽和高
        mWindowParams.width = (int) (ITEM_SCALE * dragBitmap.getWidth());
        mWindowParams.height = (int) (ITEM_SCALE * dragBitmap.getHeight());
        // 属性 ：flag 设置 Window 属性，type 设置 Window 类别（层级）
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // 透明度
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.alpha = 0.6f;
        mWindowParams.windowAnimations = 0;

        // 新建 ImageView 显示内容为 ItemView 的 Bitmap 数据
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(dragBitmap);

        // 在 Windows 里添加 ImageView ，用于被拖拽
        mWindowManager.addView(mDragImageView, mWindowParams);
    }

    /**
     * 删除拖动镜像 mDragImageView
     */
    private void deleteDragImageView() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 正在拖动
     * 更新 mDragImageView 的实时位置
     */
    private void onDrag(int rx, int ry) {
        // 效果一：
//			mWindowParams.x = rx - (int)((mItemWidth / 2) * ITEM_SCALE);
//			mWindowParams.y = ry - (int)((mItemHeight / 2) * ITEM_SCALE);
        // 效果二：
        mWindowParams.x = rx - mItemX;
        mWindowParams.y = ry - mItemY;
        mWindowManager.updateViewLayout(mDragImageView, mWindowParams);
    }

    /**
     * 结束拖动
     * 删除 mDragImageView
     */
    private void stopDrag() {
        // ScrollView 的滑动冲突
        requestDisallowInterceptTouchEvent(false);

        deleteDragImageView();

        DragAdapter dragAdapter = (DragAdapter) getAdapter();
        dragAdapter.notifyDataSetChanged();
    }

    /**
     * 判断拖动位置
     * 进行换位动画
     */
    public void onMove(int x, int y) {
        // 正在拖动的 mDragImageView 下方的 Position
        int position = pointToPosition(x, y);

        // 如果 位置无效 或 原位置一样，结束函数
        if (position == -1 || position == 0 || position == mDragPosition) {
            return;
        }

        // 记录可能要进行交换 position
        mDropPosition = position;

        // 计算要移动的 item 数目
        int moveCount = mDropPosition - mDragPosition;
        int moveCountAbs = Math.abs(moveCount);

        // 当移动完一次位置时，未放手，还需再次移动，则需再次隐藏当前位置下面的 item
        View dragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
        dragItemView.setVisibility(INVISIBLE);

        // 记录要移动的距离（根据比例）
        float toX;
        float toY;
        float xValue = ((float) mHorizontalSpacing / (float) mItemWidth) + 1.0f;
        float yValue = ((float) mVerticalSpacing / (float) mItemHeight) + 1.0f;

        for (int i = 0; i < moveCountAbs; i++) {
            int holdPosition;
            // 从前往后拖动，左移
            if (moveCount > 0) {
                holdPosition = mDragPosition + 1 + i;
                // 判断是不是同一行的
                if (mDragPosition / ITEM_COLUMNS == holdPosition / ITEM_COLUMNS) {
                    // 左移一位
                    toX = -xValue;
                    toY = 0;
                } else if (holdPosition % 4 == 0) {
                    // 下一行第一个 右移三位，上移一位
                    toX = 3 * xValue;
                    toY = -yValue;
                } else {
                    // 下一行其他 左移一位
                    toX = -xValue;
                    toY = 0;
                }
            } else {
                // 从后往前拖动，右移
                holdPosition = mDragPosition - 1 - i;
                if (mDragPosition / ITEM_COLUMNS == holdPosition / ITEM_COLUMNS) {
                    toX = xValue;
                    toY = 0;
                } else if ((holdPosition + 1) % 4 == 0) {
                    toX = -3 * xValue;
                    toY = yValue;
                } else {
                    toX = xValue;
                    toY = 0;
                }
            }

            // 开始要换位 item 的动画
            View view = getChildAt(holdPosition - getFirstVisiblePosition());
            Animation moveAnimation = getMoveAnimation(toX, toY);
            view.startAnimation(moveAnimation);
            //如果是最后一个移动的，那么设置他的最后个动画ID为LastAnimationID
            if (holdPosition == mDropPosition) {
                mLastAnimationID = moveAnimation.toString();
            }
            moveAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isMoving = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // 如果为最后个动画结束，那执行下面的方法
                    if (animation.toString().equalsIgnoreCase(mLastAnimationID)) {
                        // 更新 DragAdapter
                        DragAdapter dragAdapter = (DragAdapter) getAdapter();
                        dragAdapter.exchangeItem(mDragPosition, mDropPosition);
                        mDragPosition = mDropPosition;
                        isMoving = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    /**
     * 获取移动动画
     */
    public Animation getMoveAnimation(float toXValue, float toYValue) {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, toXValue,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, toYValue);
        // 设置一个动画效果执行完毕后，View对象保留在终止的位置。
        mTranslateAnimation.setFillAfter(true);
        mTranslateAnimation.setDuration(300L);
        return mTranslateAnimation;
    }


    /**
     * 设置编辑模式
     */
    public void setEditStatus(boolean b) {
        isEditStatus = b;
    }
}
