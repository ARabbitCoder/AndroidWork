/*
 * Copyright (C) 2018 CoorChice <icechen_@outlook.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Last modified 18-3-29 下午5:06
 */

package com.ayearn.main;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayearn.ui.animation.AnimationFactory;
import com.ayearn.ui.utils.ViewFactory;
import com.ayearn.ui.utils.ViewUtils;
import com.voole.utils.glide.ImageLoadUtil;
import com.voole.utils.screen.DensityUtil;
import com.voole.utils.stytle.StytleUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String myWeixinHead = "http://wx.qlogo.cn/mmhead/Q3auHgzwzM5YnooSNMTYK4uvb5I8Xwps0RJ4dgooLUdtFwMzzFssNQ/132";
    private ImageView left_animation_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StytleUtils.setStateBar(this);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_root);
        ViewUtils.setDrawerLayoutPullSize(MainActivity.this,drawerLayout,0.3f);
        navigationView = (NavigationView) findViewById(R.id.drawer_left_view);
        setDrawLayoutLeftView(navigationView);
    }

    /**
     * 设置抽屉布局左边的view
     * @param navigationView
     */
    private void setDrawLayoutLeftView(NavigationView navigationView) {
        //680 约等于 200÷0.3（平移动画百分比）避免平移过多
        int imageViewHeight = DensityUtil.getInstance().dip2px(this,680);
        left_animation_background = ViewFactory.createImageView(this, -1, imageViewHeight, null, ViewFactory.FRAMELAYOUT_TYPE, R.drawable.drawer_background);
        //将动画背景填在第一个位置
        navigationView.addView(left_animation_background, 0);
        //根据屏幕计算topmargin
        int viewMargin =DensityUtil.getInstance().dip2px(this,200);
        View view = ViewFactory.createView(this, -1, -1, new int[]{0, viewMargin, 0, 0}, 3);
        navigationView.addView(view, 1);


        ImageView left_head_imageview = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.left_header_image);
        //获取图片数据添加至左面头像
        ImageLoadUtil.getInstance().loadImageViewWithTrans(this,myWeixinHead,left_head_imageview,new CropCircleTransformation(this));
        //添加菜单选项
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.add("new");
        item.setIcon(R.drawable.ic_file_download_black_24dp);
        item.setCheckable(true);
        initListener();

    }


    private void initListener() {
        //拖动监听
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                /*if(left_animation_background != null) {
                    left_animation_background.clearAnimation();
                }*/
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (left_animation_background != null) {
                    left_animation_background.clearAnimation();
                    left_animation_background.startAnimation(AnimationFactory.createTranslateAnimationY(-0.3f, 6000, -1, 2));
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (left_animation_background != null) {
                    left_animation_background.clearAnimation();
                    left_animation_background.animate().cancel();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //菜单点击监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}
