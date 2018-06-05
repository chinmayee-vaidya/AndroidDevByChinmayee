package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.jokesfactory.JokesActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * Created by Chinmayee on 5/25/18.
 */

public class JokeFetchAsyncTask extends AsyncTask<Context, Void, String> {
    private MyApi myApiServ = null;
    private Context context;
    private boolean isNotTest;
    public JokeFetchAsyncTask(boolean isNotTest){
        this.isNotTest=isNotTest;
    }
    @Override
    protected String doInBackground(Context... contexts) {

        if(myApiServ == null){
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(),null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override                         public void initialize(AbstractGoogleClientRequest<?>
                                                                                         abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            myApiServ = builder.build();
        }
        context = contexts[0];
        try {
            return myApiServ.sayJokes().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        if(isNotTest){
            Intent intent = new Intent(context, JokesActivity.class);
            intent.putExtra("KEY_JOKE_PASS",s);
            context.startActivity(intent);
        }

    }
}
