public class Alphabetically {
  
  public BufferedReader getFile(String file_path) throws IOException {
    
    /*
     * getFile simply sets up the file we'll be using for the rest of the methods 
     */
    
    path = file_path;
    InputStream configStream = getClass().getResourceAsStream(path);
    BufferedReader file = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
    return file;
  }
  
  
  
}