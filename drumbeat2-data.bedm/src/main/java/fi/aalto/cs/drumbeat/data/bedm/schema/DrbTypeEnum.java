package fi.aalto.cs.drumbeat.data.bedm.schema;

import java.util.EnumSet;

public enum DrbTypeEnum {	
	
	ENTITY,
	BINARY,
	ENUM,
	INTEGER,
	REAL,
	NUMBER,
	STRING,
	LOGICAL,
	DATETIME;
	
	public static final EnumSet<DrbTypeEnum> PRIMITIVE	= EnumSet.of(ENUM, INTEGER, REAL, NUMBER, STRING, LOGICAL, DATETIME);
	public static final EnumSet<DrbTypeEnum> COMPLEX	= EnumSet.of(ENTITY);
	public static final EnumSet<DrbTypeEnum> NUMBERIRC	= EnumSet.of(INTEGER, REAL, NUMBER);
	
}
