//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.media;

import android.content.res.AssetFileDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

public class AssetsDataSourceProvider implements IMediaDataSource {
    private AssetFileDescriptor mDescriptor;
    private byte[] mMediaBytes;

    public AssetsDataSourceProvider(AssetFileDescriptor descriptor) {
        this.mDescriptor = descriptor;
    }

    public int readAt(long position, byte[] buffer, int offset, int size) {
        if (position + 1L >= (long)this.mMediaBytes.length) {
            return -1;
        } else {
            int length;
            if (position + (long)size < (long)this.mMediaBytes.length) {
                length = size;
            } else {
                length = (int)((long)this.mMediaBytes.length - position);
                if (length > buffer.length) {
                    length = buffer.length;
                }

                --length;
            }

            System.arraycopy(this.mMediaBytes, (int)position, buffer, offset, length);
            return length;
        }
    }

    public long getSize() throws IOException {
        long length = this.mDescriptor.getLength();
        if (this.mMediaBytes == null) {
            InputStream inputStream = this.mDescriptor.createInputStream();
            this.mMediaBytes = this.readBytes(inputStream);
        }

        return length;
    }

    public void close() throws IOException {
        if (this.mDescriptor != null) {
            this.mDescriptor.close();
        }

        this.mDescriptor = null;
        this.mMediaBytes = null;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}
