package com.dropbox.client.service.impl;

import com.dropbox.client.gateway.DropBoxGateway;
import com.dropbox.client.gateway.impl.DropBoxGatewayImpl;
import com.dropbox.client.service.DropBoxClientConsoleApp;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.common.RootInfo;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.users.Name;
import com.dropbox.core.v2.userscommon.AccountType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DropBoxClientConsoleAppImplTest {

  private ByteArrayOutputStream consoleText;
  private PrintStream console;

  @Mock
  private DropBoxGateway dropBoxGateway = DropBoxGatewayImpl.getInstance();

  @InjectMocks
  private DropBoxClientConsoleApp dropBoxClientConsoleApp = DropBoxClientConsoleAppImpl.getInstance();


  @Before
  public void setUp() {
    consoleText = new ByteArrayOutputStream();
    console = System.out;
    System.setOut(new PrintStream(consoleText));
  }


  @Test
  public void generateAccessToken_when_authorization_failed() throws DbxException {
    when(dropBoxGateway.authorize(anyString(),anyString(),anyString())).thenThrow(new DbxException("authorization call failed"));

    dropBoxClientConsoleApp.generateAccessToken("key","secret");

    Assert.assertTrue(consoleText.toString().contains("authorization call failed"));
  }

  @Test
  public void outPutClientFullAccount_when_fetch_client_info_call_failed() {
    when(dropBoxGateway.getClient(anyString(),anyString())).thenThrow(new RuntimeException("invalid accessToken"));

    dropBoxClientConsoleApp.outPutClientFullAccount("accessToken","locale");

    Assert.assertTrue(consoleText.toString().contains("invalid accessToken"));
  }

  @Test
  public void outPutDirectoryInfo_when_fetch_client_info_call_failed() {
    when(dropBoxGateway.getClient(anyString(),anyString())).thenThrow(new RuntimeException("invalid accessToken"));

    dropBoxClientConsoleApp.outPutDirectoryInfo("accessToken","path","locale");

    Assert.assertTrue(consoleText.toString().contains("invalid accessToken"));
  }

}