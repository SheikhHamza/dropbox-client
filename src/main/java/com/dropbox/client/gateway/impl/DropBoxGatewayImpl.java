package com.dropbox.client.gateway.impl;

import com.dropbox.client.gateway.DropBoxGateway;
import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DropBoxGatewayImpl implements DropBoxGateway {

  private static final String CLIENT_IDENTIFIER = "sample-app/1.0";
  private static DropBoxGateway dropBoxGateway = null;

  private DropBoxGatewayImpl() {}

  public static DropBoxGateway getInstance() {
    if (dropBoxGateway == null) {
      dropBoxGateway = new DropBoxGatewayImpl();
    }
    return dropBoxGateway;
  }

  @Override
  public String authorize(String key, String secret, String locale) throws DbxException {
    DbxAppInfo dbxAppInfo = new DbxAppInfo(key, secret);
    DbxWebAuth dbxWebAuth = new DbxWebAuth(getDbxRequestConfig(locale), dbxAppInfo);

    DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder().withNoRedirect().build();

    String authorizeUrl = dbxWebAuth.authorize(request);
    System.out.println("1. Go to " + authorizeUrl);
    System.out.println("2. Click Allow (you might have to log in first).");
    System.out.println("3. Copy the authorization code.;");
    System.out.println("Enter the authorization code here: ");

    String code = readInput();

    DbxAuthFinish authFinish;
    authFinish = dbxWebAuth.finishFromCode(code);

    return authFinish.getAccessToken();
  }

  @Override
  public DbxClientV2 getClient(String accessToken, String locale) {
    return new DbxClientV2(getDbxRequestConfig(locale), accessToken);
  }

  private String readInput() {
    String inputString = null;
    try {
      inputString = new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
    }
    return inputString;
  }

  private DbxRequestConfig getDbxRequestConfig(String locale) {
    return DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).withUserLocale(locale).build();
  }
}
