package com.dropbox.client;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.client.gateway.DropBoxGateway;
import com.dropbox.client.utils.ConsoleUtils;

public class DropBoxClientApp {

  private static String AUTH = "auth";
  private static String INFO = "info";
  private static String LIST = "list";

  public static void main(String[] args) throws DbxException {

    if (args.length < 1) {
      System.out.println("Please give command");
      return;
    }

    if (AUTH.equals(args[0])) {
      if (args.length < 3) {
        System.out.println("Please provide both key and secret");
        return;
      }

      String accessToken = DropBoxGateway.authorize(args[1], args[2]);

      ConsoleUtils.outPutString("Your AccessToken");
      ConsoleUtils.outPutString(accessToken);

    } else if (INFO.equals(args[0])) {

      if (args.length < 2) {
        System.out.println("Please provide access token");
        return;
      }

      DbxClientV2 dbxClientV2 = DropBoxGateway.getClient(args[1]);
      FullAccount fullAccount = dbxClientV2.users().getCurrentAccount();
      System.out.println("--------------------------------------------------------");
      System.out.println("User ID:      "+ fullAccount.getAccountId());
      System.out.println("Display Name: "+ fullAccount.getName().getDisplayName());
      System.out.println("Name:         "+ fullAccount.getName().getGivenName() + " " + fullAccount.getName().getSurname() + " " + fullAccount.getName().getFamiliarName());
      System.out.println("Email:        "+ fullAccount.getEmail() + " " + "(" + (fullAccount.getEmailVerified()?"Verified":"") + ")");
      System.out.println("Country:      "+ fullAccount.getCountry());
      System.out.println("Referral link "+ fullAccount.getReferralLink());
      System.out.println("--------------------------------------------------------");

    } else if (LIST.equals(args[0])) {

      boolean isPathGiven = args.length>3;

      if(args.length < 2){
        System.out.println("Please provide access token");
        return;
      }

      DbxClientV2 dbxClientV2 = DropBoxGateway.getClient(args[1]);

      ListFolderResult result = dbxClientV2.files().listFolder(isPathGiven?args[2]:"");

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
    }
  }
}
