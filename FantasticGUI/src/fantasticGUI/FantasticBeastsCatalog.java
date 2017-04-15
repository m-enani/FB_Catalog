package fantasticGUI;
/*
 * Name: Mahmoud Enani
 * Date: 04/03/2017
 * Language: Java
 * Version: 8
 * Includes: None
 * Class: COP2552.OM1
 * Assignment: Project 3
 */

/*
 * This program is a catalog of Fantastic Beasts. The user can search for Beats already in the catalog
 * or add additional Beasts. The user can enter new beasts and currently only one food per beast.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class FantasticBeastsCatalog {
	
	// create file instance
	static File file = new File("catalog.txt");
	static Scanner catalog; 
	static PrintWriter output;
	
	static {
		try {
			catalog = new Scanner(file);
		}
		catch (FileNotFoundException e){
			System.out.println("Catalog file cannot be located!");
			System.exit(1);
		}
	}
	// file to write to
	static {
		try {
			output = new PrintWriter(new FileWriter(file, true));
		}
		catch (IOException e){
			System.out.println("Catalog file cannot be located!");
			System.exit(1);
		}
	}
	
	// creates scanner 'userInput' to take in user input
	final Scanner userInput = new Scanner(System.in);
	
	// creates string 'userResponse' variable to store userInput
	final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	// constructor
	FantasticBeastsCatalog() throws FileNotFoundException, IOException{

	}
	
	// method to enter data into the catalog
	String enterData(String nameEntered, String foodEntered) throws IOException{
			
		boolean matchesFound = false;  // tracks if any matches are found in the catalog		
	
		catalog = new Scanner(file);
		output = new PrintWriter(new FileWriter(file, true)); 
		
		// determine if word is already in the catalog
		while(catalog.hasNext()) {
				
			String temp = catalog.nextLine();
			
			String[] word = temp.split(",");

			if(word[0].trim().toLowerCase().equals(nameEntered.toLowerCase())) {
				matchesFound = true;
				break;
			}
		}
	
		// alerts user to fact that the beast already exists (mitigates duplicate entries)
		if (matchesFound){
			return "Hate to break it to you, but we've already found that one...";
		}
		
		output.print("\n" + nameEntered + ", " + foodEntered);		
		output.close();
		
		return "Thank you for the entry. Your assistance is very much appreciated!";
		
	}
	
	// method to retrieve data from the catalog
	String[] retrieveData(String searchTerm) throws IOException{
	
		int matchesFound = 0; // determines if only one result is returned by search
		
		catalog = new Scanner(file);
		
		String[] word;
		String[] result = new String[1];
	
		// determine how many results returned
		while(catalog.hasNext()) {
			
			String temp = catalog.nextLine();
			
			word = temp.split(",");

			if(word[0].trim().replaceAll("\\s+","").toLowerCase().startsWith(searchTerm.toLowerCase())) {
				
				matchesFound++;
								
				// ensure a complete match is not missed because of a partial match
				if (word[0].toLowerCase().equals(searchTerm.toLowerCase())){
					matchesFound = 1; 
					break;
				}
			}
		}
				
		// indicates to the user that no results were found
		if (matchesFound == 0) {
			result[0] = "No results found!";		
		}
		
		// displays information about beast to user if only one match
		else if (matchesFound == 1) {
			
			catalog = new Scanner(file);
						
			while(catalog.hasNext()) {
				
				String temp = catalog.nextLine();
				
				word = temp.split(",");
				
				System.out.println(word[0]);
				
				if(word[0].trim().replaceAll("\\s+","").toLowerCase().startsWith(searchTerm.toLowerCase())) {
				
					result[0] = ("Name: " + word[0].trim());
					
					for(int i = 1; i < word.length; i ++) {
						
						if (i == 1){
							
							result[0] = result[0] + " ;Food: " + word[i].trim();

						}
						else {
							result[0] = result[0] + word[i].trim();
						}
						
						if(i != word.length - 1) {
							result[0] = result[0] + ", ";
						}
					}
						
					break;
					
				}
			}
		}
		// display matches found to user and initiates option to search for beast
		else if (matchesFound > 1) {
			
			String[] matchesList = new String[matchesFound];
					
			catalog = new Scanner(file);
			
			int i = 0;
			
			while(catalog.hasNext()) {
				
				String temp = catalog.nextLine();
				
				word = temp.split(",");
				
				if(word[0].trim().replaceAll("\\s+","").toLowerCase().startsWith(searchTerm.toLowerCase())) {
					matchesList[i] = word[0].trim().toString();
					i++;
				}
			}
			
			Arrays.sort(matchesList);
			
			return matchesList;
			
		}
		return result;
	
	}
}
