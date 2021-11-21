package com.funfish.lebo.net;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;


public interface ApiListener {

    void onCancelled(Callback.CancelledException var1);

    void onComplete(RequestParams var1, String var2, String type);

    void onError(Map<String, String> var1, RequestParams var2);

    void onExceptionType(Throwable var1, RequestParams params, String type);

}



