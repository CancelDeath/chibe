package org.gvt.model.sifl3;

import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.pattern.miner.SIFInteraction;
import org.biopax.paxtools.pattern.miner.SIFMiner;
import org.biopax.paxtools.pattern.miner.SIFSearcher;
import org.biopax.paxtools.pattern.miner.SIFType;
import org.gvt.model.BioPAXGraph;
import org.gvt.util.EntityHolder;
import org.gvt.util.PathwayHolder;
import org.patika.mada.graph.GraphObject;
import org.patika.mada.graph.Node;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * SIF graphs are not excisable.
 *
 * @author Ozgun Babur
 *
 * Copyright: Bilkent Center for Bioinformatics, 2007 - present
 */
public class SIFGraph extends BioPAXGraph
{
	private List<SIFType> ruleTypes;
	private Set<String> ubiqueIDs;

	public SIFGraph(Model biopaxModel, List<SIFType> ruleTypes, Set<String> ubiqueIDs)
	{
		setBiopaxModel(biopaxModel);
		setGraphType(SIF_LEVEL3);
		this.ruleTypes = ruleTypes;
		this.ubiqueIDs = ubiqueIDs;

		createContents();
	}

	private void createContents()
	{
		Set<SIFInteraction> sifInts = getSimpleInteractions();

		// Map to remember created nodes
		Map<EntityHolder, SIFNode> map = new HashMap<EntityHolder, SIFNode>();

		// Encountered rules. For avoiding duplicate edges.
		Set<String> encountered = new HashSet<String>();

		for (SIFInteraction simpleInt : sifInts)
		{
            if(simpleInt.type != null)
            {
                EntityHolder source = new EntityHolder(simpleInt.source);
                EntityHolder target = new EntityHolder(simpleInt.target);

                if (!map.containsKey(source))
                {
                    map.put(source, new SIFNode(this, source));
                }
                if (!map.containsKey(target))
                {
                    map.put(target, new SIFNode(this, target));
                }
                SIFNode sourceNode = map.get(source);
                SIFNode targetNode = map.get(target);

                String id = source.getID() + " - " + target.getID();

                if (encountered.contains(id))
                {
                    continue;
                }

                new SIFEdge(sourceNode, targetNode, simpleInt.type.getTag());

                encountered.add(id);

                if (!simpleInt.type.isDirected())
                {
                    encountered.add(target.getID() + " - " + source.getID());
                }
            }
		}
	}

	private Set<SIFInteraction> getSimpleInteractions()
	{
		SIFSearcher searcher = new SIFSearcher(ruleTypes.toArray(new SIFType[ruleTypes.size()]));
		searcher.setUbiqueIDs(ubiqueIDs);
		return searcher.searchSIF(biopaxModel);
	}

	/**
	 * Extracts rule types from possible rule classes.
	 * @return possible rule types
	 */
	public static List<SIFType> getPossibleRuleTypes()
	{
		return Arrays.asList(SIFType.values());
	}

	public void write(OutputStream os)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

			for (Object o : getEdges())
			{
				SIFEdge edge = (SIFEdge) o;
				SIFNode source = (SIFNode) edge.getSource();
				SIFNode target = (SIFNode) edge.getTarget();

				writer.write(source.getText() + "\t");
				writer.write(edge.getTag() + "\t");
				writer.write(target.getText() + "\n");
			}

			writer.close();
		}
		catch (IOException e){e.printStackTrace();}
	}

	// Had to implement these methods to make SIF graph a BioPAX graph

	public List<String> getPathwayNames()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public String getPathwayRDFID()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public List<String[]> getInspectable()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public BioPAXGraph excise(Collection<GraphObject> objects, boolean keepHighlights)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	protected void prepareEntityToNodeMap()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void representDataOnActors(String type)
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void removeRepresentations()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public boolean fetchLayout()
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public boolean fetchLayout(String pathwayRDFID)
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void recordLayout()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void forgetLayout()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Set<Node> getRelatedStates(EntityHolder pe)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public Set<Node> getRelatedStates(Collection<EntityHolder> entities)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void replaceComplexMembersWithComplexes(Collection<Node> objects)
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public BioPAXGraph excise(PathwayHolder p)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public PathwayHolder getPathway()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void setPathway(PathwayHolder p)
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void registerContentsToPathway()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
