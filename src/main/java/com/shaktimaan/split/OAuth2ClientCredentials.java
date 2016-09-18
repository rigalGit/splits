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

/**
 * OAuth 2 credentials found in the <a href="http://www.dailymotion.com/profile/developer">
 * Developer Profile Page</a>.
 *
 * <p>
 * Once at the Developer Profile page, you will need to create a Daily Motion account if you do not
 * already have one. Click on "Create a new API Key". Enter "http://127.0.0.1:8080/Callback" under
 * "Callback URL" and select "Native Application" under "Application Profile". Enter a port number
 * other than 8080 if that is what you intend to use.
 * </p>
 *
 * @author Ravi Mistry
 */
public class OAuth2ClientCredentials {

  /** Value of the "API Key". */
  public static final String API_KEY = "9e074003924105a1d034";

  /** Value of the "API Secret". */
  public static final String API_SECRET = "2b4a2c405b713b62d94aff0427d2f756a4f6c812";

  /** Port in the "Callback URL". */
  public static final int PORT = 8080;

  /** Domain name in the "Callback URL". */
  public static final String DOMAIN = "127.0.0.1";

  public static void errorIfNotSpecified() {
    if (API_KEY.startsWith("Enter ") || API_SECRET.startsWith("Enter ")) {
      System.out.println(
          "Enter API Key and API Secret from http://www.dailymotion.com/profile/developer"
          + " into API_KEY and API_SECRET in " + OAuth2ClientCredentials.class);
      System.exit(1);
    }
  }
}
