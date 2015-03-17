package com.innofi.framework.exception;

import java.sql.SQLException;

/**
 *
 */
public class DAOException extends BaseException {

	private String SQLString;
	private String SQLState;
	private int errorCode;

	public DAOException(SysMessage msg) {
		super(msg);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

	public DAOException(SysMessage msg, Throwable cause) {
		super(msg, cause);
	}

	public DAOException(String msg) {
		super(msg);
	}

	public DAOException(SQLException ex) {
		super(ex);
		this.SQLState = ex.getSQLState();
		this.errorCode = ex.getErrorCode();
	}

	public DAOException(String msg, Throwable ex) {
		super(msg,ex);
	}
	
	public DAOException(String msg, SysMessage sysMsg, Throwable cause) {
		super(msg, sysMsg, cause);
	}

	public String getSQLString() {
		return this.SQLString;
	}

	public void setSQLString(String SQLString) {
		this.SQLString = SQLString;
	}

	public String getSQLState() {
		return this.SQLState;
	}

	public void setSQLState(String SQLState) {
		this.SQLState = SQLState;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}	
	
	
}
