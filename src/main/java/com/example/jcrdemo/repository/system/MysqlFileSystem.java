package com.example.jcrdemo.repository.system;


import com.example.jcrdemo.repository.system.BaseDbFileSystem;

/**
 * @author gaowenfeng02
 */
public class MysqlFileSystem extends BaseDbFileSystem {
	@Override
	public String databaseType() {
		return "mysql";
	}
}
