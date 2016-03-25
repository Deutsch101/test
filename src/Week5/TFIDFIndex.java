package Week5;

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
import java.lang.*;

public class TFIDFIndex {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		File folder = new File("cacm/");
		String indexed_link = "indexed_cacm/";
	      Stack<String> cacm = listFilesForFolder(folder);
	      FileWriter fWriter = null;
	      BufferedWriter writer = null;
	      HashMap<String, Vector<HashMap<Integer, String>>> index = new HashMap();
	      Vector<HashMap<Integer, String>> id = new Vector<HashMap<Integer, String>>();
	      int wordcount = 0;
	      int docCount = 0;
	      while (!cacm.empty())
	      {
	    	  	HashMap nullmap = new HashMap();
	    	  	docCount++;
	    	  	String link = cacm.pop();
	    	  	BufferedReader br = new BufferedReader(new FileReader("cacm/" + link));
				HashMap<Integer, String> addHash = new HashMap();
        		addHash.put(1, link);
        		nullmap.put(0, link);
        		id.add(nullmap);
        		
				String str;
				String contents = "";
				
						while ((str = br.readLine()) != null) {
								contents +=str;
						}
						br.close();
						
						        	String[] tokens = Tokenize(contents);
						        	wordcount += tokens.length;
						        	
						        	Set<String> keys = index.keySet();
						        	for (String key : keys) {
						        		index.get(key).add(nullmap);
						        	}
						        	for (String token : tokens)
						        	{
						        		
						        		Vector<HashMap<Integer, String>> newId = (Vector<HashMap<Integer, String>>) id.clone();

						        		if (index.containsKey(token))
						        		{
						        			if (index.get(token).lastElement().containsKey(0))
						        			{
						        				index.get(token).setElementAt(addHash, index.get(token).size()-1);
						        			}
						        		}
						        			
						        		
						        		else
						        		{
						        			newId.add(addHash);
						        			index.put(token, newId);
						        		}
						        	}
	      }
	
	
		
	
				
			Scanner userInput = new Scanner( System.in);
			Set<String> keys = index.keySet();
		  String query = "";
		  while (query != "STOP") {
		      System.out.println("Index size is: " + keys.size());
		      System.out.println("Please input a query as a AND/OR/NOT b eg: Charmonman NOT carries \n Or type STOP to quit \n Query: ");Scanner input = new Scanner( System.in);
		      
		      String[] querySplit;
		      query = userInput.nextLine( );
		      querySplit = query.split(" ");
		      Vector<HashMap<Integer, String>> inverseIndex = new Vector<HashMap<Integer, String>>();
		      if (index.containsKey(querySplit[0])) {
		      if (querySplit.length > 1) {
			      switch(querySplit[1]) {
				      case "AND":
				    	  inverseIndex = AND(querySplit[0], querySplit[2], index, wordcount);
				    	  break;
				      case "OR":
				    	  inverseIndex = OR(querySplit[0], querySplit[2], index, wordcount);
				    	  break;
				      case "NOT":
				    	  inverseIndex = NOT(querySplit[0], querySplit[2], index, wordcount);
				    	  break;
			      }
			      
		      }
		      
		      else {
		    	  int length = index.get(querySplit[0]).size();
		    	  for (int i = 0; i < length; i++) {
		    		 
		    		  if(index.get(querySplit[0]).elementAt(i).get(1) != null) {
		    			  inverseIndex = index.get(querySplit[0]); 
		    			  System.out.println(inverseIndex.get(i));
		    		  }
		    	  
		    	  }
		      }
		      
		      }
		      else {
		    	  System.out.println("No such item in query!");
		      }
		      
		  }
	      }
	
	
	public static double TFIDF(Vector<HashMap<Integer, String>> index, int wordcount, String query)
	{
		int containsTerm = 0;
		int freq;
		double cosine = 0;
		double tfidf;
	      for (HashMap key : index)
	      {
	    	  
	    	  if(key.containsKey(1)) {
	    		  containsTerm++;
	    	  }
	      }
	      for (HashMap key : index) {
	    	  freq = key.containsKey(1) ? 1 : 0;
	    	  cosine += Math.pow(Math.log(freq+1)*Math.log(wordcount/containsTerm), 2);
	      }
	      
	      cosine = Math.sqrt(cosine);
	      
    	  tfidf = (Math.log(1+1)*Math.log(wordcount/containsTerm)/cosine);
	      return tfidf;
	}
	

	
	
	public static String[] Tokenize(String contents)
	{
		contents = contents.replaceAll("<html>", "");
		contents = contents.replaceAll("<pre>", "");
		contents = contents.replaceAll("</html>", "");
		contents = contents.replaceAll("</pre>", "");
		contents = contents.replaceAll("[0-9]", "");
		contents = contents.replaceAll(",", "");
		contents = contents.replaceAll(":", "");
		contents = contents.replaceAll("\t", "");
		contents = contents.replaceAll("\t", "");
		contents = contents.toLowerCase();
		
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
	      
	      public static Vector<HashMap<Integer, String>> AND(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index, int wordcount) {
	    	  Vector<HashMap<Integer, String>> inverseIndex = new Vector<HashMap<Integer, String>>();
	    	  HashMap<Integer, String> indexHash;
	    	  System.out.println(index.get(query1).size());
	    	  System.out.println(index.get(query2).size());
	    	  for (int i = 0; i < index.get(query1).size() -1; i++) {
	    		  indexHash = new HashMap();

	    		  if (index.get(query1).elementAt(i).containsKey(1) && index.get(query2).elementAt(i).containsKey(1))
	    		  {
	    			  indexHash.put(1, index.get(query1).elementAt(i).get(1));
	    			  inverseIndex.add(indexHash);
	    			  
	    		  }
	    		  
	    		  else { if (index.get(query1).elementAt(i).containsKey(0) || index.get(query2).elementAt(i).containsKey(0)) {
	    			  indexHash.put(0, index.get(query1).elementAt(i).get(1));
	    			  inverseIndex.add(indexHash);
	    		  } }
	    		  
	    	  }
	    	  double q1tfidf;
	    	  double q2tfidf;
	    	  
	    	  q1tfidf = TFIDF(index.get(query1), wordcount, query1);
	    	  q2tfidf = TFIDF(index.get(query2), wordcount, query2);
	    	  System.out.println(query1 + ": \t" + q1tfidf);
	    	  System.out.println(query2 + ": \t" + q2tfidf);
	    	  for (int i = 0; i < inverseIndex.size(); i++) {
	    		  System.out.println(inverseIndex.get(i).get(1));
	    	  }
	    	  return inverseIndex;
	      }
	      
	      public static Vector<HashMap<Integer, String>> OR(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index, int wordcount) {
	    	  Vector<HashMap<Integer, String>> inverseIndex = new Vector<HashMap<Integer, String>>();
	    	  HashMap<Integer, String> indexHash;
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  indexHash = new HashMap();
	    	  		
	    			  if (index.get(query1).elementAt(i).containsKey(1) || index.get(query2).elementAt(i).containsKey(1)) {
	    				  indexHash.put(1, index.get(query1).elementAt(i).get(1));
		    			  inverseIndex.add(indexHash);
	    			  }
	    		  else {
	    			  indexHash.put(0, index.get(query1).elementAt(i).get(1));
	    			  inverseIndex.add(indexHash);
	    		  }
	    		  
	    	  }
	    	  double q1tfidf;
	    	  double q2tfidf;
	    	  
	    	  q1tfidf = TFIDF(index.get(query1), wordcount, query1);
	    	  q2tfidf = TFIDF(index.get(query2), wordcount, query2);
	    	  System.out.println(query1 + ": \t" + q1tfidf);
	    	  System.out.println(query2 + ": \t" + q2tfidf);
	    	  for (int i = 0; i < inverseIndex.size(); i++) {
	    		  System.out.println(inverseIndex.get(i).get(1));
	    	  }

	    	  return inverseIndex;
	      }
	      
	      public static Vector<HashMap<Integer, String>> NOT(String query1, String query2, HashMap<String, Vector<HashMap<Integer, String>>> index, int wordcount) {
	    	  Vector<HashMap<Integer, String>> inverseIndex = new Vector<HashMap<Integer, String>>();
	    	  HashMap<Integer, String> indexHash;
	    	  for (int i = 0; i < index.get(query1).size(); i++) {
	    		  indexHash = new HashMap();
		    		  if (index.get(query1).elementAt(i).containsKey(1) != index.get(query2).elementAt(i).containsKey(1))
		    		  {
		    			  indexHash.put(1, index.get(query1).elementAt(i).get(1));
		    			  inverseIndex.add(indexHash);
		    		  }
	    		  else {
	    			  indexHash.put(0, index.get(query1).elementAt(i).get(1));
	    			  inverseIndex.add(indexHash);
	    		  }
	    	  }
	    	  double q1tfidf;
	    	  double q2tfidf;
	    	  
	    	  q1tfidf = TFIDF(index.get(query1), wordcount, query1);
	    	  q2tfidf = TFIDF(index.get(query2), wordcount, query2);
	    	  System.out.println(query1 + ": \t" + q1tfidf);
	    	  System.out.println(query2 + ": \t" + q2tfidf);
	    	  for (int i = 0; i < inverseIndex.size(); i++) {
	    		  System.out.println(inverseIndex.get(i).get(1));
	    	  }

	    	  return inverseIndex;
	      }
}
