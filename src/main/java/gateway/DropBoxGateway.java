package gateway;

import com.dropbox.core.*;
import utils.ConsoleUtils;

public class DropBoxGateway {

  public static String authorize(String key, String secret){
    DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder("sample-app/1.0").build();
    DbxAppInfo dbxAppInfo = new DbxAppInfo(key,secret);
    DbxWebAuth dbxWebAuth = new DbxWebAuth(dbxRequestConfig,dbxAppInfo);

    DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder()
            .withNoRedirect()
            .build();

    String authorizeUrl = dbxWebAuth.authorize(request);
    ConsoleUtils.outPutString("1. Go to " + authorizeUrl);
    ConsoleUtils.outPutString("2. Click Allow (you might have to log in first).");
    ConsoleUtils.outPutString("3. Copy the authorization code.;");
    ConsoleUtils.outPutString("Enter the authorization code here: ");

    String code = ConsoleUtils.getStringInput();

    DbxAuthFinish authFinish;
    try {
      authFinish = dbxWebAuth.finishFromCode(code);
    } catch (DbxException ex) {
      throw new RuntimeException("Error in DbxWebAuth.authorize: " + ex.getMessage());
    }
    return authFinish.getAccessToken();
  }
}
