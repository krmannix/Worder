// This simple program will provide you with the hash key for your BU ID
//    number, which can be used to access the list of final exam grades on
//    the CS 112 web site. 

import java.util.*;

public class HashID {
   
   private static int M = 1667; 
   private static int D = 103651;
   private static int N = 549;
   
   private static int hash(int key) {
      return (key * M) % D;
   }
   
   private static int betterHash(String key) {
      int sum = 0; 
      for(int i = 0; i < key.length(); ++i) {
         sum += key.charAt(i);
         sum = hash(sum);                    
      }
      return sum % N;                     
   }
   
   public static void main(String [] args) {
      
      Scanner console = new Scanner(System.in);
      
      System.out.println("Please type in your BU ID Number including the initial U:");
      String ID = console.nextLine();
      
      System.out.println("The hash key for " + ID + " is: " + betterHash(ID));
      
   }
   
}
