package com.facerecognition.dualdemoVL;

import android.os.Environment;
import android.util.Log;

import com.am.fras.License;
import com.am.fras.LicenseError;

import java.io.File;

public class LicenseUtils {
    public final static String accountFace = "eyecool3_dw";//授权账号
    public final static String accountPasswordFace = "123abc456";//授权账号密码
    final static String mLicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dualdemoVL/license.dat";

    public static int verifyLicense(){
        File f = new File(mLicPath);
        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }
        return License.verifyLicense(mLicPath);
    }

    public static void requestLicenseOnThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestLicense();
            }
        }).start();
    }

    public static int requestLicense(){
        File f = new File(mLicPath);
        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }
        int resultFlag = LicenseError.AM_E_SUCCESS;
        if (License.verifyLicense(mLicPath) != LicenseError.AM_E_SUCCESS) {
            License.removeLicense(mLicPath);
            resultFlag = License.requestLicense(mLicPath, accountFace, accountPasswordFace);
        }
        Log.i("yanshen","requestLicense result:" + resultFlag + "," + LicenseError.ErrnoStr(resultFlag));
        return resultFlag;
    }

    public static int requestLicense(String name,String key){
        File f = new File(mLicPath);
        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }

        if (License.verifyLicense(mLicPath) != LicenseError.AM_E_SUCCESS) {
            License.removeLicense(mLicPath);
            int flag = License.requestLicense(mLicPath, name, key);
            return flag;
        }
        return LicenseError.AM_E_SUCCESS;
    }

}
