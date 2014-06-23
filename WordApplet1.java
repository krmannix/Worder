import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import java.util.*;

/***************************************/
// Created by: Kevin Mannix
// kmannix@bu.edu, Fall 13
// This Java Applet parses text files of 
// words and phonic sounds and allows 
// users to search through these lists
// by choosing various criteria
/**************************************/

public class WordApplet1 extends JApplet implements TextListener {
  
  // Global variables: These labels are the default labels
  // that will be shown on the loading of the applet
  Label appletTitle = new Label("Phonic Words");
  JLabel bottomWord = new JLabel("None checked", null, JLabel.CENTER);
  // Global variables: contains all the words and sounds 
  // that have been chosen by the user. The counters make sure
  // we index correctly and don't overflow or underflow
  String[] addedWords;
  String[] addedSounds;
  int addedWordsCounter = 0;
  int addedSoundsCounter = 0;
  
  // Create black line border for use in all the JPanels  
  Border blackLineBorder = BorderFactory.createLineBorder(Color.black);
  String returnedWords = "";
  JTextArea outputText;
  JPanel showLimiters;
  JComboBox selectBrackets;
  JButton letterButton, searchButton, deleteButton;  
  ButtonGroup typeOfSound;
  JRadioButton ipaButton;
  JRadioButton soundcodeButton;
  TextField enterLetter;
  JList list;
  DefaultListModel<String> listModel;
  CreateSoundList sound; // Our two variables that will be need to get the Sound List to fill the brackets
  ArrayList<ArrayList<String>> soundList; 
  MutableComboBoxModel<String> combomodel;
  ArrayList<Word> wordlist;
  CreateWordList makeWordList;
  JTabbedPane tabbedPane;
  String whichList;
  TextField enterSyllables; // To choose number of syllables
  
  /**************************************************************************************/
  // APPLET SET-UP //
  public void init() {
    setup_layout();
    
    // Create sound list
    setUpSoundList();
    setUpWordList(); 
  }
  
  private void setup_layout() {
    addedWords = new String[20];
    addedSounds = new String[20];
    setLayout(new BorderLayout());
    add(combinePanels(makeMainPanel(), makeHelpPanel()));
    
  }
  
  private JPanel combinePanels(JPanel main, JPanel help) {
    JPanel paneView = new JPanel(); // Create panel to hold the tabs
    JTabbedPane tabbedPane = new JTabbedPane();
    ImageIcon icon = null;
    paneView.setLayout(new BorderLayout()); // Put the tabbedPane panel in the center of Pane View
    tabbedPane.addTab("Main", icon, main, "Phonic Page");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);          
    tabbedPane.addTab("Help", icon, help, "Help Page");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_1);
    
    //Add the tabbed pane to this panel
    paneView.add(tabbedPane);
    
    //The following line enables to use scrolling tabs
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); 
    return paneView;
  }
  
  /**************************************************************************************/
  /**************************************************************************************/
  
  
  /**************************************************************************************/
  // WORD & SOUND SET-UP //
  
  private void setUpSoundList() {
    // Parse through sound list and receive 
    // the ArrayList
    try {
      sound = new CreateSoundList();
    } catch (Exception e) {
      System.out.println("IO Exception making sound list"); 
    }
    soundList = sound.getSoundList();
  }
  
  private void setUpWordList() {
    // Parse through word list and receive 
    // the ArrayList
    try {
      makeWordList = new CreateWordList();
    } catch (Exception e) { 
      System.out.println("IO Exception making word list");
    }
    wordlist = makeWordList.getWordList();
  }
  
  /**************************************************************************************/
  /**************************************************************************************/
  
  
  
  /********************************************************************************/
  // THE MAIN LAYOUT SETUP //
  
  private JPanel makeMainPanel() {   
    JPanel mainLayout = new JPanel();
    mainLayout.setLayout(new BorderLayout());
    //  Set all the panels up in the layout
    mainLayout.add(topBlock(), BorderLayout.NORTH);
    mainLayout.add(rightBlock(), BorderLayout.EAST);
    mainLayout.add(centerBlock(), BorderLayout.CENTER);
    mainLayout.add(lowBlock(), BorderLayout.SOUTH); 
    return mainLayout;
  }
  
  public JPanel topBlock() {
    // Panel for the top block
    JPanel topDisplay = new JPanel();
    topDisplay.setBorder(blackLineBorder);
    topDisplay.add(appletTitle);   
    return topDisplay;
  }
  
  public JPanel rightBlock() {
    // Panel for the right block
    JPanel rightDisplay = new JPanel();
    rightDisplay.setLayout(new BorderLayout());
    rightDisplay.setBorder(blackLineBorder);
    
    JPanel letterAndBracket = new JPanel(new GridLayout(0,1));
    JPanel letterAndBracketTop = new JPanel(new FlowLayout());
    JPanel letterAndBracketBottom = new JPanel(new FlowLayout());
    
    // Create a list (where the returned words will go)
    listModel = new DefaultListModel<String>();
    list = new JList<String>(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    JScrollPane listScroller = new JScrollPane(list);
    listScroller.setPreferredSize(new Dimension(50, 80));
    
    // Create options box for sound choices
    selectBrackets = new JComboBox();
    selectBrackets.setPreferredSize(new Dimension(50, 20));
    combomodel = (MutableComboBoxModel) selectBrackets.getModel();
    
    // Add button actions
    letterButton = new JButton(new AbstractAction("Add") {
      @Override
      public void actionPerformed( ActionEvent e ) {
        addButtonClicked(e);
      }
    });  
    
    // Search button actions
    searchButton = new JButton(new AbstractAction("Search") {
      @Override
      public void actionPerformed( ActionEvent e ) {
       // ExpandSoundList checking = new ExpandSoundList();
        searchButtonClicked(e);
      }
    });
    
    // delete button actions
    deleteButton = new JButton(new AbstractAction("Delete") {
      @Override 
      public void actionPerformed(ActionEvent e) {
        deleteButtonClicked(e);
      }        
    });     
    
    // Search and delete buttons are added to bottom panel
    JPanel searchButtonPanel = new JPanel(new FlowLayout());
    searchButtonPanel.add(deleteButton);
    searchButtonPanel.add(searchButton);
    
    // set soundcode actions
    soundcodeButton = new JRadioButton(new AbstractAction("Sound Code") {
      @Override
      public void actionPerformed( ActionEvent e ) {
        whichList = "Sound Code";
      }
    }); 
    
    // Make soundcode the default
    soundcodeButton.setSelected(true);
    whichList = "Sound Code";
    
    // set ipaButton actions
    ipaButton = new JRadioButton(new AbstractAction("IPA") {
      @Override
      public void actionPerformed( ActionEvent e ) {
        whichList = "IPA";
      }
    });  
    
    
    // Create text field for user input
    enterLetter = new TextField(6);
    enterLetter.addTextListener(this);
    
    // Add the text box, drop-down menu, and Add button
    letterAndBracketTop.add(enterLetter);
    letterAndBracketTop.add(selectBrackets);
    letterAndBracketTop.add(letterButton);
    
    // Add the radio buttons
    typeOfSound = new ButtonGroup();
    typeOfSound.add(soundcodeButton);
    typeOfSound.add(ipaButton);
    letterAndBracketBottom.add(soundcodeButton);
    letterAndBracketBottom.add(ipaButton);
    
    // Add the radio buttons and search bar together
    letterAndBracket.add(letterAndBracketTop);
    letterAndBracket.add(letterAndBracketBottom);
    
    
    // Add all panels to the main display
    rightDisplay.add(letterAndBracket, BorderLayout.NORTH);
    rightDisplay.add(listScroller, BorderLayout.CENTER);
    rightDisplay.add(searchButtonPanel, BorderLayout.SOUTH);
    return rightDisplay;
  }
  
  public JPanel centerBlock() {
    // Panel for the center block
    JPanel centerDisplay = new JPanel();    
    centerDisplay.setLayout(new BorderLayout());
    centerDisplay.setBorder(blackLineBorder);
    
    
    // CREATING OUR TEXT BOX TO DISPLAY THE WORDS
    outputText = new JTextArea("");
    outputText.setEditable(false); // This will make it so the user cannot remove words from the box
    outputText.setLineWrap(true); // The lines will wrap when they extend past the horizontal boundaries
    outputText.setWrapStyleWord(true);
    //JScrollPane scroll = new JScrollPane(outputText);
    JScrollPane scroll = new JScrollPane(outputText);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    centerDisplay.add(scroll);
    return centerDisplay;
  }
  
  public JPanel lowBlock() {
    // Panel for the lower block
    JPanel lowDisplay = new JPanel();
    Font f = bottomWord.getFont();
    bottomWord.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
    lowDisplay.setPreferredSize(new Dimension(400, 30)); // Arbitrary size for with, but we want a little height
    lowDisplay.setLayout(new BorderLayout());
    lowDisplay.setBorder(blackLineBorder);   
    lowDisplay.add(bottomWord, BorderLayout.CENTER);
    return lowDisplay;
  }
  
  /**************************************************************************************/
  /**************************************************************************************/   
  
  
  
  /**************************************************************************************/
  // BUTTON SET-UP (FOR MAIN PANEL) // 
  
  private void addButtonClicked(ActionEvent e) {
    addWordToList();
  }
  
  private void addWordToList() {
    boolean alreadyAdded = false;
    try {
    String selected = (String) selectBrackets.getSelectedItem(); // Get the sound of the letter
    String letter = enterLetter.getText(); // Get the letter they want to add
    if (!letter.equals("") && (!selected.equals("")) && (addedWordsCounter < addedWords.length)) { // If we have appropriate inputs
      for (int j = 0; j < addedWordsCounter; j++) { // Go through the array that contains the words we want to add         // and we have to update out words
        if (addedWords[j].equals(letter + selected)) {
          alreadyAdded = true; // We've already added the word
        } 
      }
      if (!alreadyAdded) {
        listModel.addElement(letter + " = " + selected); // Add this onto our list of added words
        addedWords[addedWordsCounter] = letter + selected; // Add this string to the array of added words
        ++addedWordsCounter; // Keep track of how many words we have
        bottomWord.setText("");
        for (int k = 0; k < addedWordsCounter; k++) {
          bottomWord.setText(bottomWord.getText() + " " + addedWords[k]); // Update the bottom text
        }
      }
    }
    System.out.println(addedWords[0]);
    } catch(Exception e) {
      System.out.println("Something went wrong when adding a word to the list.");
    }
  }
  
  // What occurs when the search button in the main panel
  // is clicked
  private void searchButtonClicked(ActionEvent e) {
    outputText.setText(""); // Clear the output text as we will be updating it
    boolean shouldBeAdded = true; // Assume if should be added - if it won't, we'll change this condition
    if (addedWordsCounter != 0) {
      for (int i = 0; i < wordlist.size(); i++) { // Go through every word
        int k = 0;
        shouldBeAdded = true; // Reset this boolean value as we go though the loop
        while (k < addedWordsCounter && shouldBeAdded) { // Go through the list of sounds we've added 
          if (wordlist.get(i).phonicList.indexOf("," + addedWords[k] + ",") == -1) { // Search through every word looking for each specific sound
            shouldBeAdded = false;
            break;
          }
          k++;
        }  
        //System.out.println(k);
        if (shouldBeAdded)
          outputText.append(wordlist.get(i).name + "\n");
      }
      if (outputText.getText().equals("")) {
        outputText.setText("No results found");
      }
    }     
  }
  
  // What occurs when the delete button in the
  // main panel is clicked
  private void deleteButtonClicked(ActionEvent e) {
    int selectedIndex = list.getSelectedIndex();
    if (selectedIndex != -1) { // Make sure we actually have something selected
      listModel.remove(selectedIndex); // Remove this from the list
      for (int h = selectedIndex; h < addedWords.length - 1; h++) {
        addedWords[h] = addedWords[h + 1]; // Move everything in the array to fill the space
      } 
      --addedWordsCounter; 
      bottomWord.setText(""); // Reset text
      for (int k = 0; k < addedWordsCounter; k++) {
        bottomWord.setText(bottomWord.getText() + " " + addedWords[k]); // Refill the label
      }
    }
  }   
  
  // What occurs when a user types in
  // or deletes text in the box
  public void textValueChanged(TextEvent e) {
    
    selectBrackets.removeAllItems();
    String searchedLetter = enterLetter.getText();
    if (whichList == "IPA");
    for (int i = 0; i < soundList.size(); i++) {
      if (soundList.get(i).get(0).equals(searchedLetter)) {
        for (int j = 1; j < soundList.get(i).size(); j++) {
          combomodel.addElement(soundList.get(i).get(j));
        }
        break;
      }
    }
  }
  
  
  /**************************************************************************************/
  /**************************************************************************************/
  
  
  
  /**************************************************************************************/
  // THE HELP PANEL SET-UP //
  private JPanel makeHelpPanel() {
    
    // Create all necessary panels
    JPanel help = new JPanel(new BorderLayout());
    
    
    
    
    
    help.add(makeHelpTop(), BorderLayout.NORTH);
    help.add(combineLeftRight(), BorderLayout.CENTER);
    help.add(makeHelpBottom(), BorderLayout.SOUTH);
    help.setBorder(blackLineBorder);
    
    return help;
  }
  
  private JComponent makeHelpLeft() {
    JComponent helpLeft = new JPanel(new GridLayout(1, 1));
    JTextArea description = new JTextArea(
                                          "The purpose of this applet is to enable teachers the ability " + 
                                          "to search through a databse of words and find words that contain " +
                                          "specific phonetic sounds.\n\n" +
                                          "Multiple sounds can be search for as well, " +
                                          "but the returned words will contain both of these sounds - words " +
                                          "with only one of these sounds will not be returned.\n\n\n");
    description.setBackground(helpLeft.getBackground());
    description.setEditable(false); // This will make it so the user cannot remove words from the box
    description.setLineWrap(true); // The lines will wrap when they extend past the horizontal boundaries
    description.setWrapStyleWord(true);
    JScrollPane descScroller = new JScrollPane(description);
    descScroller.setBorder(BorderFactory.createEmptyBorder());
    helpLeft.setBorder(
                       BorderFactory.createCompoundBorder(
                                                          BorderFactory.createTitledBorder("Purpose"),
                                                          BorderFactory.createEmptyBorder(0,0,0,0)));
    helpLeft.add(descScroller);
    return helpLeft;
  }
  
  private JPanel makeHelpRight() {
    JPanel helpRight = new JPanel(new FlowLayout());
    JPanel libraries = new JPanel();
    JPanel syl = new JPanel();
    JPanel sylCheck = new JPanel();
    JPanel lett = new JPanel();
    JPanel lettCheck = new JPanel();
    
    
    helpRight.setBorder(
                        BorderFactory.createCompoundBorder(
                                                           BorderFactory.createTitledBorder("Options"),
                                                           BorderFactory.createEmptyBorder(5,5,5,5)));
    JScrollPane scroll = new JScrollPane(helpRight);
   
    // Buttons for choosing between libraries
    JRadioButton americanLibrary = new JRadioButton(new AbstractAction("American") {
      @Override
      public void actionPerformed( ActionEvent e ) {

      }
    }); 
    
    JRadioButton britishLibrary = new JRadioButton(new AbstractAction("Bristish") {
      @Override
      public void actionPerformed( ActionEvent e ) {

      }
    }); 
   
    ButtonGroup typeOfLib = new ButtonGroup();
    typeOfLib.add(americanLibrary);
    typeOfLib.add(britishLibrary);
    libraries.add(americanLibrary);
    libraries.add(britishLibrary);
    
    // Make the option for number of syllables
    JTextArea numSylDescption = new JTextArea("Number of syllables");
    numSylDescption.setBackground(helpRight.getBackground());
    numSylDescption.setEditable(false);
    enterSyllables = new TextField(4);
    syl.add(numSylDescption);
    syl.add(enterSyllables);
    
    helpRight.add(libraries);
    helpRight.add(syl);
    return helpRight;
  }
  
  private JPanel makeHelpTop() {
    JPanel helpTop = new JPanel();
    helpTop.add(new JLabel("Help Page"));
    return helpTop;
  }
  
  private JPanel makeHelpBottom() {
    JPanel helpBottom = new JPanel();
    JLabel helpBottomLabel = new JLabel("Devin Kearns, Boston University    2013");
    helpBottomLabel.setFont(new Font("Times New Roman", Font.ITALIC, 9));
    helpBottom.add(helpBottomLabel);
    
    return helpBottom;
  }
  
  private JPanel combineLeftRight() {
    JPanel top = new JPanel(new GridLayout(1, 2));
    top.add(makeHelpLeft());
    top.add(makeHelpRight());
    return top;
  }
  
  
  /**************************************************************************************/
  /**************************************************************************************/
  
  
}

class Word {
  public String phonicList; // List of our sounds for this word
  public String name; // What the word is 
  public String fullPhonic; // List of the total line
  
  Word(String name, String list) {
    this.name = name;
    this.phonicList = list;
    this.fullPhonic = null;
  }
  
  Word() {
    this.name = null;
    this.phonicList = null;
    this.fullPhonic = null;   
  }
  
}







