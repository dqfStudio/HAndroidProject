

1》app发布版本代码只需改动config.gradle文件：/yhvideo-android/config.gradle

config.gradle：参数说明

     * @debug                 日志打印开关,线上建议关掉 开启：true 关闭：false
     * @versionName           app版本号 :app版本更新
     * @appletsVersionName    app小程序版本号 :app小程序版本更新
     * @developType           开发模式 :FORMAL=生产 OUTEST=外测 INTEST=内测
     * @applicationId         包名
     * @inTestUrl             内测地址 developType=INTEST 生效
     * @outTestUrl            外测地址 developType=OUTEST 生效
     * @formalUrl             生产地址 developType=FORMAL 生效 多个地址用逗号隔开
     * @secretKey             请求接口的密钥
     * @weiboUrl              微博地址拉去域名
     * @appName               app名
     * @appletsName           小程序名

2》发布版本logo跟启动图只需更改video文件：/yhvideo-android/app/src/video/res/drawable-xxhdpi

    替换logo :/yhvideo-android/app/src/video/res/drawable-xxhdpi/ic_logo
    替换启动页 :/yhvideo-android/app/src/video/res/drawable-xxhdpi/ic_splash

3》替换本地小程序文件
   yhvideo-android/app/src/main/assets/__UNI__A9E02E4.wgt
      替换__UNI__A9E02E4.wgt文件

4》打包签名文件：/yhvideo-android/key/key/lebo.jks

   打包生成的apk所在目录：/yhvideo-android/app/video/release

    签名文件密码：
    QWE123!@#

5》注：目录下有对应图片打包步骤









