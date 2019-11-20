package com.li.androidprojecttext.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.li.androidprojecttext.R;

/**
 * 个人中心Fragment
 * Created by okkuaixiu on 2017/3/16.
 */

public class MyCenterFragment extends ViewPagerBaseFragment {

    /**
     * 懒加载模式下的单例
     *
     */
    private static MyCenterFragment myCenterFragment;

    private MyCenterFragment() {
    }
    /**
     * 添加synchronized是线程安全的，如果在多线程下频繁获取会造成性能问题
     * 去掉synchronized后，在多线程访问时会出现操作不是原子操作
     * （1.分配内存空间
     *   2.初始化对象
     *   3.将对象指向分配好的地址空间（执行完之后就不再是null了）
     *   其中第2，3步在一些编译器中为了优化单线程中的执行性能是可以重排的。重排之后就是这样的：
     *   1.分配内存空间
     *   2.将对象指向分配好的地址空间（执行完之后就不再是null了）
     *   3.初始化对象
     *   所以会出现当A线程在执行myCenterFragment==null后，myCenterFragment已经指向了某一内存，此时myCenterFragment对象并未初始化
     *   此时B线程再去访问时就会跳过myCenterFragment==null，那么B线程获取到的对象就是空对象）
     */
    public synchronized static MyCenterFragment newInstance(){
        if (myCenterFragment == null) {
            myCenterFragment = new MyCenterFragment();
        }
        return myCenterFragment;
    }

    /**
     * 使用枚举实现单例
     * 支持序列化，线程安全，只会装载一次
     * 单例的使用最好使用枚举实现，避免了被破坏
     * （破坏单例可以使用以下方式：1.使用反射，反射会调用构造方法获取对象。可以通过
     * private SingletonObject1(){    if (instance !=null){        throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");    }}
     * 这种方式可以避免生成新的对象。
     * 2.如果单例类实现了序列化接口Serializable, 就可以通过反序列化破坏单例，
     * 所以我们可以不实现序列化接口,如果非得实现序列化接口，可以重写反序列化方法readResolve(), 反序列化时直接返回相关单例对象。
     *   public Object readResolve() throws ObjectStreamException {        return instance;    }）
     */
    public enum MyCenterFragmentHolder{
        INSTANCE;
        private MyCenterFragment centerFragment;
        MyCenterFragmentHolder(){
            centerFragment = new MyCenterFragment();
        }
        private MyCenterFragment getInstance(){
            return centerFragment;
        }
    }
    public static MyCenterFragment getInstance(){
        return MyCenterFragmentHolder.INSTANCE.getInstance();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.my_center_fragment;
    }

    @Override
    protected void initView() {

    }
}
