/**
 * 
 */
package RuneWord;


/**
 * @author U205340
 *
 */
public class SpecialMacHandler implements MRJQuitHandler, MRJPrefsHandler, MRJAboutHandler {

	Program us;
	
	public SpecialMacHandler( Program theProgram ) {
		us = theProgram;
		System.setProperty( "com.apple.macos.useScreenMenuBar", "true" );
		System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "RuneWord" );
		System.setProperty( "com.apple.mrj.application.growbox.intrudes", "false" );
		System.setProperty( "com.apple.macos.smallTabs", "true" );
		MRJApplicationUtils.registerAboutHandler( this );
		MRJApplicationUtils.registerPrefsHandler( this );
		MRJApplicationUtils.registerQuitHandler( this );
	}

	
	
}
