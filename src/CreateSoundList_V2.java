import java.util.*;
import java.io.*;


///////////////////////////////////
/*
 * This is what the return array looks like:
 *  ______________________________________________
 * |              |         |           |         |     
 * |  soundLetter | gpcCode | soundCode | ipaCode |
 * |______________|_________|___________|_________|
 * |              |         |           |         |
 * |      a       |  a[E]   |    E      |    e:   |
 * |______________|_________|___________|_________|
 * 
 * And so on down vertically
 * 
 */

public class CreateSoundList_V2 {
  
  private static String path;
  private static ArrayList<ArrayList<String>> soundsList;
  private static int maxSubSounds;
  
  
  CreateSoundList_V2() throws IOException {
    this.path = "Sound_And_IPA_List.txt";
    this.soundsList = readFile(path);
  }
  
  public ArrayList<ArrayList<String>> getSoundList() {
    return this.soundsList;    
  }
    
  public int getMaxSubSounds() {
   return this.maxSubSounds; 
  }
  
  private void setMaxSubSounds(int max) {
    this.maxSubSounds = max;
  }
  
  public ArrayList<ArrayList<String>> readFile(String filepath) throws IOException {
    
    /*
     * readFile is the main method that is called to get the sounds list. It's simply a container for the other methods
     */ 
    
    return getSoundsList(new Scanner(getFile(filepath)));
  }
  
  public BufferedReader getFile(String file_path) throws IOException {
    
    /*
     * getFile simply sets up the file we'll be using for the rest of the methods 
     */
    
    path = file_path;
    InputStream configStream = getClass().getResourceAsStream(path);
    BufferedReader file = new BufferedReader(new InputStreamReader(configStream, "UTF-16"));
    return file;
  }
  
  public ArrayList<ArrayList<String>> getSoundsList(Scanner input) {
    int tempMax = 0; // This will count how many subsounds there are per sound, so we can compute the max number of subsounds
    int realMax = 0;
    String baseString;
    StringTokenizer line;
    ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
    ArrayList<String> soundLetter = new ArrayList<String>(); // Just the base letter, no brackets. For ex., it would be 'a' for 'a[]'
    ArrayList<String> soundCode = new ArrayList<String>();
    ArrayList<String> ipaCode = new ArrayList<String>(); // The list of all the IPA sounds, in accordance with the other sounds
    ArrayList<String> gpcCode = new ArrayList<String>(); // This will be our first index - it is used when searching for words
    while(input.hasNextLine()) {
      
      line = new StringTokenizer(input.nextLine());
      baseString = line.nextToken();
      soundLetter.add(baseString.substring(0, baseString.indexOf('[')));
      gpcCode.add(baseString);
      if (!line.hasMoreTokens()) { // If we have a case with empty brackets
        ipaCode.add(" ");
        soundCode.add(" ");
      } else {
        soundCode.add(line.nextToken());
        ipaCode.add(line.nextToken());
      } 
    }
    output.add(soundLetter);
    output.add(gpcCode);
    output.add(soundCode);
    output.add(ipaCode);
    setMaxSubSounds(realMax);
    return output;
  }

  
  public void printArray(ArrayList<ArrayList<String>> inputArray) {
    
    for (int i = 0; i < inputArray.get(0).size(); i++) {
      System.out.println(inputArray.get(0).get(i) + "  " + inputArray.get(1).get(i) + "  " + inputArray.get(2).get(i) + "  " + inputArray.get(4).get(i));
    }
  }
  
  
  
  
}