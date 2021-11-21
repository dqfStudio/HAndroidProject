package com.funfish.lebo.net;

import android.provider.Settings;

import com.funfish.lebo.BuildConfig;
import com.funfish.lebo.utils.MD5Utils;

import org.xutils.http.RequestParams;
import org.xutils.x;

public class AppNet {


    public void getNetinfo(String url, ApiListener apiListener) {


        String data_str = System.currentTimeMillis()+"";
        RequestParams params = new RequestParams(url+"/api/check/version");

        String uuid = Settings.Secure.getString(x.app().getContentResolver(), Settings.Secure.ANDROID_ID);
        String key = BuildConfig.SecretKey; // 密钥
        String sign = MD5Utils.digest(key+uuid+data_str+"/api/check/version"+key).toUpperCase();//`${key}${signData}${uuid}${timeStamp}${key}`).toUpperCase();

        params.setHeader("Content-Type","application/json");
        params.setHeader("SIGN",sign);
        params.setHeader("TIMESTAMP",data_str);
        params.setHeader("UDID",uuid);
        params.setHeader("APP-TYPE","1");
        params.setHeader("APP-VERSION", BuildConfig.VERSION_NAME);
        params.setHeader("PACK-NAME", BuildConfig.PackName);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params,apiListener,"getNetinfo");

    }

    public  void  getYuMing(ApiListener apiListener){
        String data_str = System.currentTimeMillis()+"";
//        String url = "https://m.weibo.cn/api/container/getIndex?is_all[]=1%3Fis_all%3D1&is_all[]=1&jumpfrom=weibocom&id=1&tabKey=weibo&must_show=1&hidden=0&type=uid&value=2671342627&containerid=1076032671342627&myid="+data_str+"/";
//        String url ="https://m.weibo.cn/comments/hotflow?id=4641577341032015&mid=4641577341032015&max_id_type=0";
        RequestParams params = new RequestParams(BuildConfig.WeiboUrl);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params,apiListener,"getYuMing");
    }


}
