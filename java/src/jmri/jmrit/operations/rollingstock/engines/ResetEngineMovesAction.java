// ResetEngineMovesAction.java

package jmri.jmrit.operations.rollingstock.engines;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


/**
 * This routine will reset the move count for all engines in the operation database.
 * 
 * @author Dan Boudreau Copyright (C) 2012
 * @version $Revision: 17977 $
 */


public class ResetEngineMovesAction extends AbstractAction {
	
	static final ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.operations.rollingstock.engines.JmritOperationsEnginesBundle");
	
	EngineManager manager = EngineManager.instance();
	
    public ResetEngineMovesAction(String actionName, Component frame) {
        super(actionName);
    }
	
	public void actionPerformed(ActionEvent ae) {
		if (JOptionPane.showConfirmDialog(null,
				rb.getString("engineSureResetMoves"), rb.getString("engineResetMovesAll"),
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
			log.debug("Reset moves for all engines in roster");
			manager.resetMoves();
		}
	}

	static org.apache.log4j.Logger log = org.apache.log4j.Logger
	.getLogger(ResetEngineMovesAction.class.getName());
}