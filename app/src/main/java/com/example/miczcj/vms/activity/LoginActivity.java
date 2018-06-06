package com.example.miczcj.vms.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miczcj.vms.R;
import com.example.miczcj.vms.model.User;
import com.example.miczcj.vms.model.UserBean;
import com.example.miczcj.vms.okhttp.BaseHttp;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class LoginActivity extends AppCompatActivity {
    TextView loginBtn;
    EditText usernameEdt;
    EditText passwordEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();


    }

    private void initWidget() {
        usernameEdt = (EditText) findViewById(R.id.username);
        passwordEdt = (EditText) findViewById(R.id.password);

        loginBtn = (TextView) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPost();

            }
        });
    }

    private void loginPost() {
        BaseHttp baseHttp = new BaseHttp();
        String username = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        Log.i("用户名：", username);
        Log.i("密码：", password);

        //1.new一个OkhttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request对象

        FormBody fromBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(baseHttp.getUrl() + "APILogin")
                .post(fromBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("登陆返回结果：", "失败！");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO
                String result = response.body().string();
                JsonElement je = new JsonParser().parse(result);
                if(je.getAsJsonObject().get("code").toString().equals("0")){
                    Log.i("返回结果", result);
                    UserBean userBean = new Gson().fromJson(result, UserBean.class);
                    saveAccount(userBean.getUser());
                    Intent it = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(it);
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

    }

    public void saveAccount(User user) {
        SharedPreferences preference = getSharedPreferences("login_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("uid", user.getUid());
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("dept", user.getDept());
        editor.putString("authority", user.getAuthority());
        editor.commit();
    }
}
