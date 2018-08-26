package com.codezclub.ivines.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codezclub.ivines.Adapter.ViewPagerAdapter;
import com.codezclub.ivines.R;

/**
 * Created by Mj 2 on 18-Jun-17.
 */

public class HomeFragment extends Fragment {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    BbkivinesFragment bbkivinesFragment;
    CarryMinatiFragment carryMinatiFragment;
    HarshBeniwalFragment harshBeniwalFragment;
    AshishFragment ashishFragment;
    AasheanFragment aasheanFragment;
    RealShitFragment realShitFragment;
    //RishsomeFragment rishsomeFragment;
    //AmitFragment amitFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.home_fragment,container,false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);


        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        return view;
    }

/*    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }*/


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        bbkivinesFragment = new BbkivinesFragment();
        carryMinatiFragment = new CarryMinatiFragment();
        ashishFragment = new AshishFragment();
        harshBeniwalFragment = new HarshBeniwalFragment();
        aasheanFragment = new AasheanFragment();
        realShitFragment = new RealShitFragment();
        //rishsomeFragment = new RishsomeFragment();
        //amitFragment = new AmitFragment();

        adapter.addFragment(bbkivinesFragment,"BBKiVines");
        adapter.addFragment(carryMinatiFragment,"Carry Minati");
        adapter.addFragment(ashishFragment,"Ashish");
        adapter.addFragment(harshBeniwalFragment,"Harsh Beniwal");
        adapter.addFragment(aasheanFragment,"Aashqean");
        adapter.addFragment(realShitFragment,"Real Shit");
        //adapter.addFragment(rishsomeFragment,"Rishsome");
        //adapter.addFragment(amitFragment,"Amit Bhadana");

        viewPager.setAdapter(adapter);
    }

   /* @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if( keyCode == KeyEvent.KEYCODE_BACK )
                    {
                        return true;
                    }
                    return false;
            }
        });
    }*/
}
