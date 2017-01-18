package fi.aalto.cs.drumbeat.data.bem.model;

public abstract class BemValue {
	
	public BemValue() {
	}
	
	public abstract Boolean isLiteralType();
	public abstract String toString();	
	public abstract boolean isNullOrAny();
	public abstract boolean equals(Object other);
	
	
	/*****************************************************************************/
	
	public static final BemValue NULL = new BemValue() {
		
		@Override
		public boolean isNullOrAny() {
			return true;
		}

		@Override
		public String toString() {
			return "NULL";
		}

		@Override
		public Boolean isLiteralType() {
			return null;
		}

		@Override
		public boolean equals(Object other) {			
			return (other instanceof BemValue) && ((BemValue)other).isNullOrAny();
		}

	};
	
//	public static final BemValue ANY = new BemValue() {
//		
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public boolean isNullOrAny() {
//			return true;
//		}
//
//		@Override
//		public String toString() {
//			return BemVocabulary.SpfFormat.ANY;
//		}
//
//		@Override
//		public Boolean isLiteralType() {
//			return null;
//		}
//		
////		@Override
////		public RdfNodeTypeEnum getRdfNodeType() {
////			return RdfNodeTypeEnum.BlankNode;
////		}
////
////		@Override
////		public RdfUri toRdfUri() {
////			throw new UnsupportedOperationException();
////		}
////
////		@Override
////		public List<IRdfLink> getRdfLinks() {
////			return null;
////		}
//		
//		@Override
//		public boolean equals(Object other) {			
//			return (other instanceof BemValue) && ((BemValue)other).isNullOrAny();
//		}		
//
//	}	
	

}
