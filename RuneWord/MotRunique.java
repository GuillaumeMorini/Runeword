/**
 * 
 */
package RuneWord;

//mots_runiques.csv
//Nom du mot US;Nom du mot Fr;Runes à insérer;niveau requis;Objet nécessaire;Bonus magiques

/**
 * @author U205340
 *
 */
public class MotRunique {
	String nomUS;
	String nomFR;
	String listRunes;
	int nivRequis;
	String typeObjet;
	String bonus;
	
	MotRunique( String lNomUS, String lNomFR, String lListRunes, int lNivRequis, String lTypeObjet, String lBonus ) {
		nomUS = lNomUS;
		nomFR = lNomFR;
		listRunes = lListRunes;
		nivRequis = lNivRequis;
		typeObjet = lTypeObjet;
		bonus = lBonus;
	}
	
	public String toString() {
		return RuneWord.resource.getString( "usName" ) + " : " + nomUS + "\n" + RuneWord.resource.getString( "frName" ) + " : " + nomFR + "\n" + RuneWord.resource.getString( "listRunes" ) + " : " + listRunes + "\n" + RuneWord.resource.getString( "lvlRequired" ) + " : " + nivRequis + "\n" + RuneWord.resource.getString( "objectNeeded" ) + " : " + typeObjet + "\n" + RuneWord.resource.getString( "bonus" ) + " : \n" + bonus + "\n";
	}
}
