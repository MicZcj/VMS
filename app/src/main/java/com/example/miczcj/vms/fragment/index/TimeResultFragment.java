package com.example.miczcj.vms.fragment.index;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.example.miczcj.vms.model.Time;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Widget(name = "查询结果")
public class TimeResultFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.pager)
    QMUIViewPager mViewPager;

    private QMUIPagerAdapter pagerAdapter;
    private List<String> mItems = new ArrayList<>();
    private ArrayList<Time> list = new ArrayList<Time>();
    private String id;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private BaseHttp baseHttp = new BaseHttp();
    private Handler handler =new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = (String)bundle.get("id");
        doPost();

    }

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_loop_viewpager, null);
        ButterKnife.bind(this, layout);
        initTopBar();
        initPagers();
        return layout;
    }

    private void initData(int count) {
        for (int i = 0; i < count; i++) {
            mItems.add("序号："+(i+1) + "\n" +
                    list.get(i).getName()+"\n" +
                    list.get(i).getStuclass()+"\n" +
                    list.get(i).getStuid()+"\n" +
                    list.get(i).getWorknum()+"\n" +
                    list.get(i).getActivity()+"\n" +
                    list.get(i).getTime());
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
        new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                     @Override
                     public void run() {
                        pagerAdapter = new QMUIPagerAdapter() {

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
                });
            }
        }.start();
    }

    private void doPost(){
        FormBody formBody = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl()+"APIWorkTimeQuery")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JsonElement je = new JsonParser().parse(result);
                JsonObject jo = je.getAsJsonObject();
                JsonArray ja = jo.getAsJsonArray("list");
                Gson gson = new Gson();
                for(JsonElement time : ja){
                    Time t = gson.fromJson(time,Time.class);
                    list.add(t);
                }
                initData(list.size());
                initPagers();
            }
        });
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
