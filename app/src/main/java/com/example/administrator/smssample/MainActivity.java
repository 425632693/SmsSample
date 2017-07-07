package com.example.administrator.smssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.HashMap;
import java.util.Random;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity {

    private Button bing_phone;
    private EventHandler eventHandler;
    String APPKEY = "1e0a28f425488";
    String APPSECRETE = "40755ed0778abc42fc966611e3854ff5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSSDK.initSDK(this,APPKEY,APPSECRETE);
        //初始化控件
        initView();

    }

    //初始化控件
    private void initView(){
        bing_phone = (Button) findViewById(R.id.regist_phone);
        bing_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册手机号
                RegisterPage registerPage = new RegisterPage();

                //注册回调事件
                registerPage.setRegisterCallback(eventHandler);

                //事件完成后调用
                eventHandler = new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        //判断结果是否获取到
                        if(result == SMSSDK.RESULT_COMPLETE){
                            //获取数据data
                            HashMap<String,Object> maps = (HashMap<String, Object>) data;
                            //国家信息
                            String country = (String) maps.get("country");
                            //手机号信息
                            String phone = (String) maps.get("phone");
                            //提交用户信息
                            submitUserInfo(country,phone);
                        }

                    }
                };

                //显示注册界面
                registerPage.show(MainActivity.this);

//                // 注册监听器
//                SMSSDK.registerEventHandler(eventHandler);
            }
        });
    }

    /**
     * 提交用户信息
     * @param country   国家信息
     * @param phone     手机号码
     */
    public void submitUserInfo(String country,String phone){
        //生成一个随机数
        Random r = new Random();
        String uid = Math.abs(r.nextInt())+"";
        String nickName = "XiaoCui";

        //调用服务器的提交方法(五个参数:1.用户id 2.用户昵称 3.null 4.国家信息 5.手机号)
        SMSSDK.submitUserInfo(uid,nickName,null,country,phone);
    }



    //注销SDK
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}

