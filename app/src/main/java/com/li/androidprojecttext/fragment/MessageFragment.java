package com.li.androidprojecttext.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.li.androidprojecttext.R;

/**
 * 消息Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class MessageFragment extends ViewPagerBaseFragment {

    private MessageFragment(){
    }

    /**
     * 静态内部类的方式实现单例
     * 一个ClassLoader下同一个类只会加载一次，保证了并发时不会得到不同的对象
     */
    public static MessageFragment newInstance(){
        return MessageFragmentHolder.messageFragment;
    }
    public static class MessageFragmentHolder{
        public static MessageFragment messageFragment = new MessageFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.message_fragment;
    }

    @Override
    protected void initView() {

    }
}
