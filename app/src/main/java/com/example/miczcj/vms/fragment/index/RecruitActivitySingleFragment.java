package com.example.miczcj.vms.fragment.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib.annotation.Group;
import com.example.lib.annotation.annotation.Widget;
import com.example.miczcj.vms.R;
import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.model.QDItemDescription;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by MicZcj on 2018/5/27.
 */
@Widget(group = Group.Other, name = "活动详情")
public class RecruitActivitySingleFragment extends BaseFragment {
    //private static final String ARG_INDEX = "arg_index";

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private QDItemDescription mQDItemDescription;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return ContentPage.SIZE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ContentPage page = ContentPage.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };


    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_recruit_activity_single, null);
        ButterKnife.bind(this, view);

        initTopBar();
        initTabAndPager();


        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.setTitle("浙江图书馆志愿者活动");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetList();
                    }
                });
    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("开启活动招募")
                .addItem("停止活动招募")
                .addItem("修改活动招募")
                .addItem("删除本招募信息")
                .addItem("导出报名信息（Excel）")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                //开启活动招募
                                break;
                            case 1:
                                //停止活动招募
                                break;
                            case 2:
                                //修改活动招募,跳转到RecruitNewActivity
                                QMUIFragment fragment = new RecruitActivityNewFragment();
                                startFragment(fragment);
                                break;
                            case 3:
                                //删除本招募信息
                                break;
                            case 4:
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorPosition(false);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.addTab(new QMUITabSegment.Tab("招募详情"));
        mTabSegment.addTab(new QMUITabSegment.Tab("报名信息"));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    public static RecruitActivitySingleFragment newInstance(int index) {
        Bundle args = new Bundle();
        RecruitActivitySingleFragment fragment = new RecruitActivitySingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View getPageView(ContentPage page) {

        View view;


        if (page == ContentPage.Item1) {
            view = View.inflate(getContext(), R.layout.recruit_single, null);
            //把数据库去除的数据设置进去
        } else {
            QMUIGroupListView mGroupListView = new QMUIGroupListView(getContext());
            //把报名的人的信息写到GroupList中去
            QMUICommonListItemView itemInfo = mGroupListView.createItemView("叶茜雅");
            itemInfo.setDetailText("201706051812");
            itemInfo.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

            //监听事件 显示报名活动的志愿者的详细信息
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QMUIFragment fragment = new RecruitInfoFragment();
                    startFragment(fragment);
                }
            };

            //把itemInfo加入到GroupList里面
            QMUIGroupListView.newSection(getContext()).addItemView(itemInfo, onClickListener).addTo(mGroupListView);

            view = mGroupListView;

        }


        return view;
    }

    public enum ContentPage {
        Item1(0),
        Item2(1);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            return position;
        }
    }
}
