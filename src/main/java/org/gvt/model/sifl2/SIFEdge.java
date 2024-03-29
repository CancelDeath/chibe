package org.gvt.model.sifl2;

import org.biopax.paxtools.io.sif.BinaryInteractionType;
import org.eclipse.swt.graphics.Color;
import org.gvt.model.biopaxl2.BioPAXEdge;
import org.gvt.model.biopaxl2.IBioPAXL2Node;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @author Ozgun Babur
 *
 * Copyright: Bilkent Center for Bioinformatics, 2007 - present
 */
public class SIFEdge extends BioPAXEdge
{
	private String tag;
	private boolean breadthEdge;

	public static Map<String, EdgeType> typeMap;

	public SIFEdge(SIFNode source, SIFNode target, String tag)
	{
		super(source, target);

		this.tag = tag;

		setTooltipText(tag);

		EdgeType et = getType(tag);

		setColor(et.color);

		if (et.directed)
		{
			setArrow("Target");
		}

		if (!et.solid)
		{
			setStyle("Dashed");
		}

		breadthEdge = !getType(tag).noDistance;
	}

	public String getTag()
	{
		return tag;
	}

	public int getSign()
	{
		return getType(tag).sign;
	}

	public boolean isDirected()
	{
		return getType(tag).directed;
	}

	public boolean isBreadthEdge()
	{
		return breadthEdge;
	}

	public List<String[]> getInspectable()
	{
		List<String[]> list = super.getInspectable();
		list.add(new String[]{"Type", tag});
		return list;
	}

	public static class EdgeType
	{
		BinaryInteractionType intType;
		String tag;
		Color color;
		boolean solid;
		boolean directed;
		int sign;
		boolean noDistance;

		private EdgeType(BinaryInteractionType intType, Color color, boolean solid, int sign,
			boolean noDistance)
		{
			this.intType = intType;
			this.tag = intType.getTag();
			this.directed = intType.isDirected();
			this.color = color;
			this.solid = solid;
			this.sign = sign;
			this.noDistance = noDistance;
		}

		private EdgeType(String tag, boolean directed, Color color, boolean solid, int sign,
			boolean noDistance)
		{
			this.tag = tag;
			this.directed = directed;
			this.color = color;
			this.solid = solid;
			this.sign = sign;
			this.noDistance = noDistance;
		}

		public String getTag()
		{
			return tag;
		}

		public boolean isDirected()
		{
			return directed;
		}

		public BinaryInteractionType getIntType()
		{
			return intType;
		}

		public Color getColor()
		{
			return color;
		}

		public boolean isSolid()
		{
			return solid;
		}

		public int getSign()
		{
			return sign;
		}

		public boolean isNoDistance()
		{
			return noDistance;
		}
	}

	private static void addType(EdgeType type)
	{
		typeMap.put(type.tag, type);
	}

	public static EdgeType getType(String tag)
	{
		return typeMap.get(tag);
	}
	
	public static boolean isDirected(String tag)
	{
		return getType(tag).intType.isDirected();
	}

	public String getIDHash()
	{
		return ((IBioPAXL2Node) getSourceNode()).getIDHash() +
			((IBioPAXL2Node) getTargetNode()).getIDHash() + tag;
	}

	private static final boolean SOLID = true;
	private static final boolean DASHED = false;

	static
	{
		typeMap = new HashMap<String, EdgeType>();
		
		addType(new EdgeType(BinaryInteractionType.INTERACTS_WITH,
			new Color(null, 200, 0, 0), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.REACTS_WITH,
			new Color(null, 0, 150, 0), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.IN_SAME_COMPONENT,
			new Color(null, 0, 0, 250), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.COMPONENT_OF,
			new Color(null, 100, 100, 100), DASHED, POSITIVE, true));
		addType(new EdgeType(BinaryInteractionType.STATE_CHANGE,
			new Color(null, 0, 50, 150), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.METABOLIC_CATALYSIS,
			new Color(null, 250, 0, 250), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.SEQUENTIAL_CATALYSIS,
			new Color(null, 150, 50, 150), SOLID, NO_SIGN, false));
		addType(new EdgeType(BinaryInteractionType.CO_CONTROL,
			new Color(null, 100, 100, 0), SOLID, POSITIVE, false));
		addType(new EdgeType(BinaryInteractionType.ACTIVATES,
			new Color(null, 100, 200, 0), SOLID, POSITIVE, false));
		addType(new EdgeType(BinaryInteractionType.INACTIVATES,
			new Color(null, 200, 100, 0), SOLID, NEGATIVE, false));
		addType(new EdgeType(BinaryInteractionType.GENERIC_OF,
			new Color(null, 150, 150, 0), SOLID, POSITIVE, true));
        addType(new EdgeType(BinaryInteractionType.DOWNREGULATE_EXPRESSION,
                new Color(null, 250, 50, 50), SOLID, NEGATIVE, false));
        addType(new EdgeType(BinaryInteractionType.UPREGULATE_EXPRESSION,
                new Color(null, 50, 50, 200), SOLID, POSITIVE, false));


        // Non-Paxtools SIF edges

		addType(new EdgeType("TRANSCRIPTION", true,
			new Color(null, 150, 150, 0), DASHED, NO_SIGN, false));
		addType(new EdgeType("DEGRADATION", true,
			new Color(null, 150, 0, 150), SOLID, NO_SIGN, false));
		addType(new EdgeType("BINDS_TO", false,
			new Color(null, 100, 100, 100), SOLID, NO_SIGN, true));
	}
}
