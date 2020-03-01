import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import gateway.DropBoxGateway;
import utils.ConsoleUtils;

public class DropBoxClientApp {
  public static void main(String[] args) throws DbxException {
    ConsoleUtils.outPutString("Welcome to Dropbox Client Application");

    boolean run = true;
    DbxClientV2 dbxClientV2 = null;
    while (run) {
      ConsoleUtils.outPutString("Please enter command from the auth, info, list");
      Integer command = Integer.valueOf(ConsoleUtils.getStringInput());

      switch (command) {
        case 1:
          {
            ConsoleUtils.outPutString("Enter key");
            String key = ConsoleUtils.getStringInput();

            ConsoleUtils.outPutString("Enter secret");
            String secret = ConsoleUtils.getStringInput();

            String accessToken = DropBoxGateway.authorize(key, secret);

            ConsoleUtils.outPutString("Your AccessToken");
            ConsoleUtils.outPutString(accessToken);

            dbxClientV2 = DropBoxGateway.getClient(accessToken);
            break;
          }
        case 2:
        {
          ConsoleUtils.outPutString("Enter accessToken");
          String accessToken = ConsoleUtils.getStringInput();

          FullAccount fullAccount = dbxClientV2.users().getCurrentAccount();
          ConsoleUtils.outPutString(fullAccount.toString());
        }
      }
    }
  }
}
