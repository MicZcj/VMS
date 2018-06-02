package com.example.miczcj.vms.fragment.index;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.fragment.index.viewpager.CardTransformer;
import com.example.miczcj.vms.manager.QDDataManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Widget(name = "查询结果")
public class TimeResultFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.pager)
    QMUIViewPager mViewPager;

    private List<String> mItems = new ArrayList<>();


    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_loop_viewpager, null);
        ButterKnife.bind(this, layout);
        initData(5);
        initTopBar();
        initPagers();
        return layout;
    }

    private void initData(int count) {
        for (int i = 1; i < count; i++) {
            mItems.add("序号："+i + "\n张昌健\n健行学院1502班\n201507420139\n9.0\n灵隐寺志愿者活动\n2018.04.05");
        }
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.setTitle(QDDataManager.getInstance().getDescription(this.getClass()).getName());
    }


    private void initPagers() {
        QMUIPagerAdapter pagerAdapter = new QMUIPagerAdapter() {

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mItems.get(position);
            }

            @Override
            protected Object hydrate(ViewGroup container, int position) {
                return new ItemView(getContext());
            }

            @Override
            protected void populate(ViewGroup container, Object item, int position) {
                ItemView itemView = (ItemView) item;
                itemView.setText(mItems.get(position));
                container.addView(itemView);
            }

            @Override
            protected void destroy(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        //setPageTransformer默认采用ViewCompat.LAYER_TYPE_HARDWARE， 但它在某些4.x的国产机下会crash
        boolean canUseHardware = Build.VERSION.SDK_INT >= 21;
        mViewPager.setPageTransformer(false, new CardTransformer(),
                canUseHardware ? ViewCompat.LAYER_TYPE_HARDWARE : ViewCompat.LAYER_TYPE_SOFTWARE);
        mViewPager.setInfiniteRatio(500);
        mViewPager.setEnableLoop(true);
        mViewPager.setAdapter(pagerAdapter);
    }

    static class ItemView extends FrameLayout {
        private TextView mTextView;

        public ItemView(Context context) {
            super(context);
            mTextView = new TextView(context);
            mTextView.setTextSize(20);
            mTextView.setTextColor(ContextCompat.getColor(context, R.color.app_color_theme_5));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.qmui_config_color_white));
            mTextView.setLineSpacing(10,1);
            int size = QMUIDisplayHelper.dp2px(context, 300);
            LayoutParams lp = new LayoutParams(size, size);
            lp.gravity = Gravity.CENTER;
            addView(mTextView, lp);
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }
    }
}





//public class TimeResultFragment extends BaseFragment {
//    private static final String TAG = "工时查询";
//    QDRecyclerViewAdapter mRecyclerViewAdapter;
//    LinearLayoutManager mPagerLayoutManager;
//
//    @BindView(R.id.recyclerView)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.collapsing_topbar_layout)
//    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;
//    @BindView(R.id.topbar)
//    QMUITopBar mTopBar;
//
//    private QDItemDescription mQDItemDescription;
//
//    @Override
//    protected View onCreateView() {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_collapsing_topbar_layout, null);
//        ButterKnife.bind(this, view);
//        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
//
//        initTopBar();
//        mPagerLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mPagerLayoutManager);
//        mRecyclerViewAdapter = new QDRecyclerViewAdapter();
//        mRecyclerViewAdapter.setItemCount(10);
//        mRecyclerView.setAdapter(mRecyclerViewAdapter);
//
//        mCollapsingTopBarLayout.setScrimUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.i(TAG, "scrim: " + animation.getAnimatedValue());
//            }
//        });
//        return view;
//    }
//
//    @Override
//    protected boolean translucentFull() {
//        return true;
//    }
//
//    private void initTopBar() {
//        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popBackStack();
//            }
//        });
//
//        mCollapsingTopBarLayout.setTitle(mQDItemDescription.getName());
//    }
//}
