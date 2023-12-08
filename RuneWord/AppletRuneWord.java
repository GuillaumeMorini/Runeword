package RuneWord;

import javax.swing.JApplet;

public class AppletRuneWord extends JApplet {

	private static final long serialVersionUID = 1L;

	  /**
	   *  create GUI
	   *  @param tree model
	   */
	  private void createGUI() {
	    // set main panel
	    getContentPane().add( new RuneWord( false, this ) ); 
	 }

	  //----------------------------------------------------------------------------
	  // Public methods
	  //----------------------------------------------------------------------------

	  /**
	   *  The applet is loaded (unused at this time)
	   *  @return nothing
	   */
	  public void init() {
	    // create GUI components
	    createGUI();
	  }

	  /**
	   *  The applet starts
	   *  @return nothing
	   */
	  public void start() {
	  }

	  /**
	   *  The applet stops (unused at this time)
	   *  @return nothing
	   */
	  public void stop() {
	  }

	  /**
	   *  The applet is unloaded (unused at this time)
	   *  @return nothing
	   */
	  public void destroy() {
	  }

}
