import java.util.Scanner;
/**
 * @author Avi Patel
 * @author Jonathan Wang
 * @author Ethan Lau
 * @version 11/12/19
 */
public class BotRunner
{

	/**
	 * Create a Magpie, give it user input, and print its replies.
	 */
	public static void main(String[] args)
	{
		WaiterBot maggie = new WaiterBot();
		
		System.out.println (maggie.getGreeting());
		Scanner in = new Scanner (System.in);
		String statement = in.nextLine();
		
		while (!statement.equals("Bye"))
		{
			System.out.println (maggie.getResponse(statement));
			statement = in.nextLine();			
			
		}
	}

}
