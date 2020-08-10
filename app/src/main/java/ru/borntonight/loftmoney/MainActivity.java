package ru.borntonight.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import ru.borntonight.loftmoney.item.ExpensesFragment;
import ru.borntonight.loftmoney.item.IncomesFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        tabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expenses);
        tabLayout.getTabAt(1).setText(R.string.incomes);

        FloatingActionButton buttonAdd = findViewById(R.id.floatingActionButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddItem = new Intent(getApplicationContext(), AddItemActivity.class);
                if (viewPager.getCurrentItem() == 0) {
                    intentAddItem.putExtra("type", "expense");
                } else {
                    intentAddItem.putExtra("type", "income");
                }
                startActivityForResult(intentAddItem, 1);
            }
        });
    }

    public static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ExpensesFragment();
            } else if (position == 1) {
                return new IncomesFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreyBlue));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreyBlue));
    }

    @Override
    public void onActionModeFinished(final ActionMode mode) {
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBar));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBar));
        super.onActionModeFinished(mode);
    }
}