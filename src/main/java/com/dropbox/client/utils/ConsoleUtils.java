package com.dropbox.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtils {

  public static String getStringInput() {
    String inputString = null;
    try {
      inputString = new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return inputString == null ? "" : inputString;
  }

  public static void outPutString(String outputString) {
    System.out.println(outputString);
  }
}
