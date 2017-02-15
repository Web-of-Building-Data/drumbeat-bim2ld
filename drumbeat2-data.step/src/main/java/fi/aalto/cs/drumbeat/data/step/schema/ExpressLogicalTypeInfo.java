package fi.aalto.cs.drumbeat.data.step.schema;

import java.util.Collection;

import fi.aalto.cs.drumbeat.data.bem.schema.BemEnumerationTypeInfo;
import fi.aalto.cs.drumbeat.data.bem.schema.BemSchema;

public class ExpressLogicalTypeInfo extends BemEnumerationTypeInfo {

	public ExpressLogicalTypeInfo(BemSchema schema, String typeName, Collection<String> values) {
		super(schema, typeName, values);
	}	
	
}
