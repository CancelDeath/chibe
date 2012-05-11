package org.gvt.action;

import org.biopax.paxtools.model.level3.SimplePhysicalEntity;
import org.biopax.paxtools.model.level3.UnificationXref;
import org.biopax.paxtools.model.level3.Xref;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.gvt.ChisioMain;
import org.gvt.model.NodeModel;
import org.gvt.model.biopaxl3.Actor;
import org.gvt.model.biopaxl3.ChbComplex;
import org.gvt.model.biopaxl3.ChbConversion;

import java.util.*;

/**
 *
 * @author Ozgun Babur
 *
 * Copyright: Bilkent Center for Bioinformatics, 2007 - present
 */
public class DebugButtonAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 */
	public DebugButtonAction(ChisioMain main)
	{
		super("Debug button");
		this.setToolTipText(
			"Debug Button - You can run any\n" +
			"code after pressing this button.\n" +
			"Insert your code in the class\n" +
			"DebugButtonAction");
		setImageDescriptor(ImageDescriptor.createFromFile(getClass(), "../icon/bug.png"));
		this.main = main;
	}

	public void run()
	{
		workOnSelected();
	}

	private void workOnSelected()
	{
		ScrollingGraphicalViewer viewer = main.getViewer();
		Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();

		while (selectedObjects.hasNext())
		{
			Object o = ((EditPart)selectedObjects.next()).getModel();

			if (o instanceof NodeModel)
			{
				NodeModel model = (NodeModel) o;

				if (model instanceof Actor)
				{
					Actor actor = (Actor) model;
					System.out.println(actor.getEntity().l3pe.getDisplayName());
					System.out.println("pe id = " + actor.getEntity().getID());

					for (Xref xref : ((SimplePhysicalEntity)actor.getEntity().l3pe).getEntityReference().getXref())
					{
						if (xref instanceof UnificationXref)
							System.out.println(xref);
					}
				}
				else if (o instanceof ChbComplex)
				{
					ChbComplex cmp = (ChbComplex) o;
					System.out.println(cmp.getEntity().l3pe.getDisplayName());
					System.out.println(cmp.getComplex().getRDFId());
				}
				else if (o instanceof ChbConversion)
				{
					ChbConversion cnv = (ChbConversion) o;
					System.out.println("conversion id = " + cnv.getConversion().getRDFId());
				}
			}
		}

	}

	static class Waiter
	{
		public synchronized static void pause(long time)
		{
			Waiter w = new Waiter();
			w.bekle(time);
		}

		private synchronized void bekle(long time)
		{
			try
			{
				this.wait(time);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}
	}
}