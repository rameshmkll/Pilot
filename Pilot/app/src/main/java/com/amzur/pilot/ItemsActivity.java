package com.amzur.pilot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amzur.pilot.adapters.ItemsAdapter;
import com.amzur.pilot.customviews.EndlessRecyclerViewScrollListener;
import com.amzur.pilot.myretrofit.Listener;
import com.amzur.pilot.myretrofit.RetrofitService;
import com.amzur.pilot.pojos.ItemsPojo;
import com.amzur.pilot.pojos.JsonParserForAll;
import com.amzur.pilot.utilities.PreferenceData;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;


/**
 * Created by MRamesh on 06-05-2016.
 * This Activity displays items of a selected category.
 */
public class ItemsActivity extends AppCompatActivity {
    // to display items of selected category.
    RecyclerView rvItems;
    //adapter to set data to the recycler view
   ItemsAdapter itemsAdapter;
    //name of the selected category.
    String categoryName;
    SwipeRefreshLayout refreshLayout;
    int CAT_ID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_bottom_to_top,R.anim.fade_top_to_bottom);
        setContentView(R.layout.activity_items);
        Bundle b = getIntent().getExtras();
        categoryName = b.getString("category_name");
        CAT_ID= Integer.parseInt(b.getString("category_id"));
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        setTitle(categoryName);
        initializeRecyclerView(2);

    }

    /**
     * This method sets layout manager to the recyclerview,get items from the server and set items details to the recyclerview.
     * @param numberOfColumns number of columns to be displayed in grid layout.
     */
    private void initializeRecyclerView(int numberOfColumns) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ItemsActivity.this, numberOfColumns);
        rvItems.setLayoutManager(gridLayoutManager);
        rvItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //getItems();
            }
        });
        getItems();
        setAdapterToRecyclerView(numberOfColumns);
    }

    /**
     * Initialize views*/
    public void initViews()
    {
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

    }

    /**
     * This method sets data to the recycler view.
     * @param itemHeightIndicator
     */
    private void setAdapterToRecyclerView(int itemHeightIndicator) {

        itemsAdapter = new ItemsAdapter(ItemsActivity.this, categoryName,itemHeightIndicator);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setHasFixedSize(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,R.id.button,Menu.NONE,"Logout").setIcon(R.drawable.ic_power_settings_new_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, R.id.auto, Menu.NONE, "").setIcon(R.drawable.ic_border_all_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.button)
            PreferenceData.signOut(ItemsActivity.this);
        if(item.getItemId()==android.R.id.home)
            finish();
        if (item.getItemId() == R.id.auto) {
            initializeRecyclerView(1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * This method gets items data from the server.
     */
    public void getItems(){
        Call<ResponseBody> call=MyApplication.getSerivce().getItems(CAT_ID);
        call.enqueue(new Listener(new RetrofitService() {
            @Override
            public void onSuccess(String result, int pos, Throwable t) {
                if(pos==0){
                    Log.i("result",result);
                    ItemsPojo items=new JsonParserForAll().parseResponseToItems(result);
                    if(items!=null&&items.Items.size()>0)
                    itemsAdapter.addItems(items.Items);

                }
            }
        },"Loading",false,ItemsActivity.this));
    }

}
