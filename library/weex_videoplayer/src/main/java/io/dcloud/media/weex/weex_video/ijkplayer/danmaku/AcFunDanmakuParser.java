//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.danmaku;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.android.JSONSource;
import master.flame.danmaku.danmaku.util.DanmakuUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AcFunDanmakuParser extends BaseDanmakuParser {
    public AcFunDanmakuParser() {
    }

    public Danmakus parse() {
        if (this.mDataSource != null && this.mDataSource instanceof JSONSource) {
            JSONSource jsonSource = (JSONSource)this.mDataSource;
            return this.doParse(jsonSource.data());
        } else {
            return new Danmakus();
        }
    }

    protected Danmakus doParse(JSONArray danmakuListData) {
        Danmakus danmakus = new Danmakus();
        if (danmakuListData != null && danmakuListData.length() != 0) {
            for(int j = 0; j < danmakuListData.length(); ++j) {
                try {
                    JSONArray danmaList = danmakuListData.getJSONArray(j);
                    if (danmaList.length() > 0) {
                        for(int i = 0; i < danmaList.length(); ++i) {
                            JSONObject danmakuArray = danmaList.getJSONObject(i);
                            if (danmakuArray != null) {
                                danmakus = this._parse(danmakuArray, danmakus);
                            }
                        }
                    }
                } catch (JSONException var7) {
                    var7.printStackTrace();
                }
            }

            return danmakus;
        } else {
            return danmakus;
        }
    }

    protected Danmakus _parse(JSONObject jsonObject, Danmakus danmakus) {
        if (danmakus == null) {
            danmakus = new Danmakus();
        }

        if (jsonObject != null && jsonObject.length() != 0) {
            try {
                String c = jsonObject.getString("c");
                String[] values = c.split(",");
                if (values.length > 0) {
                    int type = Integer.parseInt(values[2]);
                    if (type != 7) {
                        long time = (long)(Float.parseFloat(values[0]) * 1000.0F);
                        int color = Integer.parseInt(values[1]) | -16777216;
                        float textSize = Float.parseFloat(values[3]);
                        BaseDanmaku item = this.mContext.mDanmakuFactory.createDanmaku(type, this.mContext);
                        if (item != null) {
                            item.setTime(time);
                            item.textSize = textSize * (this.mDispDensity - 0.6F);
                            item.textColor = color;
                            item.textShadowColor = color <= -16777216 ? -1 : -16777216;
                            DanmakuUtils.fillText(item, jsonObject.optString("m", "...."));
                            item.setTimer(this.mTimer);
                            danmakus.addItem(item);
                        }
                    }
                }
            } catch (JSONException var12) {
            } catch (NumberFormatException var13) {
            }

            return danmakus;
        } else {
            return danmakus;
        }
    }
}
