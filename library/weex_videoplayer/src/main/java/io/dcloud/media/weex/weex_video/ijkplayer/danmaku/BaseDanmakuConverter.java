//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.danmaku;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.FBDanmaku;
import master.flame.danmaku.danmaku.model.FTDanmaku;
import master.flame.danmaku.danmaku.model.R2LDanmaku;

public abstract class BaseDanmakuConverter<T extends BaseDanmakuData> {
    public BaseDanmakuConverter() {
    }

    public abstract T convertDanmaku(BaseDanmaku var1);

    protected void initData(T data, BaseDanmaku danmaku) {
        int danmakuType = 1;
        if (danmaku instanceof R2LDanmaku) {
            danmakuType = 1;
        } else if (danmaku instanceof FBDanmaku) {
            danmakuType = 4;
        } else if (danmaku instanceof FTDanmaku) {
            danmakuType = 5;
        }

        data.setType(danmakuType);
        data.setContent(danmaku.text.toString());
        data.setTime(danmaku.getTime());
        data.setTextSize(danmaku.textSize);
        data.setTextColor(danmaku.textColor);
    }
}
