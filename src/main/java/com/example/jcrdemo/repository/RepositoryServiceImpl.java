package com.example.jcrdemo.repository;

import com.example.jcrdemo.exception.DefaultException;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.jackrabbit.value.DateValue;
import org.springframework.stereotype.Component;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gaowenfeng02
 * @desc
 * @date 2020-03-02
 */
@Component
public class RepositoryServiceImpl extends BaseRepositoryService implements RepositoryService{
    @Override
    public InputStream readFile(String path) throws Exception {
        Node rootNode=getRootNode();
        path = processPath(path);
        if (!rootNode.hasNode(path)) {
            throw new DefaultException("File [" + path + "] not exist.");
        }
        Node fileNode = rootNode.getNode(path);
        Property property = fileNode.getProperty(DATA);
        Binary fileBinary = property.getBinary();
        return fileBinary.getStream();
    }

    @Override
    public InputStream readFile(String path, String version) throws Exception {
        path = processPath(path);
        Node rootNode=getRootNode();
        if (!rootNode.hasNode(path)) {
            throw new DefaultException("File [" + path + "] not exist.");
        }
        Node fileNode = rootNode.getNode(path);
        VersionHistory versionHistory = versionManager.getVersionHistory(fileNode.getPath());
        Version v = versionHistory.getVersion(version);
        Node fnode = v.getFrozenNode();
        Property property = fnode.getProperty(DATA);
        Binary fileBinary = property.getBinary();
        return fileBinary.getStream();
    }

    @Override
    public void createDir(String path, String userName) throws Exception {
        Node rootNode=getRootNode();
        path = processPath(path);
        if (rootNode.hasNode(path)) {
            throw new DefaultException("Dir [" + path + "] already exist.");
        }
        boolean add = false;
        String[] subpaths = path.split("/");
        Node parentNode = rootNode;
        for (String subpath : subpaths) {
            if (StringUtils.isEmpty(subpath)) {
                continue;
            }
            String subDirs[] = subpath.split("\\.");
            for (String dir : subDirs) {
                if (StringUtils.isEmpty(dir)) {
                    continue;
                }
                if (parentNode.hasNode(dir)) {
                    parentNode = parentNode.getNode(dir);
                } else {
                    parentNode = parentNode.addNode(dir);
                    parentNode.addMixin("mix:versionable");
                    parentNode.addMixin("mix:lockable");
                    parentNode.setProperty(DIR_TAG, true);
                    parentNode.setProperty(FILE, true);
                    parentNode.setProperty(CRATE_USER,userName);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    DateValue dateValue = new DateValue(calendar);
                    parentNode.setProperty(CRATE_DATE, dateValue);
                    add = true;
                }
            }
        }
        if (add) {
            session.save();
        }
    }

    @Override
    public void createFile(String path, String content, String userName) throws Exception {
        createFileNode(path,content,userName,true);
    }

    @Override
    public String saveFile(String path, String content, boolean newVersion, String versionComment,String userName) throws Exception {
        final String processedPath = processPath(path);

        Node rootNode=getRootNode();
        if (!rootNode.hasNode(processedPath)) {
            throw new DefaultException("File [" + processedPath + "] not exist.");
        }
        Node fileNode = rootNode.getNode(processedPath);
        versionManager.checkout(fileNode.getPath());
        Binary fileBinary = new BinaryImpl(content.getBytes("utf-8"));
        fileNode.setProperty(DATA, fileBinary);
        fileNode.setProperty(FILE, true);
        fileNode.setProperty(CRATE_USER, userName);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DateValue dateValue = new DateValue(calendar);
        fileNode.setProperty(CRATE_DATE, dateValue);
        if (newVersion && StringUtils.isNotBlank(versionComment)) {
            fileNode.setProperty(VERSION_COMMENT, versionComment);
        }
        session.save();
        if (newVersion) {
            Version v = versionManager.checkin(fileNode.getPath());
            return v.getName();
        }
        return null;
    }

    private void createFileNode(String path, String content,String user,boolean isFile) throws Exception{
        Node rootNode=getRootNode();
        path = processPath(path);
        try {
            if (rootNode.hasNode(path)) {
                throw new DefaultException("File [" + path + "] already exist.");
            }
            Node fileNode = rootNode.addNode(path);
            fileNode.addMixin("mix:versionable");
            fileNode.addMixin("mix:lockable");
            Binary fileBinary = new BinaryImpl(content.getBytes());
            fileNode.setProperty(DATA, fileBinary);
            if(isFile){
                fileNode.setProperty(FILE, true);
            }
            fileNode.setProperty(CRATE_USER, user);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            DateValue dateValue = new DateValue(calendar);
            fileNode.setProperty(CRATE_DATE, dateValue);
            session.save();
        } catch (Exception ex) {
            throw new DefaultException(ex);
        }
    }
}
