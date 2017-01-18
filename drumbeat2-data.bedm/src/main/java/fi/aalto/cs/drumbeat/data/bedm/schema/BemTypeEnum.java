package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.EnumSet;

public enum BemTypeEnum {	
	
	ENTITY,
	BINARY,
	ENUM,
	INTEGER,
	REAL,
	NUMBER,
	STRING,
	LOGICAL,
	DATETIME;
	
	public static final EnumSet<BemTypeEnum> PRIMITIVE	= EnumSet.of(ENUM, INTEGER, REAL, NUMBER, STRING, LOGICAL, DATETIME);
	public static final EnumSet<BemTypeEnum> COMPLEX	= EnumSet.of(ENTITY);
	public static final EnumSet<BemTypeEnum> NUMBERIRC	= EnumSet.of(INTEGER, REAL, NUMBER);
	
}
