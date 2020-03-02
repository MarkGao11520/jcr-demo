package com.example.jcrdemo.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class RepositoryServiceTest{

    @Autowired
    private RepositoryService repositoryService;

    private String path = "/a/b/c/test.txt";
    private String content = "hello world";
    private String contentVersion = "hello world 1";
    private String versionComment = "hhh";
    private String userName = "gwf";

    @Test
    void createDir() throws Exception {
        repositoryService.createDir("/a/b/c",userName);
    }

    @Test
    void createFile() throws Exception {
        repositoryService.createFile(path,content,userName);
    }

    @Test
    void readFile() throws Exception {
        InputStream inputStream = repositoryService.readFile(path);
        List<String> contentList = IOUtils.readLines(inputStream);
        contentList.forEach(System.out::println);
        assertEquals(content,contentList.get(0));
    }

    @Test
    void saveFile() throws Exception {
        String versionName = repositoryService.saveFile(path,contentVersion,true, versionComment,userName);
        log.info("VersionName:{}",versionName);
    }

    @Test
    void readFileVersion() throws Exception {
        InputStream inputStream = repositoryService.readFile(path,"1.0");
        List<String> contentList = IOUtils.readLines(inputStream);
        contentList.forEach(System.out::println);
        assertEquals(contentVersion,contentList.get(0));
    }




}