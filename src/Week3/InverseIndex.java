package Week3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class InverseIndex {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		File folder = new File("cacm/");
		String indexed_link = "indexed_cacm/";
	      Stack<String> cacm = listFilesForFolder(folder);
	      FileWriter fWriter = null;
	      BufferedWriter writer = null;
	      String str;
	      String contents = "";
	      HashMap<String, Vector<Integer>> index = new HashMap();
	      HashMap<String, Vector<Integer>> id = new HashMap<String, Vector<Integer>>();
	      
	      
	      
	      
	      
	      while (!cacm.empty())
	      {
	    	  	String link = cacm.pop();
	    	  	BufferedReader br = new BufferedReader(new FileReader("cacm/" + link));
	    	  	System.out.println(link);
				
				Set<String> keys = index.keySet();
				try {
						while ((str = br.readLine()) != null) {
								contents += str;
						}
						br.close();
						} catch (IOException e) {
								e.printStackTrace();
								}
						        finally
						        {
						        	String[] tokens = Tokenize(contents);
						        	for (String token : tokens)
						        	{
						        		Vector<Integer> values = index.containsKey(token) ? index.get(token)  : new Vector<Integer>();
						        		if (index.containsKey(token))
						        		{
						        			values.add(1);
						        			index.put(token, values);
						        		}
						        		else
						        		{
						        			values.add(0);
						        			index.put(token, values);
						        
						        		}
						        		
						        	}
						        		
						        }
		
	      	}
				
				
	      
	      

			
			Scanner userInput = new Scanner( System.in);
		  String query = "";
		  while (query != "STOP") {
			  Set<String> keys = index.keySet();
		      System.out.println("Index size is: " + keys.size());
		      System.out.println("Please input a query as a AND/OR/NOT b eg: Charmonman NOT carries \n Or type STOP to quit \n Query: ");Scanner input = new Scanner( System.in);
		      
		      String[] querySplit;
		      query = userInput.nextLine( );
		      querySplit = Tokenize(query);
		      if (querySplit.length > 1) {
			      switch(querySplit[1]) {
				      case "AND":
				    	  AND(querySplit[0], querySplit[2], index);
				    	  break;
				      case "OR":
				    	  OR(querySplit[0], querySplit[2], index);
				    	  break;
				      case "NOT":
				    	  NOT(querySplit[0], querySplit[2], index);
				    	  break;
			      }
		      }
		      else {
		    	  int length = index.get(querySplit[0]).size();
		    	  System.out.println("Documents that contain " + querySplit[0] + ": ");
		      }
		     }
	      }
	
	
	public static String[] Tokenize(String contents)
	{
		contents = contents.replaceAll("<html>", "");
		contents = contents.replaceAll("<pre>", "");
		contents = contents.replaceAll("</html>", "");
		contents = contents.replaceAll("</pre>", "");
		contents = contents.replaceAll("[0-9]", "");
		contents = contents.replaceAll(",", "");
		contents = contents.replaceAll(".", "");
		contents = contents.replaceAll("\t", " ");
		
		String[] tokens = contents.split(" ");
		for (String token : tokens) {
			token.trim();
		}
		return tokens;
	}
	      
	      public static Stack<String> listFilesForFolder(File folder)
	  	{
	  		Stack<String> files = new Stack();
	  		for (File fileEntry : folder.listFiles()) {
	  	        if (fileEntry.isDirectory()) {
	  	            listFilesForFolder(fileEntry);
	  	        } else {
	  	            files.push(fileEntry.getName());
	  	        }
	  	    }
	  		return files;
	  		
	  	}
	      
	      public static void AND(String query1, String query2, HashMap<String, Vector<Integer>> index) {
	    	  System.out.println("Documents that contain " + query1 + " and " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.containsValue(query1) && index.containsValue(query2)) {
	    			  System.out.println("This document contains " + query1 + "and " + query2);
	    		  }
	    	  }
	      }
	      
	      public static void OR(String query1, String query2, HashMap<String, Vector<Integer>> index) {
	    	  System.out.println("Documents that contain " + query1 + " or " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.containsValue(query1) || index.containsValue(query2)) {
	    			  System.out.println("This document contains " + query1 + "and " + query2);
	    		  }
	    		  
	    	  }
	      }
	      
	      public static void NOT(String query1, String query2, HashMap<String, Vector<Integer>> index) {
	    	  System.out.println("Documents that contain " + query1 + ", but not " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.containsValue(query1) && !index.containsValue(query2)) {
	    			  System.out.println("This document contains " + query1 + "and " + query2);
	    		  }
	    	  }
	      }
}