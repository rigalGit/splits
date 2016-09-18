package com.shaktimaan.split;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Created by jitendra.gangwar on 12/09/16.
 */
public class SplitWiseApi extends DefaultApi10a {
    protected SplitWiseApi(){

    }

    public static SplitWiseApi instance(){
        return InstanceHolder.INSTANCE;
    }
    @Override
    public String getRequestTokenEndpoint() {
        return "https://secure.splitwise.com/api/v3.0/get_request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://secure.splitwise.com/api/v3.0/get_access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return "https://secure.splitwise.com/authorize?oauth_token=" + requestToken.getToken();
    }

    private static class InstanceHolder{
        private static SplitWiseApi INSTANCE = new SplitWiseApi();
        private InstanceHolder(){}
    }
}
