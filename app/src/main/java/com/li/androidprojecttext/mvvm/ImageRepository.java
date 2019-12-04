package com.li.androidprojecttext.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.li.androidprojecttext.mvvm.model.BaseData;
import com.li.androidprojecttext.mvvm.model.ImageBean;
import com.li.androidprojecttext.mvvm.model.NetUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageRepository {

    private static final String TAG = "ImageRepository";

    private NetUtil netUtil = new NetUtil();
    private MutableLiveData<BaseData<ImageBean>> imageBean1= new MutableLiveData<>();
    private int idx = 1;

    //获取图片
    public MutableLiveData<BaseData<ImageBean>> getBingImage(){
        netUtil.getBingImage("js", idx, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ImageBean imageBean) {
                        imageBean1.setValue(new BaseData<>(imageBean, null));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return imageBean1;
    }

    //获取下一张图片
    public MutableLiveData<BaseData<ImageBean>> getNextImage(){
        if (idx > 6) {
            imageBean1.setValue(new BaseData<ImageBean>(null, "已经是最后一张了"));
            return imageBean1;
        }
        ++idx;
        return getBingImage();
    }

    //获取下一张图片
    public MutableLiveData<BaseData<ImageBean>> getPreviousImage(){
        if (idx < 1) {
            imageBean1.setValue(new BaseData<ImageBean>(null, "已经是第一张了"));
            return imageBean1;
        }
        --idx;
        return getBingImage();
    }
}
