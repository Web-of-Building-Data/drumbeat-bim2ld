package fi.aalto.cs.drumbeat.data.bedm.model;

public abstract class DrbValue {
	
	public DrbValue() {
	}
	
	public abstract Boolean isLiteralType();
	public abstract String toString();	
	public abstract boolean isNullOrAny();
	public abstract boolean equals(Object other);
	
	
	/*****************************************************************************/
	
//	public static final IfcValue NULL = new IfcValue() {
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
//			return IfcVocabulary.SpfFormat.NULL;
//		}
//
//		@Override
//		public Boolean isLiteralType() {
//			return null;
//		}
//
//		@Override
//		public boolean equals(Object other) {			
//			return (other instanceof IfcValue) && ((IfcValue)other).isNullOrAny();
//		}
//
//	};
//	
//	public static final IfcValue ANY = new IfcValue() {
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
//			return IfcVocabulary.SpfFormat.ANY;
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
//			return (other instanceof IfcValue) && ((IfcValue)other).isNullOrAny();
//		}		
//
//	}	
	

}
