package com.example.a51425.mainuiframe.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.a51425.mainuiframe.R;
import com.example.a51425.mainuiframe.base.BaseActivity;
import com.example.a51425.mainuiframe.ui.adapter.MainAdapter;
import com.example.a51425.mainuiframe.ui.fragment.FindFragment;
import com.example.a51425.mainuiframe.ui.fragment.HomeFragment;
import com.example.a51425.mainuiframe.ui.fragment.MessageFragment;
import com.example.a51425.mainuiframe.ui.fragment.MyFragment;
import com.example.a51425.mainuiframe.ui.fragment.ServiceFragment;
import com.example.a51425.mainuiframe.utils.LogUtil;
import com.example.a51425.mainuiframe.ui.view.ViewPagerMain;
import com.example.a51425.mainuiframe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.viewpager_main)
    ViewPagerMain mViewpagerMain;
    @BindView(R.id.main_bottom_root)
    LinearLayout mMainBottomRoot;
    private List<Fragment> fragments;
    private long mExitTime = 0L;

    @Override
    protected View getContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        //设置不能左右滑动
        mViewpagerMain.setIsScroll(false);
        //viewPager预加载1页
        mViewpagerMain.setOffscreenPageLimit(0);
        mViewpagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeUI(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setBottomListener();
    }

    @Override
    protected void initData() {
        super.initData();
        initViewPager();
        //刚进来默认展示第1页的数据
        mViewpagerMain.setCurrentItem(0,false);
        changeUI(0);
        LogUtil.e(getClass().getName()+"_________initData");
    }

    /**
     * 初始化viewPager,此处正常应该展示对应的fragment，这里简单的加载了一个
     */
    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new ServiceFragment());
        fragments.add(new FindFragment());
        fragments.add(new MyFragment());
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mViewpagerMain.setAdapter(adapter);
    }


    /**
     * 监听底部按钮的切换状态，mMainBottomRoot为底部按钮的根布局，这样封装的话如果底部按钮的数量有变动
     * 这段代码不用发生什么变动，而且下一个项目用到的话直接复制粘贴
     */
    private void setBottomListener() {
        int childCount = mMainBottomRoot.getChildCount();
        for (int i = 0; i <childCount ; i++) {
            //获得子布局,为每个子view设置点击事件
            LinearLayout child = (LinearLayout) mMainBottomRoot.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //通过mMainBottomRoot来获得点击view对应的索引值
                    int indexOfChild = mMainBottomRoot.indexOfChild(view);
                    //根据索引来改变UI效果
                    changeUI(indexOfChild);
                    //viewPager也跳到对应的Fragment中
                    mViewpagerMain.setCurrentItem(indexOfChild, false);
                }
            });
        }
    }

    /**
     *  当viewPager切换的时候更改底部按钮的UI状态
     * @param position
     */
    private void changeUI(int position) {
        for (int i = 0; i <mMainBottomRoot.getChildCount() ; i++) {
            if (i==position){
                setEnable(mMainBottomRoot.getChildAt(i),false);
            }else{
                setEnable(mMainBottomRoot.getChildAt(i),true);
            }


        }
    }

    /**
     * 更改底部按钮UI状态,如果是view直接setEnable，如果是viewGroup递归遍历其中的view setEnable
     * @param childAt
     * @param b
     */
    private void setEnable(View childAt, boolean b) {

        childAt.setEnabled(b);
        if (childAt instanceof ViewGroup){
            for (int i = 0; i < ((ViewGroup) childAt).getChildCount(); i++) {
                setEnable(((ViewGroup) childAt).getChildAt(i),b);
            }
        }
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtil.e("监听 后退键");
            if (System.currentTimeMillis() - mExitTime < 2000) {
                moveTaskToBack(true);
                LogUtil.e("返回————");
                return true;
            }
            LogUtil.e("不返回————");
            mExitTime = System.currentTimeMillis();

            ToastUtil.showToast(MainActivity.this, "再按一次退出程序");
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}