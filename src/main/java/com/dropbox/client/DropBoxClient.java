package com.dropbox.client;

import com.dropbox.client.constant.Command;
import com.dropbox.client.service.DropBoxClientConsoleApp;
import com.dropbox.client.service.impl.DropBoxClientConsoleAppImpl;
import com.dropbox.core.DbxException;

public class DropBoxClient {

  private static DropBoxClientConsoleApp dropBoxClientConsoleApp =
      DropBoxClientConsoleAppImpl.getInstance();

  public static void main(String[] args) throws DbxException {

    if (validatedArgument(args, 1, "Please give command")) return;

    Command command = Command.getCommand(args[0]);
    if (command == null) {
      System.out.println("Please provide a valid command");
      return;
    }

    switch (command) {
      case AUTH:
        {
          if (validatedArgument(args, 3, "Please provide both key and secret")) return;
          dropBoxClientConsoleApp.generateAccessToken(args[1],args[2]);
          break;
        }
      case INFO:
        {
          if (validatedArgument(args, 2, "Please provide access token")) return;
          dropBoxClientConsoleApp.outPutClientFullAccount(args[1]);
          break;
        }
      case LIST:
        {
          if (validatedArgument(args, 2, "Please provide access token")) return;
          boolean isPathGiven = args.length >= 3;
          dropBoxClientConsoleApp.outPutDirectoryInfo(args[1], isPathGiven ? args[2] : "");
          break;
        }
    }
  }
  private static boolean validatedArgument(String[] args, int i, String s) {
    if (args.length < i) {
      System.out.println(s);
      return true;
    }
    return false;
  }
}
