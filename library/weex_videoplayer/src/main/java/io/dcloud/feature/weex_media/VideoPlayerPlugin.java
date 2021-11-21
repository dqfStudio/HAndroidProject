//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.feature.weex_media;

import android.content.Context;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.SimpleComponentHolder;
import io.dcloud.feature.weex.WeexInstanceMgr;
import io.dcloud.feature.weex_media.VideoInnerViewComponent.Ceator;

public class VideoPlayerPlugin {
    public VideoPlayerPlugin() {
    }

    public static void initPlugin(Context context) {
        try {
            WXSDKEngine.registerComponent("u-video", VideoComponent.class);
            WXSDKEngine.registerComponent(new SimpleComponentHolder(VideoInnerViewComponent.class, new Ceator()), false, new String[]{"u-scalable"});
            WeexInstanceMgr.self().addComponentByName("video", VideoComponent.class);
            WeexInstanceMgr.self().addComponentByName("div", VideoInnerViewComponent.class);
        } catch (WXException var2) {
            var2.printStackTrace();
        }

    }
}
