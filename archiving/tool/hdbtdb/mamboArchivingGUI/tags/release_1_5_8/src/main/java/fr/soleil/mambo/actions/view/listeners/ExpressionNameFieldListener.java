package fr.soleil.mambo.actions.view.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import fr.soleil.mambo.actions.view.VCExpressionApplyAction;
import fr.soleil.mambo.components.view.ExpressionTree;

public class ExpressionNameFieldListener implements DocumentListener
{

	public ExpressionNameFieldListener()
	{
		// nothing, this is just here to have a constructor
	}

	private void textChanged(DocumentEvent arg0)
	{
		if ( arg0 == null )
		{
			return;
		}
		Document doc = arg0.getDocument();
		if ( doc != null && doc.getLength() > 0 )
		{
			try
			{
				if ( !"".equals( doc.getText(0 , doc.getLength()).trim() ) )
				{
                    if (ExpressionTree.getInstance().getSelectedAttribute() != null)
                    {
                        VCExpressionApplyAction.getInstance().setEnabled( true );
                    }
				}
			}
			catch ( BadLocationException e )
			{
				// trace here but should never happen.
				e.printStackTrace();
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent arg0)
	{
		//textChanged(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent arg0)
	{
		textChanged(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent arg0)
	{
		textChanged(arg0);
	}

}
