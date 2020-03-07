package com.dropbox.client.service.impl;

import com.dropbox.client.gateway.DropBoxGateway;
import com.dropbox.client.gateway.impl.DropBoxGatewayImpl;
import com.dropbox.client.service.DropBoxClientConsoleApp;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class DropBoxClientConsoleAppImpl implements DropBoxClientConsoleApp {

  private Logger logger = Logger.getLogger(DropBoxGatewayImpl.class.getName());

  private static DropBoxClientConsoleApp dropBoxClientConsoleApp = null;
  private DropBoxGateway dropBoxGateway;

  private DropBoxClientConsoleAppImpl(DropBoxGateway dropBoxGateway) {
    this.dropBoxGateway = dropBoxGateway;
  }

  public static DropBoxClientConsoleApp getInstance() {
    if (dropBoxClientConsoleApp == null) {
      dropBoxClientConsoleApp = new DropBoxClientConsoleAppImpl(DropBoxGatewayImpl.getInstance());
    }
    return dropBoxClientConsoleApp;
  }

  @Override
  public void generateAccessToken(String key, String secret) {
    String accessToken = null;
    try {
      accessToken = dropBoxGateway.authorize(key, secret);
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      return;
    }
    System.out.println("Your AccessToken");
    System.out.println(accessToken);
  }

  @Override
  public void outPutClientFullAccount(String accessToken) {
    DbxClientV2 dbxClientV2 = dropBoxGateway.getClient(accessToken);
    try {
      printClientInfo(dbxClientV2.users().getCurrentAccount());
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

  @Override
  public void outPutDirectoryInfo(String accessToken, String path) {
    DbxClientV2 dbxClientV2 = dropBoxGateway.getClient(accessToken);

    try {
      ListFolderResult result = dbxClientV2.files().listFolder(path);

      System.out.println("--------------------------------------------------------");
      while (true) {
        for (Metadata metadata : result.getEntries()) {
          System.out.println(metadata.getPathLower() + "    :" + metadata.toStringMultiline());
        }

        if (!result.getHasMore()) {
          break;
        }

        result = dbxClientV2.files().listFolderContinue(result.getCursor());
      }
      System.out.println("--------------------------------------------------------");
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

  private static void printClientInfo(FullAccount fullAccount) {
    System.out.println("--------------------------------------------------------");
    System.out.println("User ID:      " + fullAccount.getAccountId());
    System.out.println("Display Name: " + fullAccount.getName().getDisplayName());
    System.out.println(
        "Name:         "
            + fullAccount.getName().getGivenName()
            + " "
            + fullAccount.getName().getSurname()
            + " "
            + fullAccount.getName().getFamiliarName());
    System.out.println(
        "Email:        "
            + fullAccount.getEmail()
            + " "
            + "("
            + (fullAccount.getEmailVerified() ? "Verified" : "")
            + ")");
    System.out.println("Country:      " + fullAccount.getCountry());
    System.out.println("Referral link " + fullAccount.getReferralLink());
    System.out.println("--------------------------------------------------------");
  }
}
