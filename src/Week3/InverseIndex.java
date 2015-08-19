package Week3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	      HashMap<String, Vector<HashMap<Integer, String>>> index = new HashMap();
	      Vector<HashMap<Integer, String>> id = new Vector<HashMap<Integer, String>>();
	      while (!cacm.empty())
	      {
	    	  	HashMap nullmap = new HashMap();
	    	  	nullmap.put(0, "");
	    	  	id.add(nullmap);
	    	  	String link = cacm.pop();
	    	  	BufferedReader br = new BufferedReader(new FileReader("cacm/" + link));
				StringBuilder content = new StringBuilder();
				HashMap<Integer, String> addHash = new HashMap();
        		addHash.put(1, link);
				String str;
				String contents = "";
				try {
						while ((str = br.readLine()) != null) {
								contents +=str;
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
						        		
						        		Vector<HashMap<Integer, String>> newId = (Vector<HashMap<Integer, String>>) id.clone();
						        		if (!index.containsKey(token))
						        		{
						        			if (newId.isEmpty()) {
						        				newId.remove(-1);
						        			}
						        			HashMap<Integer, String> newMap = new HashMap();
						        			newMap.put(1, link);
						        			Vector<HashMap<Integer, String>> docids = new Vector<HashMap<Integer, String>>();
						        			docids.add(newMap);
						        			index.put(token, docids);
						        		}
						        		else
						        		{
						        			
						        			if (index.get(token).lastElement().get(1) != link)
						        			{
						        			
						        			index.get(token).add(addHash);
						        		/* 	System.out.println("Key: " + token + "\n");
						        			System.out.println("Value: " + link + "\n"); */
						        
						        			}
						        		}
						        		
						        	}
		
	}
				
				
	      }
	      Set<String> keys = index.keySet();

			
			/*		 File f = new File("indexed_cacm");
  		 f.createNewFile();
  		 fWriter = new FileWriter("Indexed_cacm");
  		  
	         writer = new BufferedWriter(fWriter); 
  	  
			for (String key : keys) {
				System.out.println("Key: " + key);
				for(HashMap value : index.get(key))
				{
					 System.out.println("Value: " + value.get(1));
				}
				
			}*/
			Scanner userInput = new Scanner( System.in);
		  String query = "";
		  while (query != "STOP") {
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
		    	  for (int i = 0; i < length; i++) {
		    		  System.out.println("Documents that contain " + querySplit[0] + ": ");
		    		  System.out.println(index.get(querySplit[0]).elementAt(i).get(1) + ",");
		    	  
		    	  }
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
		contents = contents.replaceAll("\t", "");
		
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
	      
	      public static void AND(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index) {
	    	  System.out.println("Documents that contain " + query1 + " and " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.get(query2).elementAt(i) != null) {
	    		  if (index.get(query1).elementAt(i).get(1) == index.get(query2).elementAt(i).get(1))
	    		  {
	    			  System.out.println(index.get(query1).elementAt(i).get(1) + ", ");
	    		  }
	    		  }
	    	  }
	      }
	      
	      public static void OR(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index) {
	    	  System.out.println("Documents that contain " + query1 + " or " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.get(query2).elementAt(i).get(1) != null) {
	    			  System.out.println(index.get(query1).elementAt(i).get(1) + ", ");
	    			  System.out.println(index.get(query2).elementAt(i).get(1) + ", ");
	    		  }
	    		  
	    	  }
	      }
	      
	      public static void NOT(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index) {
	    	  System.out.println("Documents that contain " + query1 + ", but not " + query2);
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  if (index.get(query2).elementAt(i).get(1) != null) {
	    		  if (index.get(query1).elementAt(i).containsValue(1) != index.get(query2).elementAt(i).containsValue(1))
	    		  {
	    			  System.out.println(index.get(query1).elementAt(i).get(1) + ", ");
	    		  }
	    		  }
	    	  }
	      }
}
