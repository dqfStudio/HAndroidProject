//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
    boolean isMotion = false;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startMotion() {
        this.isMotion = true;
        this.requestFocus();
    }

    public void stopMotion() {
        this.isMotion = false;
        this.clearFocus();
    }

    public boolean isFocused() {
        return this.isMotion;
    }
}
