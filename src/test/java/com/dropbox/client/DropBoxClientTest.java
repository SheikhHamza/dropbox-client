package com.dropbox.client;

import com.dropbox.client.constant.Command;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DropBoxClientTest {

  private ByteArrayOutputStream consoleText;

  @Before
  public void setUp() {
    consoleText = new ByteArrayOutputStream();
    System.setOut(new PrintStream(consoleText));
  }

  @Test
  public void main_when_no_arg_is_given() {
    DropBoxClient.main(new String[] {});
    Assert.assertTrue(consoleText.toString().contains("Please give command"));
  }

  @Test
  public void main_when_invalid_arg_given() {
    DropBoxClient.main(new String[] {"invalid"});
    Assert.assertTrue(consoleText.toString().contains("Please provide a valid command"));
  }

  @Test
  public void main_auth_command_with_without_key_secret() {
    DropBoxClient.main(new String[] {Command.AUTH.getCode()});
    Assert.assertTrue(consoleText.toString().contains("Please provide both key and secret"));
  }

  @Test
  public void main_info_command_with_without_access_token() {
    DropBoxClient.main(new String[] {Command.INFO.getCode()});
    Assert.assertTrue(consoleText.toString().contains("Please provide access token"));
  }

  @Test
  public void main_list_command_with_without_access_token() {
    DropBoxClient.main(new String[] {Command.LIST.getCode()});
    Assert.assertTrue(consoleText.toString().contains("Please provide both access token and path"));
  }

  @Test
  public void main_list_command_with_without_path() {
    DropBoxClient.main(new String[] {Command.LIST.getCode(), "accessToken"});
    Assert.assertTrue(consoleText.toString().contains("Please provide both access token and path"));
  }
}
