package RuneWord;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Objets {
	
	static final String separateur = ",";
	
	public ArrayList list;
	public int nbObjets;
	RuneWord runeWord;
	
	public void chargeObjets() {
		Objet tmp;
		boolean trouve;
		for (int i=0; i<runeWord.listMotsRuniques.nbMotsRuniques; i++) {
			MotRunique mr = (MotRunique) runeWord.listMotsRuniques.list.get( i );
			String nomObjets = mr.typeObjet;
			StringTokenizer st = new StringTokenizer( nomObjets, separateur );
			while (st.hasMoreTokens()) {
				String nomObjet = st.nextToken();
				Objet objet = new Objet( nomObjet );
				trouve = false;
				for (int j=0; ( j<list.size() && (!trouve) ); j++) {
					tmp = (Objet) list.get( j );
					if ( tmp.nom.compareTo( objet.nom ) == 0 ) {
						trouve = true;
					}
				}
				if ( ! trouve ) {
					int place = nbObjets;
					int k;
					for (k=0; ( place == nbObjets ) && k<nbObjets; k++) {
						tmp = (Objet) list.get(k);
						if ( objet.nom.compareTo( tmp.nom ) < 0 ) {
							//System.out.println( objet.nom + " avant " + tmp.nom + " en place " + k );
							place = k;
						}
					}

					//System.out.println( "Ajout de " + objet.nom + " en place " + place );
					nbObjets++;
					list.ensureCapacity( nbObjets );
					list.add( place, objet );
				}
			}
		}
	}
	
	Objets( RuneWord rw ) {
		runeWord = rw;
		list = new ArrayList();
		nbObjets = 0;
		chargeObjets();
	}
}
