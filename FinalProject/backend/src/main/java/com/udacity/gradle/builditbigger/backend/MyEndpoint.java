package com.udacity.gradle.builditbigger.backend;

import com.example.joketellerlib.myClass;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;


/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayJokes", httpMethod = ApiMethod.HttpMethod.POST)
    public MyBean  sayJokes(){
        MyBean response = new MyBean();
        myClass obj = new myClass();
        response.setData(obj.retJoke());
        return response;
    }

}
