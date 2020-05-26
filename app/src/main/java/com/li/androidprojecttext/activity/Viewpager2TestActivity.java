package com.li.androidprojecttext.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.li.androidprojecttext.R;

/**
 * @CreateDate: 2020/4/9 10:00
 * @Description: Viewpager2的测试类
 * @Author: 李想
 */
public class Viewpager2TestActivity extends BaseActivity {
    ViewPager2 viewpager2;
    private int onPageSelectPosition = 0;//用于判断滑动方向
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager2_fragment);
        viewpager2 = findViewById(R.id.viewpager2);
        viewpager2.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Viewpager2TestActivity.this).inflate(R.layout.viewpager2_item_layout, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.textView.setText("item"+position);
            }

            @Override
            public int getItemCount() {
                return 4;
            }
        });

        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                onPageSelectPosition = position;
                Log.e("lallla", "---onPageSelectPosition-"+onPageSelectPosition+"--position----"+position
                +"---positionOffset--"+positionOffset+"----positionOffsetPixels---"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (onPageSelectPosition == 0) {
                    if (onPageSelectPosition == position) {
                        //左拉
                        viewpager2.setCurrentItem(3, false);
                    } else {
                        //右拉
                    }
                } else if (onPageSelectPosition == 3) {
                    if (onPageSelectPosition == position) {
                        //左拉
                        viewpager2.setCurrentItem(0, false);
                    } else {
                        //右拉
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
