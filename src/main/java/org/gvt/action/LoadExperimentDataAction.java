package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.gvt.ChisioMain;
import org.patika.mada.dataXML.ChisioExperimentData;
import org.patika.mada.dataXML.Row;
import org.patika.mada.dataXML.impl.ReferenceImpl;
import org.patika.mada.gui.ExperimentDataConvertionWizard;
import org.patika.mada.util.XRef;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Ozgun Babur
 *
 * Copyright: Bilkent Center for Bioinformatics, 2007 - present
 */
public class LoadExperimentDataAction extends Action
{
	ChisioMain main;
	String filename;
	boolean local;

	private static String lastLocation = "experiments/";

	public LoadExperimentDataAction(ChisioMain main, String filename)
	{
		this.main = main;
		this.filename = filename;
		this.local = true;
	}
	
	/*	UK: Added to support opening CED files via URL protocol; useful/needed for JWS environment	*/	
	public LoadExperimentDataAction(ChisioMain main, URL fileURL) throws IOException{
		this.main = main;
		this.filename = fileURL.toString();
		this.local = false;
	}

	public LoadExperimentDataAction(ChisioMain main)
	{
		super("Load ChiBE Formatted Data ...");
		setImageDescriptor(ImageDescriptor.createFromFile(ChisioMain.class, "icon/open-cbe-formatted.png"));
		setToolTipText(getText());
		this.main = main;
		this.local = true;
	}
	
	
	public void run()
	{
		if (filename == null)
		{
			filename = chooseFile();
		}

		if (filename != null && hasValidExtension(filename))
		{
			try
			{
				this.main.lockWithMessage("Loading experiment data ...");
				JAXBContext jc = JAXBContext.newInstance("org.patika.mada.dataXML");

				Unmarshaller u = jc.createUnmarshaller();
				ChisioExperimentData data;
				
				/*	UK: use appropriate method of urmarshaling */
				if (this.local)
					data = (ChisioExperimentData) u.unmarshal(new File(filename));
				else 
					data = (ChisioExperimentData) u.unmarshal(new URL(filename));

                // A mapping between reference names in graph and in data is the prerequisite of loading the data.
                // Otherwise, a warning is displayed to inform the user about the lack of this matching.

                if(referenceMatch(data))
                {
				    main.setExperimentData(data, filename);

				    // Apply coloring on the current view if exists
				    new ColorWithExperimentAction(main, null, data.getExperimentType()).run();
                }
                else
                {
                    MessageDialog.openWarning(null,
                        "No data!",
                         "There is no external reference matching between graph and data.\n" +
                             "Please make sure your BioPAX model contains proper external references " +
                             "that matches the references in expression data annotation.");
                }
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
			}
			finally
			{
				this.main.unlock();
			}
		}
		filename = null;
	}

	/**
	 * opens a FileChooser for loading an xml file
	 *
	 * @return chosen filename
	 */
	public String chooseFile()
	{
		// choose an input file.
		FileDialog fileChooser = new FileDialog(main.getShell(), SWT.OPEN);
		fileChooser.setFilterExtensions(FILTER_EXTENSIONS);
		fileChooser.setFilterNames(FILTER_NAMES);

		fileChooser.setFilterPath(lastLocation);

		String f = fileChooser.open();

		String x = null;
		if (f != null)
		{
			if (f.contains("/"))
			{
				x = f.substring(0, f.lastIndexOf("/"));
			}
			else if (f.contains("\\"))
			{
				x = f.substring(0, f.lastIndexOf("\\"));
			}
		}

		if (x != null) lastLocation = x;

		return f;
	}

    /**
     * Overlap of reference names between graph and loaded data is checked.
     *
     */
    public boolean referenceMatch(ChisioExperimentData data)
    {
        boolean isMatching = false;

        // references found in the graph
        ArrayList<String> graphRef = new ArrayList<String>(XRef.getDBSet());

        // reference mapping from reference.txt
        Map<List<String>,String> referenceList = ExperimentDataConvertionWizard.getKnownReferenceSetsMap();

        // references found in loaded .ced file
        Set<String> cedRef = new HashSet<String>();

        // As all reference names will be same for each row, only the first one is used to obtain them from the data file.

        Row row = (Row)data.getRow().get(0);
        for (int i = 0; i < row.getRef().size(); i++)
        {
            cedRef.add(((ReferenceImpl)(row.getRef().get(i))).getDb());
        }

        for (String ref : graphRef)
        {
            String refTxt = null;

            // Reference names in the graph is mapped to the values in referenceList to make them comparable with
            // cedRef, which are obtained from referenceList.

            for (List<String> strings : referenceList.keySet())
            {
                if (strings.contains(ref))
                {
                    refTxt = referenceList.get(strings);
                    break;
                }
            }

            // One matching reference is enough.
            if(cedRef.contains(refTxt))
            {
                isMatching = true;
                break;
            }
        }

        return isMatching;
    }

	/**
	 * Checks if the file name is a valid chisio experiment file name.
	 *
	 * @param path of the file
	 * @return true if file has a valid chisio experiment file name
	 */
	public static boolean hasValidExtension(String path)
	{
		return path.endsWith(".ced");
	}

	public static final String[] FILTER_EXTENSIONS = new String[]{"*.ced"};

	public static final String[] FILTER_NAMES = new String[]{"ChiBE Experiment Data (*.ced)"};
}
