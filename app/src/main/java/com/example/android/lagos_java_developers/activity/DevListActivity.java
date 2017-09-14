package com.example.android.lagos_java_developers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.lagos_java_developers.R;
import com.example.android.lagos_java_developers.adapter.Developers_Adapter;
import com.example.android.lagos_java_developers.model.Developer;
import com.example.android.lagos_java_developers.model.JSONResponse;
import com.example.android.lagos_java_developers.rest.ApiClient;
import com.example.android.lagos_java_developers.rest.GitHubService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevListActivity extends AppCompatActivity implements Developers_Adapter.ListItemClickListiner, SwipeRefreshLayout.OnRefreshListener {


    List<Developer> developers;
    GitHubService gitHubClient;
    RecyclerView developerRv;
    Developers_Adapter devAdapter;
    LinearLayoutManager  layoutManager;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItemPosition;
    int pageIndex =1;
    boolean isLoading;
    SwipeRefreshLayout swipeContainer;
    List<Developer> lagosJavaDev;
    LinearLayout loadMore;
    RelativeLayout noInternet;
    RelativeLayout loading;
    RelativeLayout emptyState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_list_dev);


        gitHubClient = ApiClient.getClient().create(GitHubService.class);

        // Creates an object of the recyclerView in the activity_developers_list.xml file
        developerRv = (RecyclerView) findViewById(R.id.rv_members);

        layoutManager = new LinearLayoutManager(getBaseContext());
        developerRv.setHasFixedSize(true);
        lagosJavaDev = new ArrayList<>();
        devAdapter = new Developers_Adapter(getApplicationContext() , lagosJavaDev, DevListActivity.this);

        developerRv.setLayoutManager(layoutManager);
        developerRv.setAdapter(devAdapter);


        loading = (RelativeLayout) findViewById(R.id.loading);

        //To display the message when there is nothing to display
        emptyState = (RelativeLayout) findViewById(R.id.emptyState);

        //To display the message for no internet connection
        noInternet = (RelativeLayout) findViewById(R.id.noInternetConnection);

        //spins to show that it is loading
        loadMore = (LinearLayout) findViewById(R.id.footerPB);

        //refreshes when pulled down
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


        swipeContainer.setOnRefreshListener(this);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


            getDevelopersList();




               developerRv.addOnScrollListener(new RecyclerView.OnScrollListener() {


                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                        super.onScrollStateChanged(recyclerView,newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        if (dy > 0){
                            //Total number of items on the screen
                            visibleItemCount = layoutManager .getChildCount();

                            //Total numbers of items in the list
                            totalItemCount =   layoutManager .getItemCount();

                            //Total number of items you have already seen
                            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();


                            if (isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {

                                if  (pageIndex < 8){


                                    loadMore.setVisibility(View.VISIBLE);
                                    pageIndex += 1;
                                    getDevelopersList();
                                    isLoading = false;
                                }

                                else{

                              isLoading =true;

                                    Snackbar.make(developerRv,
                                            "Nothing to show", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Cancel", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                                }


                            }
                        }

                    }});



            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dev_list, menu);

        MenuItem search = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);

        search(searchView);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                devAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }








    private void getDevelopersList(){
        developersList().enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                developers = response.body().getItem();
                if( developers !=null && ! developers.isEmpty()) {
                    swipeContainer.setVisibility(View.VISIBLE);
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    noInternet.setVisibility(View.GONE);
                    devAdapter.addAll(developers);
                    isLoading = true;

                }

            }


            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                if(loadMore.isShown()){
                    noInternet.setVisibility(View.GONE);
                    Snackbar.make(developerRv,
                            R.string.load_more_popup, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getDevelopersList();
                                }
                            }).show();
                }
                else {

                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    swipeContainer.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                }

            }
        });

        }


    private Call<JSONResponse> developersList() {

        return gitHubClient.getJsonResponse(pageIndex);
    }


    public void trigger(View view) {
        loading.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        getDevelopersList();

    }


    @Override
    public void onRefresh() {


        if (!loadMore.isShown()) {

            noInternet.setVisibility(View.GONE);
            swipeContainer.setRefreshing(true);

            pageIndex = 1;

            devAdapter.clear();
            getDevelopersList();

            //Disable the refreshing animation
            swipeContainer.setRefreshing(false);

        } else {
            Toast.makeText(DevListActivity.this, "Allow list to load!", Toast.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
        }

    }



    @Override
            public void onListItemClicked(int clickedItemIndex) {
                Intent numbersIntent = new Intent(DevListActivity.this, DevProfileActivity.class);
                String devUserName = devAdapter.getItemPosition().getDevName();
                String jsonLink = devAdapter.getItemPosition().getDevUrl();
                String htmlLink = devAdapter.getItemPosition().getdevHtmlUrl();
                numbersIntent.putExtra("image", devAdapter.getItemPosition().getDevImg());
                numbersIntent.putExtra("username", devUserName);
                numbersIntent.putExtra("devUrl", jsonLink);
                numbersIntent.putExtra("devHtmlUrl", htmlLink);
                startActivity(numbersIntent);
            }
}
