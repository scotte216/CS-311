import java.util.Scanner;

public class Project1 {

	private static int STRSIZE = 15;
	public Project1() {

	}

	public static void main(String[] args) {
		String to_test = new String(); // String to be tested in the DFA
		DFA Machine; // An instance of our DFA
		Scanner input = new Scanner(System.in); // Used for reading in characters
		boolean again; // Used to prompt for redoing certain menu options
		boolean restart = true; // Used to start the whole thing over.
		int choice; // A menu selection variable.
		
		
		//Main menu loop
		while (restart)
		{
			choice = 0;
			again = true;
			Machine = new DFA();
			System.out.println("Which DFA would you like to test?");
			
			//Determine which DFA the user wants to test from 1 of 4 possible DFAs.
			while (choice == 0)
			{
				System.out.println("1) L(D) is a^nb^n where 0<= n <= 3, Sigma = {a,b,c}");
				System.out.println("2) L(D) is a binary number divisible by 5, S={0,1}");
				System.out.println("3) L(D) is an even number of 0s or exactly three 1s, S = {0,1}");
				System.out.println("4) L(D) is contains the substring 'abc', S = {a,b,c}");
				
				choice = input.nextInt();
				input.nextLine();
				
				if (choice == 1)
					Machine.load_file("DFA Definition A.txt");
				else if (choice == 2)
					Machine.load_file("DFA Definition B.txt");
				else if (choice == 3)
					Machine.load_file("DFA Definition C.txt");
				else if (choice == 4)
					Machine.load_file("DFA Definition D.txt");				
				else 
				{
					System.out.println("That's not a valid choice. Choose 1, 2, 3, or 4.");
					choice = 0;
				}
			}


			// Prompt user if they want to test manually entered strings or a batch of strings.
			choice = 0;
			while (choice != 1 && choice != 2)
			{
				System.out.println("What would you like to do?");
				System.out.println("1) Test manually entered strings");			
				System.out.println("2) Automatically test a set of strings");
				
				choice = input.nextInt();
				input.nextLine();
			}
			
			//At this point 'choice' is either 1 (manual strings or 2 (auto strings).
			// Run the DFA allowing manual entry of strings to test. 
			if (choice == 1)
			{
				while (again)
				{
					System.out.println("Enter a string to test");
					
					to_test = input.nextLine();
		
					if (Machine.test_string(to_test))
					{
						System.out.println("String accepted by machine!");
					}
					else
					{
						System.out.println("String rejected by machine. :(" );
					}
					System.out.println("Again? (Y/N)");
					again = input.nextLine().toLowerCase().startsWith("y");
				}

			}
			
			//Run the batch test on the loaded DFA.
			else
			{
				System.out.println("Every string in the alphabet will be tested up to length n.");
				System.out.println("What would you like to set n to? (0 to " + STRSIZE + ")");
				
				int stringlength = input.nextInt();
				input.nextLine();
				
				//Ensures the string is of a reasonable size (for some values of reasonable). 
				//Not recommended going over 12 for alphabets with 3 letters. Processing takes
				//a long time.
				while (!valid_number(stringlength))
				{
					System.out.println("Try entering a number from 0 to " + STRSIZE);
					stringlength = input.nextInt();
					input.nextLine();
				}
				Machine.batch_test(stringlength);
			}


//          A chunk of code I made to test the 'divides by 5' DFA. A 'smart' test, but I 
//			ended up making	a generic test batch function. 
//			int x = 0;
//			String temp = new String();
//			for (x = 0; x <= 1000000; ++x)
//			{
//				temp = Integer.toBinaryString(x);
//				boolean test = Machine.test_string(temp);
//				if ((test && x%5 == 0) || (!test && x%5 != 0)) {}
//				//	System.out.println("Correct for: " + x);
//				else
//					System.out.println("Error at: " + x);
//				System.out.println(x + " " + temp + " " + x%5 + " " + Machine.test_string(temp));
//			}
			
			
			
			System.out.println("Start over? (Y/N)");
			restart = input.nextLine().toLowerCase().startsWith("y");
		}
		
		input.close();
		System.out.println("Goodbye.");
	}
	
	//A little routine to make sure the size of n is valid and reasonable
	private static boolean valid_number(int to_test)
	{
		if (to_test >=0 && to_test <=STRSIZE)
			return true;
		return false;
	}
	
}
