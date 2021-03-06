//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

public class FileMediaDataSource implements IMediaDataSource {
    private RandomAccessFile mFile;
    private long mFileSize;

    public FileMediaDataSource(File file) throws IOException {
        this.mFile = new RandomAccessFile(file, "r");
        this.mFileSize = this.mFile.length();
    }

    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (this.mFile.getFilePointer() != position) {
            this.mFile.seek(position);
        }

        return size == 0 ? 0 : this.mFile.read(buffer, 0, size);
    }

    public long getSize() throws IOException {
        return this.mFileSize;
    }

    public void close() throws IOException {
        this.mFileSize = 0L;
        this.mFile.close();
        this.mFile = null;
    }
}
