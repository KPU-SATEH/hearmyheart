package com.example.hearmyheart;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class CustomWebChromeClient extends WebChromeClient {

    /**
     * 페이지를 로딩하는 현재 진행 상황을 전해줍니다.
     * newProgress  현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Log.i("WebView", "Progress: " + String.valueOf(newProgress));
        super.onProgressChanged(view, newProgress);
    }
/* 결과 :
Progress: 10
Progress: 15
Progress: 35
...
Progress: 76
Progress: 100
*/

    /**
     * 현재 페이지에 새로운 favicon이 있다고 알립니다.
     * icon 현재 페이지의 favicon이 들어있는 비트맵
     */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }


/*
favicon이란:
일반적으로 웹 브라우저의 URL이 표시되기 전에 특정 웹사이트와 관련된 16 × 16 픽셀 아이콘
*/

    /**
     * 문서 제목에 변경이 있다고 알립니다.
     * title  문서의 새로운 타이틀이 들어있는 문자열
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }
/*  아래처럼 title 태그 사이의 값을 가져옵니다.
<title> LG텔레콤 전자결제 서비스 </title>
*/

    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean dialog,
                                  boolean userGesture, Message resultMsg) {
        return super.onCreateWindow(view, dialog, userGesture, resultMsg);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long currentQuota, long estimatedSize, long totalUsedQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
                estimatedSize, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }


    @Override
    public boolean onJsBeforeUnload(WebView view, String url,
                                    String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsTimeout() {
        return super.onJsTimeout();
    }


    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded,
                                         long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }


    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }
}
