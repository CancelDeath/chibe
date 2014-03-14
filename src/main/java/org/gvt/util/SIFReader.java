package org.gvt.util;

import org.biopax.paxtools.pattern.miner.SIFType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.gvt.model.CompoundModel;
import org.gvt.model.NodeModel;
import org.gvt.model.sifl3.SIFEdge;
import org.gvt.model.basicsif.BasicSIFEdge;
import org.gvt.model.basicsif.BasicSIFGraph;
import org.gvt.model.basicsif.BasicSIFNode;
import org.gvt.ChisioMain;
import org.patika.mada.util.XRef;

import java.io.*;
import java.util.*;

/**
 * This class is extended from XMLReader but it reads txt files. This is because the class XMLReader
 * is incorrectly named. It does nothing about xml files, it should have been named as FileReader.
 *
 * @author Ozgun Babur
 */
public class SIFReader
{
	private List<? extends SIFType> enumTypes;
	private Set<String> types;
	private Map<String, BasicSIFNode> nodeMap;
	private Set<String> relationsSet;
	private BasicSIFGraph root;
	private String delim;
	private boolean useGroups;

	private boolean duplicatesExist;

	public SIFReader()
	{
		nodeMap = new HashMap<String, BasicSIFNode>();
		relationsSet = new HashSet<String>();
		useGroups = true;
	}

	public SIFReader(List<? extends SIFType> enumTypes)
	{
		this();
		this.enumTypes = enumTypes;
	}

	public void setUseGroups(boolean useGroups)
	{
		this.useGroups = useGroups;
	}

	public CompoundModel readXMLFile(File sifFile)
	{
		if (enumTypes != null)
		{
			types = new HashSet<String>();

			for (SIFType enumType : enumTypes)
			{
				types.add(enumType.getTag());
			}
		}
		try
		{
			duplicatesExist = false;
			root = new BasicSIFGraph();

			String filename = sifFile.getName();
			root.setName(filename.substring(0, filename.indexOf(".sif")));
			
			root.setAsRoot();

			delim = fileContainsTab(sifFile) ? "\t\n\r\f" : " \n\r\f";

			BufferedReader reader = new BufferedReader(new FileReader(sifFile));
			for (String line = reader.readLine(); line != null; line = reader.readLine())
			{
				if (line.trim().length() > 0)
				{
					processLine(line);
				}
			}

			reader.close();

			if (duplicatesExist)
			{
//				root.write(new FileOutputStream(sifFile));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MessageBox messageBox = new MessageBox(
				new Shell(),
				SWT.ERROR_UNSUPPORTED_FORMAT);
			messageBox.setMessage("File cannot be loaded!");
			messageBox.setText(ChisioMain.TOOL_NAME);
			messageBox.open();

			return null;
		}

		System.out.println("SIF view contains " + root.getNodes().size() + " nodes and " +
			root.getEdges().size() + " edges.");

		// Format the SIF view if an additional format file exists

		String formatFile = sifFile.getPath().substring(0, sifFile.getPath().lastIndexOf(".")) + ".format";

		boolean group = useGroups;
		if (new File(formatFile).exists())
		{
			group = formatView(formatFile) && useGroups;
		}

		if (group && Conf.getBoolean(Conf.USE_SIF_GROUPING)) root.groupSimilarNodes();

		return root;
	}

	private void processLine(String line)
	{
		StringTokenizer st = new StringTokenizer(line, delim);

		String first = st.nextToken();

		if (st.hasMoreTokens())
		{
			String relation = st.nextToken();

			if (st.hasMoreTokens())
			{
				String second = st.nextToken();

				String meds = st.hasMoreTokens() ? st.nextToken() : null;

				// commented out because we want to display non-paxtools sifs
				if (types == null || types.contains(relation))
				{
					createUnit(first, relation, second, meds);
				}
			}
		}
		else
		{
			getNode(first);
		}
	}

	private void createUnit(String first, String relation, String second, String medIDs)
	{
		// Stop if relation is unfamiliar
		if (!SIFEdge.typeMap.containsKey(relation)) return;

		String relStr = first + "\t" + relation + "\t" + second;
		String revStr = second + "\t" + relation + "\t" + first;

		if (relationsSet.contains(relStr) ||
			(!SIFEdge.typeMap.get(relation).isDirected() &&
				relationsSet.contains(revStr)))
		{
			duplicatesExist = true;
			return;
		}
		else
		{
			relationsSet.add(relStr);
		}

		BasicSIFNode node1 = getNode(first);
		BasicSIFNode node2 = getNode(second);
		new BasicSIFEdge(node1, node2, relation, medIDs);
	}

	private BasicSIFNode getNode(String id)
	{
		if (!nodeMap.containsKey(id))
		{
			BasicSIFNode node = new BasicSIFNode(root, id, id);
			nodeMap.put(id, node);
		}

		return nodeMap.get(id);
	}

	private boolean fileContainsTab(File filename) throws IOException
	{
		boolean contains = false;

		BufferedReader reader = new BufferedReader(new FileReader(filename));

		int i;
		while ((i = reader.read()) != -1)
		{
			if (i == '\t')
			{
				contains = true;
			}
		}
		reader.close();
		return contains;
	}

	private boolean formatView(String formatFile)
	{
		boolean group = useGroups;
		try
		{
			List<String> lines = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(formatFile));

			for (String line = reader.readLine(); line != null; line = reader.readLine())
			{
				if (line.startsWith("graph\tgrouping"))
				{
					useGroups = line.endsWith("\ton");
				}
				else
				{
					lines.add(line);
				}
			}

			reader.close();

			root.format(lines);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return group;
	}
}