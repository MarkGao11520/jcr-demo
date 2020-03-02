package com.example.jcrdemo.repository.manager;

import com.example.jcrdemo.repository.manager.DbPersistenceManager;
import org.apache.jackrabbit.core.persistence.PMContext;

/**
 * @author gaowenfeng02
 */
public class MySqlPersistenceManager extends DbPersistenceManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(PMContext context) throws Exception {
        // init default values
        if (getDriver() == null) {
            setDriver("org.gjt.mm.mysql.Driver");
        }
        if (getDatabaseType() == null) {
            setDatabaseType("mysql");
        }
        super.init(context);
    }

}
