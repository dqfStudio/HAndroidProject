//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import com.dcloud.android.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;

public class AndroidMediaController extends MediaController implements IMediaController {
    private ArrayList<View> mShowOnceArray = new ArrayList();

    public AndroidMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public AndroidMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        this.initView(context);
    }

    public AndroidMediaController(Context context) {
        super(context);
        this.initView(context);
    }

    private void initView(Context context) {
    }

    public void show() {
        super.show();
    }

    public void hide() {
        super.hide();
        Iterator var1 = this.mShowOnceArray.iterator();

        while(var1.hasNext()) {
            View view = (View)var1.next();
            view.setVisibility(8);
        }

        this.mShowOnceArray.clear();
    }

    public void showOnce(@NonNull View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(0);
        this.show();
    }
}
