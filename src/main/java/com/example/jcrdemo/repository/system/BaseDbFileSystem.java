package com.example.jcrdemo.repository.system;

import com.example.jcrdemo.utils.ApplicationContextHelper;
import org.apache.jackrabbit.core.fs.FileSystemException;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;

import javax.sql.DataSource;

/**
 * @author gaowenfeng02
 */
public abstract class BaseDbFileSystem extends DbFileSystem {
	@Override
	public void init() throws FileSystemException {
        if (initialized) {
            throw new IllegalStateException("already initialized");
        }
        try {
        	setSchema(databaseType());
            conHelper = createConnectionHelper(ApplicationContextHelper.getApplicationContext().getBean(DataSource.class));

            // make sure schemaObjectPrefix consists of legal name characters only
            schemaObjectPrefix = conHelper.prepareDbIdentifier(schemaObjectPrefix);

            // check if schema objects exist and create them if necessary
            if (isSchemaCheckEnabled()) {
                createCheckSchemaOperation().run();
            }

            // build sql statements
            buildSQLStatements();

            // finally verify that there's a file system root entry
            verifyRootExists();

            initialized = true;
        } catch (Exception e) {
            String msg = "failed to initialize file system";
            throw new FileSystemException(msg, e);
        }
	}
	public abstract String databaseType();
}
