//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.danmaku;

import android.graphics.Color;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.util.DanmakuUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandardDanmaKuParser extends AcFunDanmakuParser {
    private int count = 0;

    public StandardDanmaKuParser() {
    }

    protected Danmakus doParse(JSONArray danmakuListData) {
        Danmakus danmakus = new Danmakus();
        if (danmakuListData == null) {
            return danmakus;
        } else {
            this.count = danmakuListData.length();

            try {
                for(int i = 0; i < this.count; ++i) {
                    JSONObject danmakuArray = danmakuListData.getJSONObject(i);
                    if (danmakuArray != null) {
                        danmakus = this._parse(danmakuArray, danmakus);
                    }
                }
            } catch (JSONException var5) {
                var5.printStackTrace();
            }

            return danmakus;
        }
    }

    protected Danmakus _parse(JSONObject jsonObject, Danmakus danmakus) {
        if (danmakus == null) {
            danmakus = new Danmakus();
        }

        if (jsonObject != null && jsonObject.length() != 0) {
            try {
                String text = jsonObject.optString("text", "....");
                String colorStr = jsonObject.getString("color");
                int type = 1;
                BaseDanmaku item = this.mContext.mDanmakuFactory.createDanmaku(type, this.mContext);
                if (item != null) {
                    long time = jsonObject.optLong("time", 0L);
                    item.setTime(time * 1000L);
                    item.textSize = 25.0F * (this.mDispDensity - 0.6F);
                    int color = Color.parseColor(colorStr);
                    item.textColor = color;
                    item.textShadowColor = color <= -16777216 ? -1 : -16777216;
                    DanmakuUtils.fillText(item, text);
                    item.setTimer(this.mTimer);
                    danmakus.addItem(item);
                }
            } catch (JSONException var11) {
            }

            return danmakus;
        } else {
            return danmakus;
        }
    }
}
