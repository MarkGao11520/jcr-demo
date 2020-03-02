package com.example.jcrdemo.repository;

import com.example.jcrdemo.exception.DefaultException;
import com.example.jcrdemo.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockManager;
import javax.jcr.version.VersionManager;
import java.io.File;
import java.io.InputStream;

/**
 * @author gaowenfeng02
 */
@Slf4j
public abstract class BaseRepositoryService implements ApplicationContextAware {
	public static final String RES_PACKGE_FILE="___res__package__file__";
	public static final String CLIENT_CONFIG_FILE="___client_config__file__";
	public static final String RESOURCE_SECURITY_CONFIG_FILE="___resource_security_config__file__";
	private static final String MYSQL_XML = "classpath:mysql.xml";
    protected final String DATA = "_data";
	protected final String DIR_TAG = "_dir";
	protected final String FILE = "_file";
	protected final String CRATE_USER = "_create_user";
	protected final String CRATE_DATE = "_create_date";
	protected final String VERSION_COMMENT="_version_comment";
	protected final String COMPANY_ID="_company_id";

	private ApplicationContext applicationContext;

	protected RepositoryImpl repository;
	protected Session session;
	protected VersionManager versionManager;
	protected LockManager lockManager;



	protected String processPath(String path) {
		if (path.startsWith("/")) {
			return path.substring(1, path.length());
		}
		return path;
	}

	protected Node getRootNode() throws Exception{
		return session.getRootNode();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)  {
        try {
            ApplicationContextHelper.setApplicationContext(applicationContext);
            this.applicationContext = applicationContext;
            initRepositoryByXml();
            SimpleCredentials cred = new SimpleCredentials("admin", "admin".toCharArray());
            cred.setAttribute("AutoRefresh", true);
            session = repository.login(cred, null);
            versionManager = session.getWorkspace().getVersionManager();
            lockManager=session.getWorkspace().getLockManager();
        } catch (Exception e) {
            throw new DefaultException(e);
        }


    }

    private void initRepositoryByXml()throws Exception {
        log.info("Build repository from user custom xml file...");
        InputStream inputStream=null;
        try{
            inputStream= ApplicationContextHelper.getApplicationContext().getResource(MYSQL_XML).getInputStream();
            String tempRepoHomeDir= "/tmp/urule-temp-repo-home/";
            File tempDir=new File(tempRepoHomeDir);
            clearTempDir(tempDir);
            RepositoryConfig repositoryConfig = RepositoryConfig.create(inputStream,tempRepoHomeDir);
            repository=RepositoryImpl.create(repositoryConfig);
        }finally{
            if(inputStream!=null){
                inputStream.close();
            }
        }
    }

    private void clearTempDir(File file){
        if(file.isDirectory()){
            for(File childFile:file.listFiles()){
                clearTempDir(childFile);
            }
        }
        file.delete();
    }
}
