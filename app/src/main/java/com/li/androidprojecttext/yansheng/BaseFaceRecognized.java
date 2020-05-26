package com.li.androidprojecttext.yansheng;

import android.graphics.Bitmap;

import com.li.androidprojecttext.activity.DoubleCameraActivity;
import com.li.androidprojecttext.model.IdentityCardEntity;

public abstract class BaseFaceRecognized {

    public abstract void onCreate(DoubleCameraActivity fragment);

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestroy();

    public abstract void updateIDCardPhotoBase64(String idPhotoBase64, String cardNumber);

    public abstract void updateIdentityCardEntity(IdentityCardEntity identityCardEntity);

    public abstract void continueCapture();

    protected FacecompareResult getInterface() {
        return comResult;
    }

    public void setFaceCompareResultImp(FacecompareResult result) {
        if (result != null) {
            comResult = result;
        }
    }

    public interface FacecompareResult {
        void faceCompareFailed(String failedReason);

        void faceCompareSuccess(float similarity, Bitmap img, String idCardNumber, long requestTime);
    }

    private FacecompareResult comResult = new FacecompareResult() {
        @Override
        public void faceCompareFailed(String failedReason) {
        }

        @Override
        public void faceCompareSuccess(float similarity, Bitmap img, String idCardNumber, long requestTime) {
        }
    };
}
