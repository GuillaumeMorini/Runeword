package RuneWord;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HyperActive implements HyperlinkListener {
	
	boolean isApplication;
	JApplet monApplet;
	
	HyperActive( boolean isApplication, JApplet monApplet ) {
		this.isApplication = isApplication;
		this.monApplet = monApplet;
	}
	
	public void hyperlinkUpdate(HyperlinkEvent event) {
		HyperlinkEvent.EventType typ = event.getEventType();
		if ( typ == HyperlinkEvent.EventType.ACTIVATED ) {
			try {
				String url = event.getURL().toString();
				if ( ! isApplication ) {
					try	{
						monApplet.getAppletContext().showDocument( new URL(url), "_top" );
					}
					catch (MalformedURLException e) {
						e.printStackTrace();
					} 
				} else {
					if ( RuneWord.isMac() == true ) {
						// On est sur Mac
						Process p =	Runtime.getRuntime().exec( "open " + url );
						try {
							p.waitFor();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println( "code retour " + p.exitValue() );
					} else  {
						// On est pas sur Mac
						Properties sys = System.getProperties();
						String os = sys.getProperty("os.name");
						System.out.println( "OS: " + os );
						if ( os.startsWith( "Windows" ) ) {
							/*
							 if (os.endsWith("NT")||os.endsWith("2000")||os.endsWith("XP"))
							 */					
							Process p = Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + url );
							try {
								p.waitFor();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println( "code retour " + p.exitValue() );							
						} else {
							if ( os.startsWith( "Linux" ) ) {
								System.out.println( "Type d'OS " + os );
								Process p = Runtime.getRuntime().exec( "open " + url );
								try {
									p.waitFor();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								System.out.println( "code retour " + p.exitValue() );
								if ( p.exitValue() != 0 ) {
									Process p2 = Runtime.getRuntime().exec( "mozilla " + url );
									try {
										p2.waitFor();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									System.out.println( "code retour 2 " + p2.exitValue() );
									if ( p2.exitValue() != 0 ) {
										Process p3 = Runtime.getRuntime().exec( "konqueror " + url );
										try {
											p3.waitFor();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										System.out.println( "code retour 3 " + p3.exitValue() );
									}
									
								}
							}
						}
					}
				}
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}	
	}
}
