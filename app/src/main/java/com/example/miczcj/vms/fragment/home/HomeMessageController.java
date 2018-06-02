package com.example.miczcj.vms.fragment.home;

import android.content.Context;
import android.util.Log;

import com.example.miczcj.vms.manager.QDDataManager;


/** 主界面，关于 QMUI Util 部分的展示。
 * Created by Kayo on 2016/11/21.
 */

public class HomeMessageController extends HomeController {

    public HomeMessageController(Context context) {
        super(context,2);
    }

    @Override
    protected String getTitle() {
        return "消息";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        Log.i("第二页的getContext的值：", getContext().toString());
        return new ItemAdapter(getContext(), QDDataManager.getInstance().getUtilDescriptions(),2);
    }
}
