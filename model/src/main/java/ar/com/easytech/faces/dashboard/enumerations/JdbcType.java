package ar.com.easytech.faces.dashboard.enumerations;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author FMQ
 */
public enum JdbcType {

	ARRAY(Types.ARRAY), BIT(Types.BIT), TINYINT(Types.TINYINT), SMALLINT(
			Types.SMALLINT), INTEGER(Types.INTEGER), BIGINT(Types.BIGINT), FLOAT(
			Types.FLOAT), REAL(Types.REAL), DOUBLE(Types.DOUBLE), NUMERIC(
			Types.NUMERIC), DECIMAL(Types.DECIMAL), CHAR(Types.CHAR), VARCHAR(
			Types.VARCHAR), LONGVARCHAR(Types.LONGVARCHAR), DATE(Types.DATE), TIME(
			Types.TIME), TIMESTAMP(Types.TIMESTAMP), BINARY(Types.BINARY), VARBINARY(
			Types.VARBINARY), LONGVARBINARY(Types.LONGVARBINARY), NULL(
			Types.NULL), OTHER(Types.OTHER), BLOB(Types.BLOB), CLOB(Types.CLOB), BOOLEAN(
			Types.BOOLEAN), CURSOR(-10), // Oracle
	UNDEFINED(Integer.MIN_VALUE + 1000), NVARCHAR(-9), // JDK6
	NCHAR(-15), // JDK6
	NCLOB(2011), // JDK6
	STRUCT(Types.STRUCT);

	public final int TYPE_CODE;
	private static Map<Integer, JdbcType> codeLookup = new HashMap<Integer, JdbcType>();

	static {
		for (JdbcType type : JdbcType.values()) {
			codeLookup.put(type.TYPE_CODE, type);
		}
	}

	JdbcType(int code) {
		this.TYPE_CODE = code;
	}

	public static JdbcType forCode(int code) {
		return codeLookup.get(code);
	}
}
