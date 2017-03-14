package fi.aalto.cs.drumbeat.rdf.data.msg;

import java.util.*;
import java.util.function.Function;

import org.apache.jena.rdf.model.*;

import fi.aalto.cs.drumbeat.rdf.data.RdfChecksumException;
import fi.aalto.cs.drumbeat.rdf.data.RdfComparatorPool;
import fi.aalto.cs.drumbeat.rdf.data.RdfNodeTypeChecker;

public class RdfMsgContainerBuilder {

	private final Model model;
	private final RdfNodeTypeChecker nodeTypeChecker;

	private final Map<Resource, RdfTree> localResourceTrees;
	
	private final RdfMsgContainer msgContainer;
	private final Set<RdfMsg> msgs;
	

	private RdfMsgContainerBuilder(Model model, RdfComparatorPool comparatorPool) {
		this.model = model;
		nodeTypeChecker = comparatorPool.getNodeTypeChecker();

		localResourceTrees = new HashMap<>();		

		msgContainer = new RdfMsgContainer(comparatorPool);
		msgs = new HashSet<>();
		
	}
	
	public static RdfMsgContainer build(Model model, Function<Resource, Boolean> localResourceChecker) throws RdfChecksumException {		
		return build(model, new RdfComparatorPool(localResourceChecker));
	}
	
	public static RdfMsgContainer build(Model model, RdfNodeTypeChecker nodeTypeChecker) throws RdfChecksumException {		
		return build(model, new RdfComparatorPool(nodeTypeChecker));
	}

	public static RdfMsgContainer build(Model model, RdfComparatorPool comparatorPool) throws RdfChecksumException {		
		RdfMsgContainerBuilder builder = new RdfMsgContainerBuilder(model, comparatorPool);
		return builder.internalBuild();		
	}
	
	private RdfMsgContainer internalBuild() throws RdfChecksumException {		

		ResIterator subjectsResourcesIterator = model.listSubjects();

		while (subjectsResourcesIterator.hasNext()) {
			Resource subjectResource = subjectsResourcesIterator.next();
			
			if (!nodeTypeChecker.isLocalResource(subjectResource)) {
				
				StmtIterator propertyStatements = subjectResource.listProperties();
				while (propertyStatements.hasNext()) {
					Statement propertyStatement = propertyStatements.next();
					List<Statement> propertyList = new ArrayList<>(1);
					propertyList.add(propertyStatement);
					RdfMsg msg = new RdfMsg(msgContainer);
					RdfTree tree = buildTree(msg, subjectResource, true, propertyList);
					msg.addTopTree(tree);
					msgs.add(msg);
				}				
				
			} else if (!model.contains(null, null, subjectResource)) {
				
				// local resource with no incoming links				
				RdfMsg msg = new RdfMsg(msgContainer);
				RdfTree tree = buildTree(msg, subjectResource, true, subjectResource.listProperties().toList());
				msg.addTopTree(tree);
				msgs.add(msg);
				
			}
		}

		for (RdfMsg msg : msgs) {
			msgContainer.addMsg(msg);
		}
		
		return msgContainer;
	}
	
	private RdfTree buildTree(RdfMsg msg, Resource headNode, boolean isTop, List<Statement> propertyStatements) {
		
		RdfTree tree;
		
		if (isTop) {
			
			tree = new RdfTree(headNode, msgContainer);
			tree.setType(RdfTreeTypeEnum.Top);
			
		} else {
		
			tree = localResourceTrees.get(headNode);
			if (tree != null) {
				tree.setType(RdfTreeTypeEnum.SharedSub);
				mergeAndRemoveMsgs(msg, tree);
				return tree;				
			} else {
				tree = new RdfTree(headNode, msgContainer);
				tree.setType(RdfTreeTypeEnum.Sub);
				localResourceTrees.put(headNode, tree);
			}
			
		}
		
		for (Statement propertyStatement : propertyStatements) {
			
			RdfTree subTree = null;
			RDFNode statementObject = propertyStatement.getObject();
			if (nodeTypeChecker.isLocalResource(statementObject)) {
				subTree = buildTree(msg, (Resource)statementObject, false, ((Resource)statementObject).listProperties().toList());
			}
			
			tree.put(propertyStatement, subTree);
		}
		
		return tree;
		
	}
	
	private void mergeAndRemoveMsgs(RdfMsg newMsg, RdfTree sharedTree) {
		RdfMsg oldMsg = sharedTree.getMsg();
		if (newMsg.equals(oldMsg)) {
			return;
		}

		if (oldMsg == null) {
			newMsg.addSharedTree(sharedTree);
			sharedTree.setMsg(newMsg);
		} else {
			msgs.remove(oldMsg);

			for (RdfTree tree : oldMsg.getTopTrees()) {
				newMsg.addTopTree(tree);
				tree.setMsg(newMsg);
			}
			
			for (RdfTree tree : oldMsg.getSharedTrees().values()) {
				newMsg.addSharedTree(tree);
				tree.setMsg(newMsg);
			}				
		}
	}

}
