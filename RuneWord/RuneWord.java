package RuneWord;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


public class RuneWord extends JPanel implements ListSelectionListener, MouseListener, KeyListener {
	
	static final String VERSION = "0.4";
	static final long serialVersionUID = 1;
	
	static final String FICHIER_RUNES = "runes.csv";
	static final String FICHIER_MOTS_RUNIQUES = "mots_runiques.csv";
	
	static String[] nomColonnes = new String[6];
	
	// Fichier Resource
	public static ResourceBundle resource;
	
	// Variables locales
	Runes listRunes;
	MotsRuniques listMotsRuniques;
	Objets listObjets;
	JTable resultTable;
	JCheckBox[] listCheckBoxRunes;
	JCheckBox[] listCheckBoxObjets;
	JSlider niveauSlider;
	JFormattedTextField niveauText;
	
	// MotRunique selectionne
	MotRunique selection;
	
	static boolean isApplication;
	static JApplet monApplet;
	
	public static boolean isMac() {
		return System.getProperty( "mrj.version" ) != null;
	}
	
	public JCheckBox[] createMatrixCheckBox( ArrayList l, JPanel p, MyListener myListener ) {
		// Création du tableau de check box par rune
		JCheckBox[] listCheckBox = new JCheckBox[l.size()+1];
		
		// Mise en forme comme une matrice
		int x = (int) Math.sqrt( l.size() * 1.0 );
		p.setLayout( new GridLayout( x, Math.round((l.size()+x-1)/x) ) );
		
		// Création des check box par rune
		Objet monObjet;
		for (int i=0; i<l.size(); i++ ) {
			monObjet = (Objet) l.get(i);
			listCheckBox[i] = new JCheckBox( monObjet.nom );
			listCheckBox[i].addItemListener( myListener );
			p.add( listCheckBox[i] );
		}
		
		// Création de la check box Tout Selectionner
		listCheckBox[l.size()] = new JCheckBox( resource.getString( "selectAll" ) );
		listCheckBox[l.size()].addItemListener( myListener );
		p.add( listCheckBox[l.size()] );
		
		return listCheckBox;
	}
	
	/**
	 * @param monTitre : Titre de la fenetre
	 */
	RuneWord( boolean isApplication, JApplet monApplet ) {
		// Création du JPanel
		super( new BorderLayout() );
		
		RuneWord.isApplication = isApplication;
		RuneWord.monApplet = monApplet;
		
		// Recuperation du fichier de ressources
		resource = ResourceBundle.getBundle("RuneWord");
		
		// Teste si on est sur MacOS ou sur Windows
		if ( isApplication ) {
			if ( isMac() ) {
				// on est sur MacOS
				try {
					Object[] args = { this };
					Class[] arglist = { RuneWord.class };
					Class mac_class = Class.forName( "SpecialMacHandler" );
					Constructor new_one = mac_class.getConstructor( arglist );
					new_one.newInstance( args );
				}
				catch ( Exception e ) {
					System.err.println( e );
				}
				System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "RuneWord" );
				System.setProperty( "com.apple.mrj.application.growbox.intrudes", "false" );
				System.setProperty( "com.apple.macos.smallTabs", "true" );			
			}
		}
		
		// Chargement du fichier de runes depuis le .jar
		listRunes = new Runes( FICHIER_RUNES );
		
		// Chargement du fichier de mots runiques depuis le .jar
		listMotsRuniques = new MotsRuniques( FICHIER_MOTS_RUNIQUES );
		
		// Chargement des types d'objets présents dans les mots runiques
		listObjets = new Objets( this );
		
		// Creation du contenu de la fenetre
		JScrollPane monPanelB2 = new JScrollPane( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		JTabbedPane monPanelB1 = new JTabbedPane();
		JPanel monPanelH = new JPanel();
		JSplitPane monPanelB = new JSplitPane( JSplitPane.VERTICAL_SPLIT, monPanelB1, monPanelB2 );
		monPanelB.setOneTouchExpandable( true );
		JSplitPane monPanel = new JSplitPane( JSplitPane.VERTICAL_SPLIT, monPanelH, monPanelB );
		monPanel.setOneTouchExpandable( true );
		
		// Ajout d'un label pour le niveau
		JLabel niveauLabel = new JLabel( resource.getString( "level" ) );
		monPanelH.add( niveauLabel );
		
		// Ajout d'un slider pour le niveau
		niveauSlider = new JSlider( 1, 99, 99 );
		niveauSlider.setPaintTicks( true );
		niveauSlider.setMajorTickSpacing( 49 );
		niveauSlider.setPaintLabels( true );
		monPanelH.add( niveauSlider );
		
		// Ajout d'une zone de texte pour le niveau
		/*
		try {
			niveauText = new JFormattedTextField( new MaskFormatter( "##" ) );
		} catch (ParseException e1) {
			e1.printStackTrace();
		}*/
		niveauText = new JFormattedTextField();
		niveauText.setColumns( 2 );
		niveauText.setEditable( true );
		niveauText.setValue( new Integer( niveauSlider.getValue() ) );
		
		// Ajout d'un focusListener pour permettre de tout selectionner
		niveauText.addFocusListener( new FocusListener() {

			public void focusGained(FocusEvent e) {
				JTextField tf = (JTextField) e.getSource();
				tf.selectAll();
			}

			public void focusLost(FocusEvent e) {
				JTextField tf = (JTextField) e.getSource();
				tf.select( 0, 0 );
				tf.postActionEvent();
			}
			
		});
		monPanelH.add( niveauText );
		
		// Mise à jour de la zone de texte quand on bouge le Slider et inversement
		MyListener myListener = new MyListener( this );
		niveauSlider.addChangeListener( myListener );
		niveauText.addActionListener( myListener );

		// Ajout d'un bouton pour la recherche
		JButton searchBouton = new JButton( resource.getString( "search" ) );
		monPanelH.add( searchBouton );
		
		// Ajout d'un bouton "A propos"
		JButton aboutBouton = new JButton( resource.getString( "aboutTitle" ) );
		monPanelH.add( aboutBouton );
		
		// Création et ajout des 2 paneaux dans le JTabbedPane
		JPanel monPanelB11 = new JPanel();
		JPanel monPanelB12 = new JPanel();
		monPanelB1.addTab( resource.getString( "tab1" ), monPanelB11 );
		monPanelB1.addTab( resource.getString( "tab2" ), monPanelB12 );

		// Creation des panneaux Runes et Objets
		listCheckBoxRunes = createMatrixCheckBox( listRunes.list, monPanelB11, myListener );
		listCheckBoxObjets = createMatrixCheckBox( listObjets.list, monPanelB12, myListener );
		
		// Remplissage de la table des mots runiques
		Object[][] data = new Object[listMotsRuniques.nbMotsRuniques][6];
		MotRunique tmpMotRunique;
		for ( int i=0; i<listMotsRuniques.nbMotsRuniques; i++ ) {
			tmpMotRunique = (MotRunique) listMotsRuniques.list.get(i);
			data[i][0] = tmpMotRunique.nomUS;
			data[i][1] = tmpMotRunique.nomFR;
			data[i][2] = tmpMotRunique.listRunes;
			data[i][3] = new Integer( tmpMotRunique.nivRequis );
			data[i][4] = tmpMotRunique.typeObjet;
			data[i][5] = ( tmpMotRunique.bonus ).replace( '$', '\n' );
		}
		
		// Création de la JTable
		nomColonnes[0] = resource.getString( "usName" );
		nomColonnes[1] = resource.getString( "frName" );
		nomColonnes[2] = resource.getString( "listRunes" );
		nomColonnes[3] = resource.getString( "lvlRequired" );
		nomColonnes[4] = resource.getString( "objectNeeded" );
		nomColonnes[5] = resource.getString( "bonus" );
		TableTriee modele = new TableTriee( this, data, nomColonnes );
		resultTable = new JTable( modele );
		modele.addMouseListenerToHeaderInTable( resultTable );
		
		// Parametrage de la JTable
		resultTable.getTableHeader().setReorderingAllowed( false );
		resultTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		
		// Renderer special a la colonne bonus
		TableColumn bonusColumn = resultTable.getColumnModel().getColumn(5);
		bonusColumn.setCellRenderer( new TextRenderer() );
		
		// Ajout de ce qu'on fait quand on selectionne dans la table
		ListSelectionModel listMod =  resultTable.getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMod.addListSelectionListener(this);
		
		// Ajout d'un Mouse Listener sur la JTable
		// En cas de double-click, affiche le mot runique
		resultTable.addMouseListener( this );
		
		// Ajout dans le paneau du bas du Header et de la Table
		monPanelB2.setRowHeaderView( resultTable.getTableHeader() );
		monPanelB2.setViewportView( resultTable );
		
		// Ajout de l'action lorsqu'on appuit sur le bouton de recherche
		searchBouton.addActionListener( myListener );
		
		// Ajout de l'action lorsqu'on appuit sur le bouton A propos
		aboutBouton.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						HTMLEditorKit kit = new HTMLEditorKit();
						HTMLDocument modele = new HTMLDocument();
						JTextPane textPane = new JTextPane( modele );
						textPane.setEditable( false );
						textPane.setEditorKit( kit );
						textPane.addHyperlinkListener( new HyperActive( RuneWord.isApplication, RuneWord.monApplet ) );
						textPane.setText( resource.getString( "aboutMessage" ) );
						JOptionPane.showMessageDialog( RuneWord.this, textPane );
					}
				}
		);
		this.add( monPanel, BorderLayout.CENTER );

		// Fixe la taille des colonnes de la JTable
		resultTable.getColumnModel().getColumn(0).setPreferredWidth( 120 );
		resultTable.getColumnModel().getColumn(1).setPreferredWidth( 120 );
		resultTable.getColumnModel().getColumn(2).setPreferredWidth( 120 );
		resultTable.getColumnModel().getColumn(3).setPreferredWidth(  50 );
		resultTable.getColumnModel().getColumn(4).setPreferredWidth( 150 );
		resultTable.getColumnModel().getColumn(5).setPreferredWidth( 400 );

		// clique sur Select All
		listCheckBoxRunes[listRunes.nbRunes].setSelected( true );
		listCheckBoxObjets[listObjets.nbObjets].setSelected( true );
	}
	
	// Gestion de la souris
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent e){
		if (e.getClickCount() == 2){
			JOptionPane.showMessageDialog( RuneWord.this, selection, resource.getString( "runeWord" ) + " : " + selection.nomFR, JOptionPane.INFORMATION_MESSAGE );
		}
	}
	
	// Gestion du clavier
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		String keyPressed = KeyEvent.getKeyText(keyCode);
		if (keyPressed.equals("Enter")){
			JOptionPane.showMessageDialog( RuneWord.this, selection, resource.getString( "runeWord" ) + " : " + selection.nomFR, JOptionPane.INFORMATION_MESSAGE );
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	public void valueChanged(ListSelectionEvent e) {
		int[] selRows;
		if (!e.getValueIsAdjusting()) {        
			selRows = resultTable.getSelectedRows();
			if (selRows.length > 0) {
				selection = new MotRunique( 
						(String) resultTable.getValueAt( selRows[0], 0 ),
						(String) resultTable.getValueAt( selRows[0], 1 ),
						(String) resultTable.getValueAt( selRows[0], 2 ),
						( (Integer) resultTable.getValueAt( selRows[0], 3 ) ).intValue(),
						(String) resultTable.getValueAt( selRows[0], 4 ),
						(String) resultTable.getValueAt( selRows[0], 5 )
				);
			}
		}
	}
	
	private static void createAndShowGUI() {
		JFrame maFrame = new JFrame( "RuneWord v" + VERSION );
		RuneWord monRuneWord = new RuneWord( true, null );
		maFrame.setContentPane( monRuneWord );
		// Definit les actions liees a la fenetre
		maFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Choisit la position de la fenetre en fonction de la taille
		maFrame.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window = maFrame.getSize();
		//ensure that no parts of maFrame will be off-screen
		if (window.height > screen.height) {
			window.height = screen.height;
		}
		if (window.width > screen.width) {
			window.width = screen.width;
		}
		int xCoord = (screen.width/2 - window.width/2);
		int yCoord = ((screen.height-50)/2 - (window.height)/2);
		maFrame.setLocation( xCoord, yCoord );
		maFrame.setVisible( true );		
	}
	
	public static void main(String[] args) {
		//java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
