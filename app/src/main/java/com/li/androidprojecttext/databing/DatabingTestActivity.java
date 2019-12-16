package com.li.androidprojecttext.databing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;

import com.li.androidprojecttext.BR;
import com.li.androidprojecttext.R;
import com.li.androidprojecttext.databinding.DatabingTestActivityBinding;

/**
 * databing练习页面
 */
public class DatabingTestActivity extends AppCompatActivity {

    private static final String TAG = "DataBindingActivity";
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabingTestActivityBinding databingActivity = DataBindingUtil.setContentView(this, R.layout.databing_test_activity);
        //添加数据源
        user = new User("CHANG", "12", "HANGZHOU");
        ObservableUser observableUser = new ObservableUser("张三", "家里蹲", "北京");

        databingActivity.setUser(user);
        databingActivity.setObservableuser(observableUser);
        databingActivity.tvSchool.setText("技术学院");
        databingActivity.setEventListener(new EventListener());

        Button bt1 = findViewById(R.id.bt1);
        Button bt2 = findViewById(R.id.bt2);
        Button bt3 = findViewById(R.id.bt3);
        Button bt4 = findViewById(R.id.bt4);

        /**
         *
         * 实现数据变化自动驱动 UI 刷新的方式有三种：BaseObservable、ObservableField、ObservableCollection
         */
        //1.通过BaseObservable更新
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName("CHNAHE" + (int) (1 + Math.random() * (10 - 1 + 1)));
                user.setSchool("杭州" + (int) (1 + Math.random() * (10 - 1 + 1)));
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setAge(String.valueOf((int) (1 + Math.random() * (10 - 1 + 1))));
                user.setSchool("北京" + (int) (1 + Math.random() * (10 - 1 + 1)));
            }
        });
        user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                //这个监听是针对notifyPropertyChanged这个刷新才有小
                if (propertyId == BR.age) {
                    Log.d(TAG, "onPropertyChanged: " + "更新了age");
                } else if (propertyId == BR.name) {
                    Log.d(TAG, "onPropertyChanged: " + "更新了name");
                } else if (propertyId == BR.school) {
                    Log.d(TAG, "onPropertyChanged: " + "更新了school");
                }
            }
        });

        //2.通过ObservableField更新
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observableUser.getCity().set("青岛" + (int) (1 + Math.random() * (10 - 1 + 1)));
                observableUser.getName().set("拉拉" + (int) (1 + Math.random() * (10 - 1 + 1)));
            }
        });

        //通过ObservableCollection更新(包括list与map)
        ObservableMap<String, String> map = new ObservableArrayMap<>();
        map.put("泡泡", "上海");
        map.put("安安", "北京");
        databingActivity.setMap(map);
        databingActivity.setKey("泡泡");

        ObservableList<String> list = new ObservableArrayList<>();
        list.add("西藏");
        list.add("南京");
        databingActivity.setList(list);
        databingActivity.setIndex(0);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("泡泡", "杭州" + (int) (1 + Math.random() * (10 - 1 + 1)));
                list.set(0, "河南"+ (int) (1 + Math.random() * (10 - 1 + 1)));
            }
        });
    }

    /**
     * databinding事件绑定
     */
    public class EventListener {
        public void onUserAgeClick(View view) {
            user.setAge(String.valueOf((int) (1 + Math.random() * (10 - 1 + 1))));
            Toast.makeText(DatabingTestActivity.this, "点击了Button并给age赋新值", Toast.LENGTH_SHORT).show();
        }
    }
}
