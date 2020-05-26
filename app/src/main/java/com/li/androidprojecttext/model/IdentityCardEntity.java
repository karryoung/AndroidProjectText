package com.li.androidprojecttext.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class IdentityCardEntity implements Cloneable {

    private static List<String> nationList = Arrays.asList(
            "汉",
            "蒙古",
            "回",
            "藏",
            "维吾尔",
            "苗",
            "彝",
            "壮",
            "布依",
            "朝鲜",
            "满",
            "侗",
            "瑶",
            "白",
            "土家",
            "哈尼",
            "哈萨克",
            "傣",
            "黎",
            "傈僳",
            "佤",
            "畲",
            "高山",
            "拉祜",
            "水",
            "东乡",
            "纳西",
            "景颇",
            "柯尔克孜",
            "土",
            "达斡尔",
            "仫佬",
            "羌",
            "布朗",
            "撒拉",
            "毛南",
            "仡佬",
            "锡伯",
            "阿昌",
            "普米",
            "塔吉克",
            "怒",
            "乌孜别克",
            "俄罗斯",
            "鄂温克",
            "德昂",
            "保安",
            "裕固",
            "京",
            "塔塔尔",
            "独龙",
            "鄂伦春",
            "赫哲",
            "门巴",
            "基诺");

    private String strName;//姓名
    private String strSex;//性别
    private String strNationCode;//名族代码
    private String strNation;//民族
    private String strBirth;//出生年月
    private String strAddr;//地址
    private String strIdCode;//身份证号
    private String strIssue;//签发机关
    private String strBeginDate;//起始有效期
    private String strEndDate;//终止有效期
    private String encodeBitmapStr;//相片
    private String encodeBitmapForUploadStr;
    private String strUid;
    private String displayIDNumber;
    private int sex; // 1男 2女 0未知

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSex() {
        return strSex;
    }

    public void setStrSex(String strSex) {
        int sexInt = Integer.parseInt(strSex);
        sex = sexInt;
        if (sexInt == 1) {
            this.strSex = "男";
        } else if (sexInt == 0) {
            this.strSex = "未知";
        } else if (sexInt == 2) {
            this.strSex = "女";
        } else {
            this.strSex = "未指定";
        }
    }

    public void setSexCode(String strSex) {
        this.strSex = strSex;
    }

    public String getSexCode() {
        if ("男".equals(strSex)) {
            return "1";
        }
        if ("女".equals(strSex)) {
            return "2";
        }
        return "0";
    }

    public String getStrNation() {
        return strNation;
    }

    public String getStrBirth() {
        return strBirth;
    }

    public void setStrBirth(String strBirth) {
        strBirth = strBirth.substring(0, 4) + "/" + strBirth.substring(4, 6) + "/" + strBirth.substring(6, 8);
        this.strBirth = strBirth;
    }

    public String getStrAddr() {
        return strAddr;
    }

    public void setStrAddr(String strAddr) {
        this.strAddr = strAddr;
    }

    public String getStrIdCode() {
        return strIdCode;
    }

    public void setStrIdCode(String strIdCode) {
        this.strIdCode = strIdCode;
    }

    public String getStrIssue() {
        return strIssue;
    }

    public void setStrIssue(String strIssue) {
        this.strIssue = strIssue;
    }

    public String getStrBeginDate() {
        return strBeginDate;
    }

    public void setStrBeginDate(String strBeginDate) {
        this.strBeginDate = strBeginDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    public String getEncodeBitmapStr() {
        return encodeBitmapForUploadStr;
    }

    public String getOriginEncodeBitmapStr() {
        return encodeBitmapStr;
    }

    public void setEncodeBitmapStr(Bitmap bitmap) {
        encodeBitmapStr = getBase64EncodePhoto(bitmap, 100);
        encodeBitmapForUploadStr = getBase64EncodePhoto(bitmap, 50);
    }

    public String getStrNationCode() {
        return strNationCode;
    }

    public void setStrNationCode(String strNationCode) {
        this.strNationCode = strNationCode;
        int nationInt = Integer.parseInt(strNationCode);
        strNation = nationList.get(nationInt - 1);
    }

    public void setStrNation(String strNation) {
        this.strNation = strNation;
        int nationInt = nationList.indexOf(strNation);
        strNationCode = String.valueOf((nationInt + 1));
    }

    public String getDisplayIDNumber() {
        return strIdCode.substring(0, 6) + "********" + strIdCode.substring(14, 18);
    }

    private String getBase64EncodePhoto(Bitmap bitmap, int quality) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byte[] byteArray = stream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        }
        return null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof IdentityCardEntity) {
            IdentityCardEntity entity = (IdentityCardEntity) obj;
            if (strIdCode != null && strIdCode.equals(entity.getStrIdCode())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    public Bitmap getBitmap() {
        byte[] decodedString = Base64.decode(encodeBitmapStr, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String getStrUid() {
        return strUid;
    }

    public void setStrUid(String strUid) {
        this.strUid = strUid;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setEncodeBitmapStr(String encodeBitmapStr) {
        this.encodeBitmapStr = encodeBitmapStr;
    }

    public String getEncodeBitmapForUploadStr() {
        return encodeBitmapForUploadStr;
    }

    public void setEncodeBitmapForUploadStr(String encodeBitmapForUploadStr) {
        this.encodeBitmapForUploadStr = encodeBitmapForUploadStr;
    }
}
