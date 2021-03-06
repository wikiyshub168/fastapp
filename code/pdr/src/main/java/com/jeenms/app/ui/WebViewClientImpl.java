package com.jeenms.app.ui;

import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jeenms.app.commons.constant.AbsoluteConst;
import com.jeenms.app.util.FileUtils;
import com.jeenms.app.util.JSUtils;

/**
 * Created by zhangdy on 2017/3/21.
 */

public class WebViewClientImpl extends WebViewClient {

    private static final String TAG = "WebViewClientImpl";
    private static final String sRuntimeJsPath = "appAll.js";
    /*
     * 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
     */
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //执行all.js初始化plus等变量
        exePlusInit(view);
        //通知webview plus is ready
        onPlusReadyEvent(view);
        //触发事件
        //this.dispatchFrameViewEvents(AbsoluteConst.EVENTS_TITLE_UPDATE, view.getTitle());

    }

    private void exePlusInit(WebView view) {
        String jsPlusContent = new String(FileUtils.getAssetFileContent(view.getContext(),sRuntimeJsPath));
        JSUtils.exeJavaScript(view, jsPlusContent);
    }

    /**
     * 可以调用plus对象了
     */
    private void onPlusReadyEvent(final WebView view) {
        final StringBuffer jsplusready =  new StringBuffer();
        jsplusready.append("(function(){");//begin js
        jsplusready.append("var event = document.createEvent('HTMLEvents'); ");
        jsplusready.append("event.initEvent('plusready', true, true); ");
        jsplusready.append("document.dispatchEvent(event); ");
        jsplusready.append("})();");//end js
        //System.out.println(js_fun_plusready.toString());

        JSUtils.exeJavaScript(view, jsplusready.toString());

//        Handler mHandler = new Handler();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    view.loadUrl("javascript:" + jsplusready.toString());
//                }catch (Exception e){
//                    Log.e(TAG, e.toString());
//                }
//            }
//        });
    }



}
