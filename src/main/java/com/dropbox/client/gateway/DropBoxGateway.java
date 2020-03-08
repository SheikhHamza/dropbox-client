package com.dropbox.client.gateway;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

public interface DropBoxGateway {
  String authorize(String key, String secret, String locale) throws DbxException;

  DbxClientV2 getClient(String accessToken, String locale);
}
