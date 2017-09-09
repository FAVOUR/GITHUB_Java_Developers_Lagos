package com.example.android.lagos_java_developers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.lagos_java_developers.R;
import com.example.android.lagos_java_developers.adapter.Developers_Adapter;
import com.example.android.lagos_java_developers.model.Developer;
import com.example.android.lagos_java_developers.model.JSONResponse;
import com.example.android.lagos_java_developers.rest.ApiClient;
import com.example.android.lagos_java_developers.rest.GitHubClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevListActivity extends AppCompatActivity implements Developers_Adapter.ListItemClickListiner  {

    List<Developer> developers;
    GitHubClient gitHubClient;
    RecyclerView developerRv;
    Developers_Adapter devAdapter;
    LinearLayoutManager  layoutManager;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItemPosition;
    int previousTotal = 0;
    int pageIndex =1;
    boolean isLoading;
    List<Developer> lagosJavaDev;
    LinearLayout loadMore;
    RelativeLayout noInternet;
    RelativeLayout loading;
    RelativeLayout emptyState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_list_dev);

        gitHubClient = ApiClient.getClient().create(GitHubClient.class);

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



            getDevelopersList();




               developerRv.addOnScrollListener(new RecyclerView.OnScrollListener() {


                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                        super.onScrollStateChanged(recyclerView,newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);


                        if (dy > 0){
                            //Total number of items on the screen
                            visibleItemCount = layoutManager .getChildCount();

                            //Total numbers of items in the list
                            totalItemCount =   layoutManager .getItemCount();

                            //Total number of items you have already seen
                            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                            if (isLoading){
                                if (totalItemCount >  previousTotal){
                                    isLoading = false ;
                                    previousTotal = totalItemCount;
                                }

                            }
                            if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {

                                if  (pageIndex < 8){

                                    pageIndex += 1;
                                    loadMore.setVisibility(View.VISIBLE);
                                    getDevelopersList();

                                    isLoading =true;
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

    private void getDevelopersList(){
        developersList().enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                developers = response.body().getItem();
                if( developers !=null && ! developers.isEmpty()) {

                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    noInternet.setVisibility(View.GONE);
                    devAdapter.addAll(developers);
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
                    noInternet.setVisibility(View.VISIBLE);
                }

            }
        });

        }

            private  Call<JSONResponse> developersList(){

               return gitHubClient.getJsonResponse(pageIndex);
           }


    public void trigger(View view) {
        loading.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        getDevelopersList();

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
