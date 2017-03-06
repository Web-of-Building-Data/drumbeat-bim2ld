package fi.aalto.cs.drumbeat.rdf.data.msg;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

import fi.aalto.cs.drumbeat.common.collections.SortedList;
import fi.aalto.cs.drumbeat.common.string.StringUtils;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumCalculator;
import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.utils.print.RdfPrinter;

public class RdfMsgContainerPrinter extends RdfPrinter {

	private static final String PREFIX_SHARED_NODE = "[S]";
	private static final String PREFIX_TOP_NODE = "[T]";

	private String tabString = StringUtils.TABULAR;

	public RdfMsgContainerPrinter() {
		super(null, null);
	}
	
	public RdfMsgContainerPrinter(Map<String, String> nsPrefixMap) {
		super(nsPrefixMap, null);
	}
	
	public RdfMsgContainerPrinter(Map<String, String> nsPrefixMap, RdfChecksumCalculator checksumCalculator) {
		super(nsPrefixMap, checksumCalculator);
	}
	
	public String getTabString() {
		return tabString;
	}
	
	public void setTabString(String tabString) {
		this.tabString = tabString;
	}	
	
	public StringBuilder appendToStringBuilder(RdfTree tree, StringBuilder sb, int tabsCount, boolean exportOnlyTopAndSharedNodes) throws RdfChecksumException {
		
		boolean exportThisLevel = !exportOnlyTopAndSharedNodes || tree.isTopTree() || tree.isSharedTree();
		
		for (Entry<Statement, RdfTree> entry : tree.entrySet()) {
			Statement statement = entry.getKey();
			
			if (exportThisLevel) {
			
				for (int i = 0; i < tabsCount; ++i) {
					sb.append(tabString);
				}
				
				if (tree.isTopTree()) {
					sb.append(PREFIX_TOP_NODE);				
					
				} else if (tree.isSharedTree()) {
					sb.append(PREFIX_SHARED_NODE);
				}
				
				sb.append(toString(statement));
				sb.append(StringUtils.END_LINE);
			}
			
			RdfTree subTree = entry.getValue();
			if (subTree != null) {
				appendToStringBuilder(subTree, sb, tabsCount + 1, exportOnlyTopAndSharedNodes);
			}			
		}
		
		return sb;
	}
	
	public StringBuilder appendToStringBuilder(RdfMsg msg, StringBuilder sb, int tabsCount,
			boolean exportOnlyTopAndSharedNodes) throws RdfChecksumException
	{
		// sort topTrees by depth (deepest first)
		SortedList<RdfTree> topTreeList = new SortedList<>(new Comparator<RdfTree>() {

			@Override
			public int compare(RdfTree o1, RdfTree o2) {
				int result = Integer.compare(o1.getDepth(), o2.getDepth());
				if (result != 0) {
					return -result;
				}

				return o1.compareTo(o2);
			}

		});

		topTreeList.addAll(msg.getTopTrees());

		int maxDepth = msg.getMaxDepth();		

		for (RdfTree topTree : topTreeList) {
			appendToStringBuilder(topTree, sb, maxDepth - topTree.getDepth() + tabsCount, exportOnlyTopAndSharedNodes);
			sb.append(StringUtils.END_LINE);
		}

		return sb;
	}

	public String toString(RdfTree tree, int tabsCount, boolean exportOnlyTopAndSharedNodes) throws RdfChecksumException {
		StringBuilder sb = new StringBuilder();
		appendToStringBuilder(tree, sb, tabsCount, exportOnlyTopAndSharedNodes);
		return sb.toString();		
	}
	
	public String toString(RdfTree tree) throws RdfChecksumException {
		return toString(tree, 0, false);
	}
	
	
	public String toString(RdfMsg msg, int tabsCount, boolean exportOnlyTopAndSharedNodes) throws RdfChecksumException {
		StringBuilder sb = new StringBuilder();
		appendToStringBuilder(msg, sb, tabsCount, exportOnlyTopAndSharedNodes);
		return sb.toString();
	}

	public String toString(RdfMsg msg) throws RdfChecksumException {
		return toString(msg, 0, false);
	}
	
	public String toString(Object object) throws RdfChecksumException {
		if (object == null) {
			return "null";
		}
	
		if (object instanceof RDFNode) {
			return super.toString((RDFNode)object);
		} else if (object instanceof Statement) {
			return super.toString((Statement)object);
		} else if (object instanceof RdfTree) {
			return toString((RdfTree)object);
		} else if (object instanceof RdfMsg) {
			return toString((RdfMsg)object);
		} else {
			throw new NotImplementedException(object.getClass().toString());
		}
	}
	


}
