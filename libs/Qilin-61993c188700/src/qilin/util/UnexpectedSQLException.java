package qilin.util;

import java.sql.SQLException;

public class UnexpectedSQLException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnexpectedSQLException(String msg, SQLException e) {
		super(msg, e);
	}
}
