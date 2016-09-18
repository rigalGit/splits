/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.shaktimaan.split;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;


import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * A sample application that demonstrates how the Google OAuth2 library can be used to authenticate
 * against Daily Motion.
 *
 * @author Ravi Mistry
 */
public class DailyMotionSample {

  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/dailymotion_sample");

  /**
   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
   * globally shared instance across your application.
   */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** OAuth 2 scope. */
  private static final String SCOPE = "read";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private static final String TOKEN_SERVER_URL = "https://api.dailymotion.com/oauth/token";
  private static final String AUTHORIZATION_SERVER_URL =
      "https://api.dailymotion.com/oauth/authorize";
    private static OAuth1AccessToken OAuth1AccessTokenStored = new OAuth1AccessToken("DOfShghL0mm7fDpcUAXwBDHJ8o3PCVDrFXmLydyl","hoOvsYTUDl9esEoEueFOu0ht8WjUQE8Arx3QCUPF");

    /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    OAuth2ClientCredentials.errorIfNotSpecified();
    // set up authorization code flow
    AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
        .authorizationHeaderAccessMethod(),
        HTTP_TRANSPORT,
        JSON_FACTORY,
        new GenericUrl(TOKEN_SERVER_URL),
        new ClientParametersAuthentication(
            OAuth2ClientCredentials.API_KEY, OAuth2ClientCredentials.API_SECRET),
        OAuth2ClientCredentials.API_KEY,
        AUTHORIZATION_SERVER_URL).setScopes(Arrays.asList(SCOPE))
        .setDataStoreFactory(DATA_STORE_FACTORY).build();
    // authorize
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(
        OAuth2ClientCredentials.DOMAIN).setPort(OAuth2ClientCredentials.PORT).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  private static void run(HttpRequestFactory requestFactory) throws IOException {
    DailyMotionUrl url = new DailyMotionUrl("https://api.dailymotion.com/videos/favorites");
    url.setFields("id,tags,title,url");

    HttpRequest request = requestFactory.buildGetRequest(url);
    VideoFeed videoFeed = request.execute().parseAs(VideoFeed.class);
    if (videoFeed.list.isEmpty()) {
      System.out.println("No favorite videos found.");
    } else {
      if (videoFeed.hasMore) {
        System.out.print("First ");
      }
      System.out.println(videoFeed.list.size() + " favorite videos found:");
      for (Video video : videoFeed.list) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("ID: " + video.id);
        System.out.println("Title: " + video.title);
        System.out.println("Tags: " + video.tags);
        System.out.println("URL: " + video.url);
      }
    }
  }

  public static void main(String[] args) {
    try {
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
      final Credential credential = authorize();
      HttpRequestFactory requestFactory =
          HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
              @Override
              public void initialize(HttpRequest request) throws IOException {
                  credential.initialize(request);
                  request.setParser(new JsonObjectParser(JSON_FACTORY));
              }
          });
//      run(requestFactory);
        OAuth10aService service = getSplitWiseOAuth10aService();

//        OAuth1AccessToken accessToken = getOAuth1AccessToken(service);
        OAuth1AccessToken accessToken = getOAuth1AccessTokenStored();
        System.out.println("Get Access token " + accessToken );


        String url = "https://secure.splitwise.com/api/v3.0/get_expenses?visible=true&group_id=747137&limit=25";
        sendRequest(service, accessToken, url);


        // Success!
      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }

    private static void sendRequest(OAuth10aService service, OAuth1AccessToken accessToken, String url) throws IOException {
        OAuthRequest authRequest = new OAuthRequest(Verb.GET, url,service);
        service.signRequest(accessToken,authRequest);
        Response send = authRequest.send();
        System.out.println("body ::" + send.getBody());
    }

    private static OAuth1AccessToken getOAuth1AccessToken(OAuth10aService service) throws IOException {
        System.out.println("Trying to get request token ");
        OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println(" Request token :: "+requestToken);

        String authorizationUrl = service.getAuthorizationUrl(requestToken);

        System.out.println("authorizationUrl = " + authorizationUrl);
        System.out.println("Enter Oauth Verifier :: ");
        Scanner in = new Scanner(System.in);
        String verifier = in.nextLine();


        return service.getAccessToken(requestToken, verifier);
    }

    private static OAuth10aService getSplitWiseOAuth10aService() {
        return new ServiceBuilder()
                  .apiKey("ey0iV8wkwHwcACN8kAXWwDs4dZYWiP5ggE5mEStm")
                  .apiSecret("nHURgQt9orix4J5D1IP6Zk1JoBA4TRDpBpuD8cSc")
                  .build(SplitWiseApi.instance());
    }

    public static OAuth1AccessToken getOAuth1AccessTokenStored() {
        return OAuth1AccessTokenStored;
    }
}
