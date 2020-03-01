import gateway.DropBoxGateway;
import utils.ConsoleUtils;

public class DropBoxClientApp {
  public static void main(String[] args) {
    ConsoleUtils.outPutString("Welcome to Dropbox Client Application");

    ConsoleUtils.outPutString("Enter key");
    String key = ConsoleUtils.getStringInput();


    ConsoleUtils.outPutString("Enter secret");
    String secret = ConsoleUtils.getStringInput();

    String accessToken = DropBoxGateway.authorize(key,secret);

    ConsoleUtils.outPutString("Your AccessToken");
    ConsoleUtils.outPutString(accessToken);
  }
}
