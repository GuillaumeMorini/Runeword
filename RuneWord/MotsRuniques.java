/**
 * 
 */
package RuneWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author U205340
 *
 */
public class MotsRuniques {
	
	static final String DELIMITEUR = ";"; 

	public ArrayList list;
	public int nbMotsRuniques;

	MotsRuniques() {
		list = new ArrayList();
		nbMotsRuniques = 0;
	}

	boolean chargeFichier( String nomfic ) {
		String tampon;
		int nb = 0;
		try {
			InputStream is = getClass().getResource( "/" + nomfic ).openStream();
			BufferedReader aLire = new BufferedReader( new InputStreamReader ( is ) );
			// 1ere lecture
			do {
				tampon = aLire.readLine();
				if ( tampon != null ) {
					nbMotsRuniques ++;
				}
			} while ( tampon != null );
			// Retour au debut du fichier
			aLire.close();
			// 2eme lecture
			is = getClass().getResource( "/" + nomfic ).openStream();
			aLire = new BufferedReader( new InputStreamReader ( is ) );
			list.ensureCapacity( nbMotsRuniques );
			do {
				tampon = aLire.readLine();
				if ( tampon != null ) {
					StringTokenizer st = new StringTokenizer( tampon, DELIMITEUR );
					if ( st.countTokens() != 6 ) {
						System.err.println( RuneWord.resource.getString( "errorNbChamp1" ) + " 6 " + RuneWord.resource.getString( "errorNbChamp2" ) + " " + st.countTokens() );
						return false;
					}
					MotRunique tmpMotRunique = new MotRunique( st.nextToken(), st.nextToken(), st.nextToken(), Integer.parseInt( st.nextToken() ), st.nextToken(), st.nextToken() );
					list.add( nb++, tmpMotRunique );
				}
			} while ( tampon != null );
			// Fermeture du fichier
			aLire.close();
			return true;
		}
		catch (IOException e) {
			System.out.println( RuneWord.resource.getString( "errorFile" ) + " " + e );
			e.printStackTrace();
			return false;
		}
	}
	
	MotsRuniques( String nomfic ) {
		this();
		boolean b = chargeFichier( nomfic );
		if ( b == false ) {
			System.out.println( RuneWord.resource.getString( "errorFile" ) + " " + RuneWord.class.getClassLoader().getResource( nomfic ).getFile() );
			System.exit( 1 );
		}
	}
}

