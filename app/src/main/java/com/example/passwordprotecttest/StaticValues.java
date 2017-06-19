package com.example.passwordprotecttest;

/**
 * Created by 高信朋 on 2016/11/16.
 * 保存需要用到的静态值
 */

public class StaticValues {
    //msg表里记录密码信息的行的id
    public static String id = "";
    //是否设置了某一种锁
    public static  boolean hasLock = false;
    //是否设置了数字锁
    public static  boolean hasDLock = false;
    //是否设置了图案锁
    public static  boolean hasPLock = false;
    //是否设置了自定义密码锁
    public static  boolean hasDiyLock = false;
    //数字锁是多少
    public static String dLock = "";
    //图案锁是多少
    public static String pLock = "";
    //自定义密码锁是多少
    public static String diyLock = "";
    //锁屏密码使用哪个：0：不是用锁屏密码，1：使用数字密码，2：使用图案密码，3：使用自定义密码
    //默认为 0
    public static int useWhat = 0;
}
