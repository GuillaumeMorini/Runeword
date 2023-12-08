package RuneWord;

//runes.csv
//Nom;Effet sur les armes;Effet sur les armures/casques/boucliers;Niveau requis

public class Rune extends Objet {

	public String nom;
	public String effetArmes;
	public String effetAutres;
	public int nivRequis;
	
	Rune( String name, String effetSurArmes, String effetSurAutres, int niveau ) {
		super(name);
		effetArmes = effetSurArmes;
		effetAutres = effetSurAutres;
		nivRequis = niveau;
	}
	
	public String toString() {
		return RuneWord.resource.getString( "name" ) + " : " + nom + "\n" + RuneWord.resource.getString( "effectWeapon" ) + " : " + effetArmes + "\n" + RuneWord.resource.getString( "effectOther" ) + " : " + effetAutres + "\n" + RuneWord.resource.getString( "lvlRequired" ) + " : " + nivRequis + "\n";
	}
}
