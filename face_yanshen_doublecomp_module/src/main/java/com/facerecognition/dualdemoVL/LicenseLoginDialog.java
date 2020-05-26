package com.facerecognition.dualdemoVL;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.am.fras.License;
import com.am.fras.LicenseError;

/**
 *
 * @Description 授权弹框
 */
public class LicenseLoginDialog extends Dialog {

    private MainActivity mActivity;
    private EditText inputUserNameEt, inputPassWdEt;
    private Button mCommitBtn, mCancelBtn;
    private String mLicPath;

    public LicenseLoginDialog(Activity activity, String licPath, int theme) {
        super(activity, theme);
        mActivity = (MainActivity) activity;
        mLicPath = licPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_license_login);
        this.setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        inputUserNameEt = findViewById(R.id.input_username_et);
        inputPassWdEt = findViewById(R.id.input_passwd_et);
        mCommitBtn = findViewById(R.id.commit_btn);
        mCommitBtn.setOnClickListener(mClickListener);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(mClickListener);
    }

    private android.view.View.OnClickListener mClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
//            switch (v.getId()) {
//                case R.id.commit_btn:
//                    //获取输入信息
//                    String userName = inputUserNameEt.getText().toString().trim();
//                    String passWord = inputPassWdEt.getText().toString().trim();
//                    if (TextUtils.isEmpty(userName)) {
//                        Toast.makeText(mActivity, "用户名为空", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    if (TextUtils.isEmpty(passWord)) {
//                        Toast.makeText(mActivity, "密码为空", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    //获取license信息
//                    int result = LicenseError.AM_E_INTERNAL;//默认无license
//                    while (true) {
//                        result = License.requestLicense(mLicPath, userName, passWord);
//                        if (result == LicenseError.AM_E_LICENSE_BAD) {
//                            continue;
//                        }
//                        break;
//                    }
//                    //授权成功
//                    if (result == LicenseError.AM_E_SUCCESS) {
//                        Toast.makeText(mActivity, "授权成功", Toast.LENGTH_LONG).show();
//                        mActivity.initEngine();
//                        dismiss();
//                        return;
//                    }
//                    //授权失败
//                    if (result == LicenseError.AM_E_NETWORK_ERROR) {
//                        Toast.makeText(mActivity, "授权失败,网络连接异常", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    License.removeLicense(mLicPath);
//                    Toast.makeText(mActivity, "未授权", Toast.LENGTH_LONG).show();
//                    break;
//                case R.id.cancel_btn:
//                    dismiss();
//                    break;
//                default:
//                    break;
//            }
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        if (License.verifyLicense(mLicPath) != LicenseError.AM_E_SUCCESS) {
            mActivity.finish();
        }
    }
}
