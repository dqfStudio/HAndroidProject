//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.danmaku;

import android.util.Log;
import io.dcloud.media.weex.weex_video.ijkplayer.media.IjkPlayerView;
import master.flame.danmaku.danmaku.model.AbsDanmakuSync;

/** @deprecated */
@Deprecated
public class VideoDanmakuSync extends AbsDanmakuSync {
    private final IjkPlayerView mPlayerView;

    public VideoDanmakuSync(IjkPlayerView playerView) {
        this.mPlayerView = playerView;
    }

    public long getUptimeMillis() {
        if (this.mPlayerView != null) {
            Log.i("VideoDanmakuSync", "" + this.mPlayerView.getCurPosition());
            return (long)this.mPlayerView.getCurPosition();
        } else {
            return -1L;
        }
    }

    public int getSyncState() {
        if (this.mPlayerView.isPlaying()) {
            Log.e("VideoDanmakuSync", "SYNC_STATE_PLAYING");
            return 2;
        } else {
            Log.e("VideoDanmakuSync", "SYNC_STATE_HALT");
            return 1;
        }
    }

    public boolean isSyncPlayingState() {
        return true;
    }
}
