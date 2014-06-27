import java.io.*;
import java.util.*;

public class CreateWordList {
  
  private static String path; 
  private static ArrayList<Word> wordList;
  
  CreateWordList() throws IOException {
    this.wordList = readFile("WordList.txt");      
  }
  
  CreateWordList(String american) throws IOException {
   this.wordList = readFile("WordListAmerican.txt"); 
  }
  
  public ArrayList<Word> getWordList() {
   return wordList; 
  }
  
  public ArrayList<Word> readFile(String filepath) throws IOException {
    
    /*
     * readFile is the main method that is called to get the word list. It's simply a container for the other methods
     */ 
    
    return getWordList(new Scanner(getFile(filepath)));
  }
  
    
  public BufferedReader getFile(String file_path) throws IOException {
    
    /*
     * getFile simply sets up the file we'll be using for the rest of the methods 
     */
    
    path = file_path;
    InputStream configStream = getClass().getResourceAsStream(path);
    BufferedReader file = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
    return file;
  }
  
  private ArrayList<Word> getWordList(Scanner input) {
    
    /* 
     * getWordList is the control of the methods that returns the wordList we will be using. It runs until we reach
     * the end of the file and calls the getWord() method every line, then adds its return value into an ArrayList
     * 
     */
    
    ArrayList<Word> wordList = new ArrayList<Word>();
    while (input.hasNextLine()) {
      // Get the next line that will be broken down
      String inString = input.nextLine(); 
      wordList.add(getWord(inString));
    }
    input.close();
    return wordList;
  }
  
  private static Word getWord(String inString) {
    
    /* 
     * Get Word is the most basic method. It is given a string which contains all the elements need to create a word
     * and uses String methods to parse the string and return a completed Word variable. 
     * 
     */
    
    Scanner inputString = new Scanner(inString);
    // Refresh our strings and create the Word variable. Set the initial values by reading tokens
    String phonics = "";
    Word word = new Word();
    word.name = inputString.next();
    word.fullPhonic = inputString.next(); // Get the culmination
    // As each word has a different number of individual sounds, we need to keep reading tokens for a variable amount of times
    while (inputString.hasNext()) {
      String individual = inputString.next();
      int firstBracket = individual.indexOf("[") + 1;
      int secondBracket = individual.indexOf("]");
      // In some cases, items such as dashes or apostrophies can return values of -1 for indexOf methods
      // We'll disclude these characters
      if (firstBracket != -1 && secondBracket != -1) {
       // phonics += "," + individual.substring(firstBracket, secondBracket) + ",";
       phonics += "," + individual + ",";
      }
    }
    // Add this culminated string to the word object
    inputString.close();
    word.phonicList = phonics;
    return word;
  }
  
}
  

