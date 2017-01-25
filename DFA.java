import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

//This class represents delta in our DFA -- the transformation functions for each letter of our alphabet at each state

public class DFA {
	private int transform[][]; //The state transition table
	private HashMap<Character,Integer> Sigma = new HashMap<>(); // HashMap of our alphabet to determine index in delta
	private HashSet<Integer> AcceptStates = new HashSet<>(); //set of states that are accepted
	private char [] alphabet; //An array of our alphabet
	
	public DFA() {
		
		
	}
	
	//Wrapper function for the batch test which will test all strings of the alphabet up to some 
	//string size strlen
	public void batch_test(int strlen)
	{
        for (int i = 0;i <= strlen; ++i)
        	test_many(i, alphabet,"");
	}
	
	//Tests string to_test, returning true if it is accepted by the DFA, false if not. 
	//Tests each character against the alphabet sigma as well.
	public boolean test_string(String to_test)
	{
		//Sets initial state to 0, the defaul start state.
		int state = 0;
		
		//Adds each character in the string from left into the DFA. the delta function takes 
		//the current state and the next string to determine the new state. If the character isn't 
		//in the alphabet, the state will be set to -1, an error state and exit the routine.
		for (int i = 0; i < to_test.length() && state != -1; ++i)
		{
			char current = to_test.charAt(i);
			
			//Determines if the current character is in the alphabet
			if (char_isgood(current))
			{
				//Determines the new state
				state = delta(state,current); 
			}
			else
				state = -1;
		}
		//Returns true or false if the last state is a member of the accept states.
		return accept(state);
	}
	
	
	//This will execute the delta function of our DFA, taking a current state 'state' and character 'to_transform
	// and returning the new state. Returns -1 on a failed transformation (which is an error). 
	private int delta(int state,char to_transform)
	{
		//the index of our char for use in the transformation table array.
		int charIndex;
		
		//Determines the index of the character we want to transform from our alphabet for the transformation table
		charIndex = Sigma.get(to_transform);
		
		int result = -1;
		//Error checks to ensure our input state is one of states in the DFA and that the character index is within bounds
		if (transform.length >= state && transform[0].length >= charIndex)
		{
			//state will be 0 through (# of states - 1), charIndex will be 0 to (number of characters in alphabet -1)
			result = transform[state][charIndex];
		}
		return result;
	}
	
	//Creates a table for state transformations of size states x characters (in the alphabet). 
	//It defaults all values to transform to state -1 which will be used for an indication of no 
	//transformation for a given character at the given state.
	private void tablesize(int states,int characters){
	
		transform = new int[states][characters]; 
		for (int i = 0; i < states; ++i)
		{
			for (int j = 0;j < characters; ++j)
			{
				transform[i][j] = -1;
			}
		}
	}
	
	
	//Sets the transformation value for a given state and given character as newState.
	//Returns 0 on error, 1 on success. Used in creating the DFA.
	private int set_transformation(int state,int charIndex,int newState)
	{
		int error = 0;
		
		if (transform.length > state && transform[0].length > charIndex)
		{
			transform[state][charIndex] = newState;
			error = 1;
		}
		return error;
	}
	
	//Returns true if 'state' is in the set of accept states, false otherwise.
	private boolean accept(int state)
	{
		return AcceptStates.contains(state);
	}
	
	//returns true if to_test is a letter of our alphabet Sigma.
	private boolean char_isgood(char to_test)
	{
		return Sigma.containsKey(to_test);	
	}


    private void test_many(int maxLength, char[] alphabet, String curr) {
        
        // If the current string has reached it's maximum length
        if(curr.length() == maxLength) {
            System.out.print(curr + " -- ");
            if (test_string(curr))
            	System.out.print("Accepted\n");
            else
            	System.out.print("\n");

        // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
        	for (char c : alphabet) {
                String oldCurr = curr;
                curr += c;
                test_many(maxLength,alphabet,curr);
                curr = oldCurr;
            }
        }
    }
    
    
	//Loads the DFA definitions from a text file. Allows for comments in the text file with // 
	//Expected DFA definition:
	// 1  size of the alphabet (int)
	// 2  number of states (int)
	// 3  Alphabet (string of characters ex: abc or 10)
	// 4  State 0 transformations -- space separated list of integer states for each letter of alphabet (ex: 0 1 1) 
	// 5  State 1 transformations
	// 6  ...
	// V  State n-1 transformations 
	// W  Accept state 1 (int)
	// X  Accept state 2 (int)
	// Y  ...
	// Z  Accept state m (int)
	// 
	public void load_file(String filename)
	{
		FileReader file_to_read = null;
		int stateSize;
		int sigmaSize;
		String new_line = null; 
		
		try {
			file_to_read = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader buffered_file = new BufferedReader(file_to_read);
		try {
			new_line = buffered_file.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			//Allows for comments in text file
			while (new_line != null && new_line.startsWith("//"))
				new_line = buffered_file.readLine();
			
			//The first non-comment line is the size of the alphabet Sigma
			sigmaSize = Integer.parseInt(new_line);
			
			//Allows for comments
			new_line = buffered_file.readLine();
			while (new_line != null && new_line.startsWith("//"))
				new_line = buffered_file.readLine();
			
			//The second line is the number of states in the DFA
			stateSize = Integer.parseInt(new_line);
			
			//Creates a delta transformation table based on the size of Sigma and # of states
			this.tablesize(stateSize,sigmaSize);

			//Allows for comments
			new_line = buffered_file.readLine();
			while (new_line != null && new_line.startsWith("//"))
				new_line = buffered_file.readLine();
			
			//Creates a hashmap of the alphabet and an index to be used with the delta transformation
			// the first letter of the alphabet is assigned index 0, second is 1, etc...
			// Also creates an array of the alphabet
			alphabet = new char [sigmaSize];
			for (int i = 0; i < sigmaSize ; ++i)
			{
				char temp = new_line.charAt(i);
				alphabet[i] = temp;
				Sigma.put(temp,i);
				
			}
			
			//Allows for comments
			new_line = buffered_file.readLine();
			while (new_line != null && new_line.startsWith("//"))
				new_line = buffered_file.readLine();
			
			//Creates the state transformation table that the delta function will use
			for (int i = 0; i < stateSize; ++i)
			{
				String values[];
				values = new_line.split(" ");
				
				for (int j = 0; j < sigmaSize; ++j)
				{
					int temp = Integer.parseInt(values[j]);
					this.set_transformation(i,j,temp);
				}
				
				//Allows for comments
				new_line = buffered_file.readLine();
				while (new_line != null && new_line.startsWith("//"))
					new_line = buffered_file.readLine();
			}
			
			// Creates set F of all the accept states of the DFA
			while (new_line != null)
			{
				AcceptStates.add(Integer.parseInt(new_line));
				
				//Allows for comments
				new_line = buffered_file.readLine();
				while (new_line != null && new_line.startsWith("//"))
					new_line = buffered_file.readLine();
			}
			
			buffered_file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
