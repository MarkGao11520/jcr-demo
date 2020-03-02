package com.example.jcrdemo.repository;

import java.io.InputStream;
import java.util.List;

/**
 * @author gaowenfeng02
 * @desc
 * @date 2020-03-02
 */
public interface RepositoryService {

    /**
     * 读取指定的最新版本的文件
     * @param path 文件考路径
     * @return 返回文件内容
     * @throws Exception 抛出异常
     */
    InputStream readFile(String path) throws Exception;

    /**
     * 读取指定版本文件
     * @param path 文件路径
     * @param version 文件版本号
     * @return 返回文件内容
     * @throws Exception 抛出异常
     */
    InputStream readFile(String path, String version) throws Exception;

    /**
     * 创建目录
     * @param path
     * @param userName
     * @throws Exception
     */
    void createDir(String path,String userName) throws Exception;
    /**
     * 创建文件
     * @param path
     * @param content
     * @param userName
     * @throws Exception
     */
    void createFile(String path,String content,String userName) throws Exception;

    /**
     * 保存文件
     * @param path
     * @param content
     * @param newVersion
     * @param versionComment
     * @throws Exception
     */
    String saveFile(String path,String content,boolean newVersion,String versionComment,String userName) throws Exception;

}
