package com.dropbox.client.service.impl;

import com.dropbox.client.gateway.DropBoxGateway;
import com.dropbox.client.gateway.impl.DropBoxGatewayImpl;
import com.dropbox.client.service.DropBoxClientConsoleApp;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import org.apache.commons.io.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DropBoxClientConsoleAppImpl implements DropBoxClientConsoleApp {

  private static final String DEFAULT_USER_LOCALE = Locale.getDefault().toString();
  private static DropBoxClientConsoleApp dropBoxClientConsoleApp = null;
  private DropBoxGateway dropBoxGateway;
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

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
      accessToken = dropBoxGateway.authorize(key, secret, DEFAULT_USER_LOCALE);
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      return;
    }
    System.out.println("Your accessToken: ");
    System.out.println(accessToken);
  }

  @Override
  public void outPutClientFullAccount(String accessToken, String locale) {
    try {
      DbxClientV2 dbxClientV2 = dropBoxGateway.getClient(accessToken, checkUserLocale(locale));
      printClientInfo(dbxClientV2.users().getCurrentAccount());
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

  @Override
  public void outPutDirectoryInfo(String accessToken, String path, String locale) {
    try {
      DbxClientV2 dbxClientV2 = dropBoxGateway.getClient(accessToken, checkUserLocale(locale));
      Metadata rootMetaData = dbxClientV2.files().getMetadata(path);
      System.out.println("--------------------------------------------------------");
      System.out.println(printFileOrFolderInfo(rootMetaData));

      if (rootMetaData instanceof FolderMetadata) {
        ListFolderResult result = dbxClientV2.files().listFolder(path);
        result
            .getEntries()
            .forEach(
                metadata -> {
                  System.out.println("  - " + printFileOrFolderInfo(metadata));
                });
      }
      System.out.println("--------------------------------------------------------");
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

  private String checkUserLocale(String locale) {
    locale = (locale == null || locale.isEmpty()) ? DEFAULT_USER_LOCALE : locale;
    return locale;
  }

  private String getMimeType(String url) {
    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    return fileTypeMap.getContentType(url);
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

  private String printFileOrFolderInfo(Metadata metadata) {
    if (metadata instanceof FolderMetadata) {
      return "/" + metadata.getName() + "  : dir";
    } else if (metadata instanceof FileMetadata) {
      FileMetadata fileMetadata = (FileMetadata) metadata;
      return "/"
          + metadata.getName()
          + "  : file, "
          + FileUtils.byteCountToDisplaySize(fileMetadata.getSize())
          + ",  "
          + getMimeType(fileMetadata.getName())
          + ",  "
          + "modified at: "
          + simpleDateFormat.format(fileMetadata.getClientModified());
    }
    return "";
  }
}
