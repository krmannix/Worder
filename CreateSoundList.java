import java.util.*;
import java.io.*;

public class CreateSoundList {
  
  private static String path;
  private static ArrayList<ArrayList<String>> soundsList;
  private static int maxSubSounds;
  
  
  CreateSoundList() throws IOException {
    this.path = "SoundList.txt";
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
    BufferedReader file = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
    return file;
  }
  
  public ArrayList<ArrayList<String>> getSoundsList(Scanner input) {
    int tempMax = 0; // This will count how many subsounds there are per sound, so we can compute the max number of subsounds
    int realMax = 0;
    String lastSound = "";
    String line;
    ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
    ArrayList<String> sound = new ArrayList<String>();
    while(input.hasNextLine()) {
      line = input.nextLine();
      if (lastSound.equals("")) { // First case, for the first line
        lastSound = line.substring(0, line.indexOf('['));
        sound.add(lastSound);     
        sound.add('[' + line.substring(line.indexOf('[')+1, line.indexOf(']')) + ']'); // Add the rest of the line
        ++tempMax;
      } else if (lastSound.equals(line.substring(0, line.indexOf('[')))) {
        sound.add('[' + line.substring(line.indexOf('[')+1, line.indexOf(']')) + ']'); // Just add whats between the brackets
        ++tempMax;
      } else {
        output.add(sound);
        
        sound = new ArrayList<String>();
        if (realMax < tempMax) {
          realMax = tempMax;
        tempMax = 0;
        }
        lastSound = line.substring(0, line.indexOf('[')); // Change the variable 'lastSound'
        sound.add(lastSound);
        sound.add('[' + line.substring(line.indexOf('[')+1, line.indexOf(']')) + ']'); // Add the rest of the line
        ++tempMax;
        
      } 
      
    }
    setMaxSubSounds(realMax);
    return output;
  }

  
  public void printArray(ArrayList<ArrayList<String>> inputArray) {
    
    for (int i = 0; i < inputArray.size(); i++) {
      System.out.println(inputArray.get(i).get(0));
      for (int j = 1; j < inputArray.get(i).size(); j++) {
        System.out.print(inputArray.get(i).get(j) + " ");
      }
      System.out.println();
    
  }
    System.out.println(inputArray.size());
  }
  
  
  
  
}