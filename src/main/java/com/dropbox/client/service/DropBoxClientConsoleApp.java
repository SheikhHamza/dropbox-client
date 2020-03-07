package com.dropbox.client.service;

public interface DropBoxClientConsoleApp {

    void generateAccessToken(String key, String secret);
    void outPutClientFullAccount(String accessToken);
    void outPutDirectoryInfo(String accessToken,String path);

}
