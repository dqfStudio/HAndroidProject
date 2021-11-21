package com.funfish.lebo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Base64;

import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtils {


    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) x.app().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String[] getYumingTxt(Context context) {
        try {
            FileInputStream inStream = context.openFileInput("yuming.txt");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            stream.close();
            inStream.close();

            String[] yumingList = stream.toString().split("<>");
            System.out.println("*****读取域名配置文件成功*****" + stream.toString() + "<>" + yumingList.length);
            return yumingList;

        } catch (FileNotFoundException e) {

            return null;
        } catch (IOException e) {
            return null;
        }
    }

    //获取渠道信息并解析
    public static String readApk(String path) {
        byte[] bytes = null;
        try {
            File file = new File(path);
            RandomAccessFile accessFile = new RandomAccessFile(file, "r");
            long index = accessFile.length();

            // 文件最后两个字节代表了comment的长度
            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            int contentLength = bytes2Short(bytes, 0);

            // 获取comment信息
            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            return new String(bytes, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static short bytes2Short(byte[] bytes, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes[offset]);
        buffer.put(bytes[offset + 1]);
        return buffer.getShort(0);
    }

    //正则解析渠道信息
    public static String getTokens(String data) {
        StringBuffer sb = new StringBuffer();
        //编译正则字符串
        String regex = "\\{\"channel_id([^}]*)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            sb.append(matcher.group() + "\r\n");
        }
        String test = sb.toString();
        return test;
    }

    //    2021-05-27 20:43:17.495 28874-28874/com.lyfun.lyfish.lebo I/System.out: BB正则解析域名=========================www.cxsdds.cn
//2021-05-27 20:43:17.495 28874-28874/com.lyfun.lyfish.lebo I/System.out: BB正则解析域名=========================www.meiju13.com
//2021-05-27 20:43:17.496 28874-28874/com.lyfun.lyfish.lebo I/System.out: BB正则解析域名=========================www.99gendiao.com
//2021-05-27 20:43:17.496 28874-28874/com.lyfun.lyfish.lebo I/System.out: BB正则解析域名=========================www.end3389.xyz
    public static ArrayList getToken(String data) {
        System.out.println("=========================正则解析渠道信息=========================" + data.length());
        ArrayList arr = new ArrayList();
        StringBuffer sb = new StringBuffer();
        //编译正则字符串
        String regex = "1010101010(.*?)1010101010";//<html lang="zh-cn"><head>  叽里呱啦&gt;(.*?)&lt;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            sb.append(matcher.group() + "\r\n");
//            System.out.println("AA正则解析域名========================="+matcher.group(1));
            String str = new String(Base64.decode(matcher.group(1), Base64.DEFAULT));
            System.out.println("BB正则解析域名=========================" + str);
            arr.add(str);
        }
        return arr;
    }

    public static boolean isGengXin(String my_version_id, String new_version_id) {
        Boolean bo = false;
        System.out.println("版本号判断=========================" + my_version_id + "<>" + new_version_id);

        String[] myStr = my_version_id.toString().split("[.]");
        String[] newStr = new_version_id.toString().split("[.]");

        for (int i = 0; i < 3; i++) {
            int myid = Integer.parseInt(myStr[i]);
            int newid = Integer.parseInt(newStr[i]);
            System.out.println("=========================" + myid + "<>" + newid);
            if (myid >= newid) {
                bo = false;
            } else {
                bo = true;
                break;
            }
        }
        return bo;
    }

    public static boolean isGengXin2(String my_version_id, String new_version_id) {

       int my_version = Integer.parseInt(my_version_id.replaceAll("[.]",""));
       int new_version = Integer.parseInt(new_version_id.replaceAll("[.]",""));
        System.out.println("版本号判断=========================2=" + my_version + "<>" + new_version);

        if (my_version<new_version){
           return  true;
       }
       return  false;
    }





    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
//            HnLogUtils.e("删除目录失败：" + filePath + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
//                flag = deleteSingleFile(file.getAbsolutePath());
                file.delete();

                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
//            Logutils.e("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
//            Logutils.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
//            Logutils.e("删除目录：" + filePath + "失败！");
            return false;
        }
    }

    public static void installApk(File file, String fileProvider, Context context) {
        ApplicationInfo info = x.app().getApplicationInfo();
        Uri apkUri;
        if (info.targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
            apkUri = FileProvider.getUriForFile(context, fileProvider, file);
        } else {
            apkUri = Uri.fromFile(file);
        }

//        ACTION_INSTALL_PACKAGE
//        Intent intent = new Intent(Intent.ACTION_VIEW);
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void creatYumingConfig(ArrayList ymlisty) {
        String FILENAME = "yuming.txt";

        System.out.println("*****配置域名文件写入成功*****" + ymlisty.size());
        FileOutputStream fos = null;
        try {

            String str = "";
            for (int i = 0; i < ymlisty.size(); i++) {
                str += ymlisty.get(i) + "<>";

            }

            fos = x.app().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();
            System.out.println("*****配置域名文件写入成功*****" + str);

        } catch (FileNotFoundException e) {
            System.out.println("*****配置域名文件写入失败*****");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("*****配置域名文件写入失败*****");
            e.printStackTrace();
        }
    }

    /**
     * 将asset文件写入缓存
     */
    public static String copyAssetAndWrite(Context context,String fileName){
        try {
            File cacheDir=context.getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return null;
                }
            }else {
                outFile.delete();
            }
            InputStream is=context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getSkinMD5(File file) {
        FileInputStream fis = null;
        BigInteger bi = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            byte[] digest = MD5.digest();
            bi = new BigInteger(1, digest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bi.toString(16);
    }

}
