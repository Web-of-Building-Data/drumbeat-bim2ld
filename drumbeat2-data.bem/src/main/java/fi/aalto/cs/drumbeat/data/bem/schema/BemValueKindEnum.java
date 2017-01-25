package fi.aalto.cs.drumbeat.data.bem.schema;

import java.util.EnumSet;

public enum BemValueKindEnum {
	
	// Simple types	
	BINARY,
	INTEGER,
	REAL,
	NUMBER,
	STRING,
	LOGICAL,
	DATETIME,
	ENUM,
	
	// Complex types	
	COLLECTION,
	ENTITY,
	SELECT,
	;
	
	public static final EnumSet<BemValueKindEnum> NUMBERIC	= EnumSet.of(INTEGER, REAL, NUMBER);
	public static final EnumSet<BemValueKindEnum> PRIMITIVE	= EnumSet.of(INTEGER, REAL, NUMBER, STRING, LOGICAL, DATETIME);
	public static final EnumSet<BemValueKindEnum> SIMPLE	= EnumSet.of(INTEGER, REAL, NUMBER, STRING, LOGICAL, DATETIME, ENUM);
	
	public static final EnumSet<BemValueKindEnum> COMPLEX	= EnumSet.of(ENTITY, SELECT);
	
	
}
