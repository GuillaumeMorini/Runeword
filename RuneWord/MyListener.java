package RuneWord;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class MyListener implements ChangeListener, ActionListener, ItemListener {
	
	RuneWord runeWord;
	int value;
	boolean unSelectAllRunes = true;
	boolean unSelectAllObjets = true;
	boolean unSelectAllcac = true;
	boolean unSelectAlltla = true;
	
	MyListener( RuneWord rw ) {
		runeWord = rw;
		value = runeWord.niveauSlider.getValue();
	}
	
	public void stateChanged(ChangeEvent arg0) {
		// Modification du Slider
		value = runeWord.niveauSlider.getValue();
		runeWord.niveauText.setText( "" + value );
		search();
	}
	
	public void itemStateChanged(ItemEvent e) {
		// Modification des cases à cocher
		// Verifie si on coche ou on decoche
		if ( e.getStateChange() == ItemEvent.SELECTED ) {
			// On coche
			if ( e.getSource() == runeWord.listCheckBoxRunes[runeWord.listRunes.nbRunes] ) {
				// Si c'est le Select ALL alors on coche toutes les cases
				for (int i=0; i<runeWord.listRunes.nbRunes; i++ ) {
					runeWord.listCheckBoxRunes[i].setSelected( true );
				}				
			} else {
				if ( e.getSource() == runeWord.listCheckBoxObjets[runeWord.listObjets.nbObjets] ) {
					// Si c'est le Select ALL alors on coche toutes les cases
					for (int i=0; i<runeWord.listObjets.nbObjets; i++ ) {
						runeWord.listCheckBoxObjets[i].setSelected( true );
					}					
				} else {
					associationObjet( e.getSource(), true );
				}
			}
		} else {
			// On decoche
			if ( e.getSource() == runeWord.listCheckBoxRunes[runeWord.listRunes.nbRunes] ) {
				// Si le Select ALL on decoche toutes les cases
				if ( unSelectAllRunes == true ) {
					for (int i=0; i<runeWord.listRunes.nbRunes; i++ ) {
						runeWord.listCheckBoxRunes[i].setSelected( false );
					}
				}
			} else {
				if ( e.getSource() == runeWord.listCheckBoxObjets[runeWord.listObjets.nbObjets] ) {
					// Si le Select ALL on decoche toutes les cases
					if ( unSelectAllObjets == true ) {
						for (int i=0; i<runeWord.listObjets.nbObjets; i++ ) {
							runeWord.listCheckBoxObjets[i].setSelected( false );
						}
					}
				} else {
					boolean trouve = false;
					int i = 0;
					while ( ! trouve && i<runeWord.listRunes.nbRunes ) {
						if ( e.getSource() == runeWord.listCheckBoxRunes[i] ) {
							trouve = true;
						}
						i++;							
					}
					if ( trouve ) {
						// Si c'est une autre case dans les runes, on decoche la case Select ALL
						unSelectAllRunes = false;
						runeWord.listCheckBoxRunes[runeWord.listRunes.nbRunes].setSelected( false );
						unSelectAllRunes = true;
					} else {
						// Si c'est une autre case dans les objets, on decoche la case Select ALL
						unSelectAllObjets = false;
						runeWord.listCheckBoxObjets[runeWord.listObjets.nbObjets].setSelected( false );
						unSelectAllObjets = true;
						associationObjet( e.getSource(), false );
					}
				}
			}
		}
		search();
	}
	
	public void actionPerformed(ActionEvent ae) {
		// Modification du label de niveau
		if ( ae.getSource() == runeWord.niveauText ) {
			try {
				value = (int) Integer.parseInt( runeWord.niveauText.getText() );
			} catch (NumberFormatException e) {
				//e.printStackTrace();
			}
			runeWord.niveauSlider.setValue( value );
		}
		if ( runeWord.niveauText.isFocusOwner() ) {
			runeWord.niveauText.transferFocus();
		}
		search();
	}
	
	public void search() {
		String mesRunes = "";
		// Creation de la liste des runes
		for  ( int i=0; i<runeWord.listRunes.nbRunes; i++ ) {
			if ( runeWord.listCheckBoxRunes[i].isSelected() ) {
				if ( mesRunes != "" ) {
					mesRunes += " " + runeWord.listCheckBoxRunes[i].getText();
				} else {
					mesRunes += runeWord.listCheckBoxRunes[i].getText();
				}
			}
		}
		
		// Recherche des Mots Runiques possibles avec ma liste de runes
		int nb = 0;
		for  ( int i=0; i<runeWord.listMotsRuniques.nbMotsRuniques; i++ ) {
			if ( valideRunesEtLevel( mesRunes, i ) && valideObjets( i ) ) {
				nb++;
			}
		}
		
		// Remplissage de la table des mots runiques
		Object[][] data = new Object[nb][6];
		nb = 0;
		for ( int i=0; i<runeWord.listMotsRuniques.nbMotsRuniques; i++ ) {
			if ( valideRunesEtLevel( mesRunes, i ) && valideObjets( i ) ) {
				MotRunique tmpMotRunique = (MotRunique) runeWord.listMotsRuniques.list.get(i);
				data[nb][0] = tmpMotRunique.nomUS;
				data[nb][1] = tmpMotRunique.nomFR;
				data[nb][2] = tmpMotRunique.listRunes;
				data[nb][3] = new Integer( tmpMotRunique.nivRequis );
				data[nb][4] = tmpMotRunique.typeObjet;
				data[nb][5] = ( tmpMotRunique.bonus ).replace( '$', '\n' );
				nb++;
			}
		}
		TableTriee modele = (TableTriee) runeWord.resultTable.getModel();
		modele.setData( data );
		modele.sort();
	}
	
	public void associationObjet( Object o, boolean select ) {		
		boolean trouveObjet = false;
		int place = 0;
		int i = 0;
		if ( unSelectAlltla && unSelectAllcac ) {
			while ( ! trouveObjet && i<runeWord.listObjets.nbObjets ) {
				if ( o == runeWord.listCheckBoxObjets[i] ) {
					trouveObjet = true;
					place = i;
				}
				i++;							
			}
			if ( trouveObjet ) {
				if ( runeWord.listCheckBoxObjets[place].getText().compareTo( "Toutes les armes" ) == 0 ) {
					for (int j=0; j<runeWord.listObjets.nbObjets; j++) {
						if ( runeWord.listCheckBoxObjets[j].getText().compareTo( "Arme de corps à corps" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Arme à distance" ) == 0
						) {
							runeWord.listCheckBoxObjets[j].setSelected( select );
						}
					}
				}
				
				if ( runeWord.listCheckBoxObjets[place].getText().compareTo( "Arme de corps à corps" ) == 0 ) {
					for (int j=0; j<runeWord.listObjets.nbObjets; j++) {
						if ( runeWord.listCheckBoxObjets[j].getText().compareTo( "Epée" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Baguette" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Bâton" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Griffes" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Hache" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Hache d'arme" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Masse" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Massue" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Marteau" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Marteau" ) == 0
								|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Sceptre" ) == 0
								|| ( runeWord.listCheckBoxObjets[j].getText().compareTo( "Toutes les armes" ) == 0 && !select )
						) {
							unSelectAlltla = false;
							runeWord.listCheckBoxObjets[j].setSelected( select );
							unSelectAlltla = true;
						}
					}
				}
				
				if ( runeWord.listCheckBoxObjets[place].getText().compareTo( "Bouclier" ) == 0 ) {
					for (int j=0; j<runeWord.listObjets.nbObjets; j++) {
						if ( runeWord.listCheckBoxObjets[j].getText().compareTo( "Bouclier de paladin" ) == 0
						) {
							runeWord.listCheckBoxObjets[j].setSelected( select );
						}
					}
				}
				
				if ( runeWord.listCheckBoxObjets[place].getText().compareTo( "Epée" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Baguette" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Bâton" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Griffes" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Hache" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Hache d'arme" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Masse" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Massue" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Marteau" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Marteau" ) == 0
						|| runeWord.listCheckBoxObjets[place].getText().compareTo( "Sceptre" ) == 0
				) {
					if ( !select ) {
						for (int j=0; j<runeWord.listObjets.nbObjets; j++) {
							if ( runeWord.listCheckBoxObjets[j].getText().compareTo( "Arme de corps à corps" ) == 0
									|| runeWord.listCheckBoxObjets[j].getText().compareTo( "Toutes les armes" ) == 0
							) {
								unSelectAllcac = false;
								runeWord.listCheckBoxObjets[j].setSelected( select );
								unSelectAllcac = true;
							}
						}					
					}
				}				
			}
		}
	}
	
	boolean valideRunesEtLevel( String mesRunes, int i ) {
		// Verifie les mots runiques valide avec le niveau et les runes cochées
		boolean b = true;
		MotRunique mr = ( (MotRunique) runeWord.listMotsRuniques.list.get(i) );
		String tmp = mr.listRunes;
		StringTokenizer st = new StringTokenizer( tmp, " " );
		do {
			b = b && ( mesRunes.indexOf( st.nextToken() ) != -1 );
			b = b && ( mr.nivRequis <= runeWord.niveauSlider.getValue() );
		} while ( b && st.hasMoreElements() );
		return b;
		
	}
	
	boolean valideObjets( int i ) {
		// Verifie les mots runiques valide avec les objets cochés
		boolean trouve = false;
		Objet obj;
		MotRunique mr = ( (MotRunique) runeWord.listMotsRuniques.list.get(i) );		
		int j = 0;
		while ( ! trouve && j < runeWord.listObjets.nbObjets ) {
			if ( runeWord.listCheckBoxObjets[j].isSelected() ) {
				obj = (Objet) runeWord.listObjets.list.get( j );
				trouve = ( mr.typeObjet.indexOf( obj.nom ) != -1 );
				if ( trouve ) {
					if ( mr.typeObjet.length() != mr.typeObjet.indexOf( obj.nom ) + obj.nom.length() ) {
						trouve = mr.typeObjet.charAt( mr.typeObjet.indexOf( obj.nom ) + obj.nom.length() ) == ',';
					}
				}

				// Intègre les mots runiques génériques aux objets spécifiques
				// Pour les armes de corps à corps
				if ( !trouve && ( mr.typeObjet.compareTo( "Arme de corps à corps" ) == 0 || mr.typeObjet.compareTo( "Toutes les armes" ) == 0 ) ) {
					trouve = obj.nom.compareTo( "Epée" ) == 0
						|| obj.nom.compareTo( "Baguette" ) == 0
						|| obj.nom.compareTo( "Bâton" ) == 0
						|| obj.nom.compareTo( "Griffes" ) == 0
						|| obj.nom.compareTo( "Hache" ) == 0
						|| obj.nom.compareTo( "Hache d'arme" ) == 0
						|| obj.nom.compareTo( "Masse" ) == 0
						|| obj.nom.compareTo( "Massue" ) == 0
						|| obj.nom.compareTo( "Marteau" ) == 0
						|| obj.nom.compareTo( "Marteau" ) == 0
						|| obj.nom.compareTo( "Sceptre" ) == 0;
				}

				// Pour les armes à distance
				if ( !trouve && mr.typeObjet.compareTo( "Toutes les armes" ) == 0 ) {
					trouve = obj.nom.compareTo( "Arme à distance" ) == 0;
				}

				// Pour les boucliers
				if ( !trouve && mr.typeObjet.compareTo( "Bouclier" ) == 0 ) {
					trouve = obj.nom.compareTo( "Bouclier de paladin" ) == 0;
				}

			}
			j++;
		}
		return trouve;
	}
}