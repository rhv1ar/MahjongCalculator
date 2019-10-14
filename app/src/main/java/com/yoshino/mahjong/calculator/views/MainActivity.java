package com.yoshino.mahjong.calculator.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.yoshino.mahjong.calculator.BorderedCircleTransform;
import com.yoshino.mahjong.calculator.FlickListener;
import com.yoshino.mahjong.calculator.R;
import com.yoshino.mahjong.calculator.databinding.ActivityMainBinding;
import com.yoshino.mahjong.calculator.models.ResultModel;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.utils.Tile;
import com.yoshino.mahjong.calculator.viewmodels.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements MainActivityNavigation {
  private static final String TAG = MainActivity.class.getSimpleName();
  private ConstraintLayout mRootConstraintLayout;
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;
  private SimpleTsumoResultFragment mSimpleTsumoResultFragment;
  private SimpleRonResultFragment mSimpleRonResultFragment;
  private DialogFragment mKanDialogFragment;

  private PlayerModel mPlayerModel;
  private ResultModel mTsumoResultModel, mRonResultModel;

  private MainViewModel mMainViewModel;


  private Vibrator mVibrator;

  private static final Tile[][] TILES = {
          {new Tile(21), new Tile(11), new Tile(1)},
          {new Tile(22), new Tile(12), new Tile(2)},
          {new Tile(23), new Tile(13), new Tile(3)},
          {new Tile(24), new Tile(14), new Tile(4)},
          {new Tile(25), new Tile(15), new Tile(5)},
          {new Tile(26), new Tile(16), new Tile(6)},
          {new Tile(27), new Tile(17), new Tile(7)},
          {new Tile(28), new Tile(18), new Tile(8)},
          {new Tile(29), new Tile(19), new Tile(9)},
          {new Tile(31), new Tile(33), new Tile(34), new Tile(32)},
          {new Tile(35), new Tile(36), new Tile(37)}
  };

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

    mPlayerModel = new PlayerModel();
    mTsumoResultModel = new ResultModel();
    mRonResultModel = new ResultModel();

    mMainViewModel = new MainViewModel(this, mPlayerModel, mTsumoResultModel, mRonResultModel);


    mKanDialogFragment = new KanDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable("playermodel", mPlayerModel);
    mKanDialogFragment.setArguments(bundle);

    // Initialize DataBinding
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setViewModel(mMainViewModel);

    getView();


    // init viewPager
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    mViewPager = findViewById(R.id.viewPager);
    mViewPager.setAdapter(mSectionsPagerAdapter);
        /*mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);*/

    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Oswald-Stencbab.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
    );
  }



  private void getView() {
    mRootConstraintLayout = findViewById(R.id.constraintLayout);

    findViewById(R.id.imageView_one).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_two).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_three).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_four).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_five).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_six).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_seven).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_eight).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_nine).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_wind).setOnTouchListener(new FlickListener(flickListener));
    findViewById(R.id.imageView_dragon).setOnTouchListener(new FlickListener(flickListener));

    BorderedCircleTransform transform = new BorderedCircleTransform(Color.GRAY, 1);
    Picasso.get().load(R.mipmap.p1).transform(transform).into((ImageView)findViewById(R.id.imageView_one));
    Picasso.get().load(R.mipmap.p2).transform(transform).into((ImageView)findViewById(R.id.imageView_two));
    Picasso.get().load(R.mipmap.p3).transform(transform).into((ImageView)findViewById(R.id.imageView_three));
    Picasso.get().load(R.mipmap.p4).transform(transform).into((ImageView)findViewById(R.id.imageView_four));
    Picasso.get().load(R.mipmap.p5).transform(transform).into((ImageView)findViewById(R.id.imageView_five));
    Picasso.get().load(R.mipmap.p6).transform(transform).into((ImageView)findViewById(R.id.imageView_six));
    Picasso.get().load(R.mipmap.p7).transform(transform).into((ImageView)findViewById(R.id.imageView_seven));
    Picasso.get().load(R.mipmap.p8).transform(transform).into((ImageView)findViewById(R.id.imageView_eight));
    Picasso.get().load(R.mipmap.p9).transform(transform).into((ImageView)findViewById(R.id.imageView_nine));

  }



  private final FlickListener.OnFlickListener flickListener = new FlickListener.OnFlickListener() {
    private ImageView mImageTop, mImageRight, mImageLeft;

    @Override
    public Tile[] onTouchDown(final View view) {
      Log.v(TAG, "onTouchDown");
      String tag = (String) view.getTag();
      int index = Integer.parseInt(tag) - 1;

      // show top image
      mImageTop = new ImageView(MainActivity.this);
      mImageTop.setImageResource(TILES[index][1].getImageId());
      ConstraintLayout.LayoutParams paramsTop = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      paramsTop.startToStart = view.getId();
      paramsTop.endToEnd = view.getId();
      paramsTop.bottomToTop = view.getId();
      mImageTop.setLayoutParams(paramsTop);
      mRootConstraintLayout.addView(mImageTop);

      // show right image
      mImageRight = new ImageView(MainActivity.this);
      mImageRight.setImageResource(TILES[index][2].getImageId());
      ConstraintLayout.LayoutParams paramsRight = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      paramsRight.startToEnd = view.getId();
      paramsRight.topToTop = view.getId();
      paramsRight.bottomToBottom = view.getId();
      mImageRight.setLayoutParams(paramsRight);
      mRootConstraintLayout.addView(mImageRight);

      // show left image
      if (TILES[index].length == 4) {
        mImageLeft = new ImageView(MainActivity.this);
        mImageLeft.setImageResource(TILES[index][3].getImageId());
        ConstraintLayout.LayoutParams paramsLeft = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLeft.endToStart = view.getId();
        paramsLeft.topToTop = view.getId();
        paramsLeft.bottomToBottom = view.getId();
        mImageLeft.setLayoutParams(paramsLeft);
        mRootConstraintLayout.addView(mImageLeft);
      }

      return TILES[index];
    }

    @Override
    public void onTouchUp(final Tile tile) {
      // remove right image
      if (mImageRight != null) {
        mRootConstraintLayout.removeView(mImageRight);
        mImageRight = null;
      }

      // remove top image
      if (mImageTop != null) {
        mRootConstraintLayout.removeView(mImageTop);
        mImageTop = null;
      }

      // remove left image
      if (mImageLeft != null) {
        mRootConstraintLayout.removeView(mImageLeft);
        mImageLeft = null;
      }

      if (tile == null) {
        return;
      }

      mMainViewModel.setTile(tile);

      // vibrate
      mVibrator.vibrate(20);
      //long pattern[] = {0, 40, 100, 40};
      //mVibrator.vibrate(pattern, -1);
    }
  };

  @Override
  public void showKanDialog() {
    mKanDialogFragment.show(getSupportFragmentManager(), "kandialog");
  }

  @Override
  public void updateResult() {
    mSimpleTsumoResultFragment.update();
    //mSimpleRonResultFragment.update();
  }

  private class SectionsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    SectionsPagerAdapter(final FragmentManager fm) {
      super(fm);
    }

    public void add(Fragment fragment) {
      mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
      Log.v(TAG, "getItem(" + position + ")");
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).

      switch (position) {
        case 0:
          mSimpleTsumoResultFragment = SimpleTsumoResultFragment.newInstance(mTsumoResultModel, mPlayerModel);
          add(mSimpleTsumoResultFragment);
          return mSimpleTsumoResultFragment;
        case 1:
          mSimpleRonResultFragment = SimpleRonResultFragment.newInstance(mRonResultModel, mPlayerModel);
          add(mSimpleRonResultFragment);
          return mSimpleRonResultFragment;
        default:
          return null;
      }

    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
      Fragment target = (Fragment) object;
      if (mFragments.contains(target)) {
        return POSITION_UNCHANGED;
      }

      return POSITION_NONE;
    }

  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }
}
