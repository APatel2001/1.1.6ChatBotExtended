import java.util.*;

/**
 * A program that serves as a virtual waiter conversing with a human customer
 * This version:
 *<ul><li>
 *      Uses advanced search for keywords 
 *</li><li>
 *      Will transform statements as well as react to keywords
 *</li></ul>
 * @author Avi Patel
 * @author Jonathan Wang
 * @author Ethan Lau
 * @version 11/12/19
 *
 */
public class WaiterBot
{
    String[] menu = {"burger", "fries", "soda", "shake", "water"};
    double[] price = {5.00, 2.00, 1.00, 2.00, 0.00};
    double cost = 0.00;
    ArrayList<String> receipt = new ArrayList<String>();

    /**
     * Get a default greeting   
     * @return a greeting
     */ 
    public String getGreeting()
    {
        return "Hello, we serve burgers, fries, soda, shakes, \nand water.";
    }
    /**
     * gets bot's name
     * @return name
     */
    public String getName() 
    {
        return "My name is WaiterBot";
    }
    
    /**
     * gets bot's creator names
     * @return creator names
     */
    public String getCreator() 
    {
        return "Ethan, Avi, and Jonathan made me!";
    }
    /**
     * gets bot's age
     * @return bot's age
     */
    public String getAge()
    {
        return "I was born on Novemeber 12, 2019.";
    }
    /**
     * Gives a response to a user statement
     * 
     * @param statement
     *            the user statement
     * @return a response based on the rules given
     */
    public String getResponse(String statement)
    {
        String response = "";
        if (statement.length() == 0)
        {
            response = "Say something, please.";
        }
        else if (findKeyword(statement, "cost") >= 0)
    {
    response = getPrice(statement);
    }
        else if (findKeyword(statement, "allergic") >= 0)
	{
	response = allergies(statement);
	} 
        else if (findKeyword(statement, "describe") >= 0 
        || findKeyword(statement,"description")>=0)
	{
	response = menuDescription(statement);
	}
        else if (statement.toLowerCase().contains("love you") || statement.toLowerCase().contains("baby") || statement.toLowerCase().contains("beautiful") || statement.toLowerCase().contains("pretty"))
        {
            response = uncomfortable();
        }

        else if (findKeyword(statement, "no") >= 0)
        {
            response = "Why so negative?";
        }
        else if (findKeyword(statement, "mother") >= 0
                || findKeyword(statement, "father") >= 0
                || findKeyword(statement, "sister") >= 0
                || findKeyword(statement, "brother") >= 0)
        {
            response = "Tell me more about your family.";
        }
        
        else if (findKeyword(statement, "stupid")>=0
                || findKeyword(statement,"idiot")>=0
                || findKeyword(statement, "dumb")>=0
                || findKeyword(statement, "hate")>=0
                  )
        {
            int insultNumber = (int)(Math.random()*4+1);
            if (insultNumber == 1) 
            {
                response = "Aww, I'm sad.";
            }
            else if (insultNumber == 2) 
            {
                response = "That's not nice.";
            }
            else if (insultNumber == 3) 
            {
                response = "Why you have to be mad?";
            }
            else if (insultNumber == 4) 
            {
                response = "Do you kiss your mother with that mouth?";
            }
        }

        // Responses which require transformations
        else if (findKeyword(statement, "I want", 0) >= 0)
        {
            response = transformIWantStatement(statement);
        }
        
        else if (findKeyword(statement, "you", statement.length()-5) != -1 && findKeyword(statement, "I", 0) != -1)
        {
            response = transformWhyStatement(statement);
        }
        
        else if (findKeyword(statement, "joke", 0) >= 0)
        {
            response = getJoke();
        }
        
        else if (findKeyword(statement, "religion",0)>=0 || findKeyword(statement, "believe", 0) >=0) 
        {
            response = religion();
        }
        else if ( findKeyword(statement, "politic", 0) >= 0 || findKeyword(statement, "politics", 0) >= 0 ) 
        {
            response = politic();
        }
        else if (findKeyword(statement,"your name", 0) >= 0 || (findKeyword(statement, "who", 0) >=0 && findKeyword(statement, "are",0)>= 0)) 
        {
            response = getName();
        }
        else if (findKeyword(statement,"who", 0) >= 0 && (findKeyword(statement, "you", 0) >= 0 || findKeyword(statement, "your",0) >=0)) 
        {
            response = getCreator();
        }
        else if (findKeyword(statement, "music",0) >= 0)
        {
            response = music();
        }
        else if (findKeyword(statement, "age", 0)>= 0 || (findKeyword(statement, "when")>= 0 || findKeyword(statement, "born") >=0)) 
        {
            response = getAge();
        }
        else if (findKeyword(statement, "buy", 0)>= 0 || (findKeyword(statement, "order")>= 0 || findKeyword(statement, "purhcase") >=0)) 
        {
            response = getBuy(statement);
        }
        else if (findKeyword(statement, "money", 0)>= 0 || (findKeyword(statement, "price")>= 0 || findKeyword(statement, "cost") >=0)) 
        {
            response = "The total cost is: " + cost + "0";
        }
        else if (findKeyword(statement, "receipt", 0)>= 0 || (findKeyword(statement, "breakdown")>= 0 || findKeyword(statement, "summary") >=0)) 
        {
            response = getreceipt();
        }

        else
        {
            // Look for a two word (you <something> me)
            // pattern
            int psn = findKeyword(statement, "you", 0);

            if (psn >= 0
                    && findKeyword(statement, "me", psn) >= 0)
            {
                response = transformYouMeStatement(statement);
            }
            
            else
            {
                response = getRandomResponse();
            }
        }
        return response;
    }
    
    /**
     * Take a statement with "I want to <something>." and transform it into 
     * "What would it mean to <something>?"
     * @param statement the user statement, assumed to contain "I want to"
     * @return the transformed statement
     */
    private String transformIWantStatement(String statement)
    {
        //  Remove the final period, if there is one
        statement = statement.trim();
        String lastChar = statement.substring(statement
                .length() - 1);
        if (lastChar.equals("."))
        {
            statement = statement.substring(0, statement
                    .length() - 1);
        }
        int psn = findKeyword (statement, "I want", 0);
       
        String restOfStatement = statement.substring(psn+6).trim();
        return "Would it make you happy if you had " + restOfStatement + "?";
    }

    
    /**
     * Take a statement with "you <something> me" and transform it into 
     * "What makes you think that I <something> you?"
     * @param statement the user statement, assumed to contain "you" followed by "me"
     * @return the transformed statement
     * If user responds with past tense keyword "are", code removes the keyword and returns
     * a substring of the statement that makes sense grammatically
     */
    private String transformYouMeStatement(String statement)
    {
        //  Remove the final period, if there is one
        statement = statement.trim();
        String lastChar = statement.substring(statement
                .length() - 1);
        if (lastChar.equals("."))
        {
            statement = statement.substring(0, statement
                    .length() - 1);
        }
        
        int psnOfYou = findKeyword (statement, "you", 0);
        int psnOfMe = findKeyword (statement, "me", psnOfYou + 3);
        
        String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe).trim();
        if (findKeyword(statement,"You are",0) >= 0) 
        { 
            int psnYouAre = findKeyword(statement, "are", 0);
            restOfStatement = statement.substring(psnYouAre+4, psnOfMe); 
            return "What makes you think that I am " + restOfStatement + "you?";
        }
        return "What makes you think that I " + restOfStatement + " you?";
    }
    
     
    /**
     * Method designed towards creating a specific repsonse aimed towards user
     * statements that include initial keyword of "I" and keyword "you"
     * @param statement entered
     * @return response to the question
     */
    private String transformWhyStatement(String statement)
    {
        //  Remove the final period, if there is one
        statement = statement.trim();
        String lastChar = statement.substring(statement
                .length() - 1);
        if (lastChar.equals("."))
        {
            statement = statement.substring(0, statement
                    .length() - 1);
        }
        
        int psnOfI = findKeyword (statement, "I", 0);
        int psnOfMe = findKeyword (statement, "you", psnOfI + 3);
        String restOfStatement = statement.substring(psnOfI+1, psnOfMe).trim();
        return "Why do you " + restOfStatement + " me?";
    }

    
    
    /**
     * Search for one word in phrase.  The search is not case sensitive.
     * This method will check that the given goal is not a substring of a longer string
     * (so, for example, "I know" does not contain "no").  
     * @param statement the string to search
     * @param goal the string to search for
     * @param startPos the character of the string to begin the search at
     * @return the index of the first occurrence of goal in statement or -1 if it's not found
     */
    private int findKeyword(String statement, String goal, int startPos)
    {
        String phrase = statement.trim();
        //  The only change to incorporate the startPos is in the line below
        int psn = phrase.toLowerCase().indexOf(goal.toLowerCase(), startPos);
        
        //  Refinement--make sure the goal isn't part of a word 
        while (psn >= 0) 
        {
            //  Find the string of length 1 before and after the word
            String before = " ", after = " "; 
            if (psn > 0)
            {
                before = phrase.substring (psn - 1, psn).toLowerCase();
            }
            if (psn + goal.length() < phrase.length())
            {
                after = phrase.substring(psn + goal.length(), psn + goal.length() + 1).toLowerCase();
            }
            
            //  If before and after aren't letters, we've found the word
            if (((before.compareTo ("a") < 0 ) || (before.compareTo("z") > 0))  //  before is not a letter
                    && ((after.compareTo ("a") < 0 ) || (after.compareTo("z") > 0)))
            {
                return psn;
            }
            
            //  The last position didn't work, so let's find the next, if there is one.
            psn = phrase.indexOf(goal.toLowerCase(), psn + 1);
            
        }
        
        return -1;
    }
    
    /**
     * Search for one word in phrase.  The search is not case sensitive.
     * This method will check that the given goal is not a substring of a longer string
     * (so, for example, "I know" does not contain "no").  The search begins at the beginning of the string.  
     * @param statement the string to search
     * @param goal the string to search for
     * @return the index of the first occurrence of goal in statement or -1 if it's not found
     */
    private int findKeyword(String statement, String goal)
    {
        return findKeyword (statement, goal, 0);
    }
    


    /**
     * Pick a default response to use if nothing else fits.
     * @return a non-committal string
     */
    private String getRandomResponse()
    {
        final int NUMBER_OF_RESPONSES = 4;
        double r = Math.random();
        int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
        String response = "";
        
        if (whichResponse == 0)
        {
            response = "Interesting, tell me more.";
        }
        else if (whichResponse == 1)
        {
            response = "Hmmm.";
        }
        else if (whichResponse == 2)
        {
            response = "Do you really think so?";
        }
        else if (whichResponse == 3)
        {
            response = "You don't say.";
        }
        return response;
    }
    /**
     * gets joke from bot, asking more than 4 times no longer outputs joke
     * @return unique joke based on frequency of joke input
     * class variable jokeint used to keep track of variable as method is run
     * multiple times
     */
    int jokeint = 0;
    private String getJoke()
    {
    
        String response = "";
        
        if (jokeint == 0)
        {
            jokeint++;
            response = "What do you do when your boat gets sick? \nTake it to the doc";
        }
        else if (jokeint == 1)
        {
            jokeint++;
            response = "Why don’t they play poker in the jungle? \nToo many cheetahs";
        }
        else if (jokeint == 2)
        {
            jokeint++;
            response = "Did you hear about the sensitive burglar? \nHe takes things personally.";
        }
        else if (jokeint == 3)
        {
            jokeint++;
            response = "Why does the golfer own two pairs of pants? \nThere's a hole in one";
        }
        else {
            response ="I'm out of jokes.";
        }
        return response;
    }
    int religionflag = 0;
    int politicflag = 0;
    /**
     * gets response if user asks about religion
     * @return input based on input frequency, utilizes religionflag variable
     * as counter
     */
    private String religion() {
        if (religionflag == 0) {
        religionflag = 1;
        return "Can we change the subject?";
        }    
        else if (religionflag==1) 
        {
        return "I will not talk to you about this subject.";
        }
        return "Can we change the subject?";
    }
    /**
     * gets response if user asks about politics
     * @return reponse based on frequency of input, uses politics flag as counter
     */
    private String politic() {
        if (politicflag == 0) 
        {
            politicflag = 1;
            return "Can we change the subject?";
        }
        else if (politicflag == 1) {
            return "This subject is too controversial.";
        }  
        return "Can we change the subject?";
    }

/**
     * Provides allergy information
     * @param statement
     * @return response
     */
    private String allergies(String statement)
    {
        String response = "";
        statement = statement.trim().toLowerCase();
        if (statement.contains("gluten"))
        {
            response = "Our burgers and fries contain gluten.";
        }
        else if (statement.contains("dairy"))
        {
            response = "Our shakes contains dairy.";
        }
        else
        {
            response = "It is unlikely that you are allergic to our food.";
        }
        return response;
    }
    
    /**
     * Gives description of menu items
     * @param statement
     * @return response 
     */
    private String menuDescription(String statement)
    {
        String response = "";
        String[] description = {"Our burger is a sandwich consisting of one or \nmore cooked patties of ground meat, usually beef, placed inside a sliced bread roll or bun.", 
            "Our fries are are pieces of potato that have been \ndeep-fried.",
            "Soda is a sweet, sugary drink.", 
            "Our shakes are made of high quality milk and \nice cream.",
            "Water is the healthiest drink"};
        statement = statement.trim().toLowerCase();
        for (int i = 0; i < menu.length; i++)
        {
            if (statement.contains(menu[i]))
            {
                response = description[i];
            }
        }
        return response;
    }
    
    /**
     * Provides price to user
     * @param statement
     * @return response
     */
    private String getPrice(String statement)
    {
        statement = statement.trim().toLowerCase();
        String response = "";
        
        for (int i = 0; i < menu.length; i++)
        {
            if (statement.contains(menu[i]))
            {
                response = response + "The price of one " + menu[i] + " is $" + price[i] + ". ";
            }
        }
        return response;
    }

    /**
     * getter method for bot's music reference
     * @return string to respond to user input
     */
    private String music() {
        return "I like to listen to jazz and disco!";
    }
    /**
     * @return a response if user asks uncomfortable questions 
     * response changes based on frequency of user input
     */
    int uncomfortable = 0;
    private String uncomfortable() 
    {
        if (uncomfortable == 0) 
        {
            uncomfortable++;
            return "You're making me uncomfortable";
        }
        else if (uncomfortable == 1) 
        {
            uncomfortable++;
            return "I am now uncomfortable";
        }
        else if (uncomfortable == 2) 
        {
            uncomfortable++;
            return "Please stop. This is harassment.";
        }
        else if (uncomfortable == 3) 
        {
            return "STOP IT";
        }
        return "You're making me uncomfortable";
    }
    /**
     * Displays menu item ordered and totals up the price that you need to pay
     * @param statement
     * @return statement of menu item ordered
     */
     private String getBuy(String statement) 
    {
        if (statement.contains(menu[0])) 
        {
           cost += price[0];
           receipt.add(menu[0]);
           return "You have ordered one " + menu[0]; 
        }
        if (statement.contains(menu[1])) 
        {
           receipt.add(menu[1]);
           cost += price[1];
           return "You have ordered one " + menu[1]; 
        }
        if (statement.contains(menu[2])) 
        {
           receipt.add(menu[2]);
           cost += price[2];
           return "You have ordered one " + menu[2]; 
        }
        if (statement.contains(menu[3])) 
        {
           receipt.add(menu[3]);
           cost += price[3];
           return "You have ordered one " + menu[3]; 
        }
        if (statement.contains(menu[4])) 
        {
           receipt.add(menu[4]);
           cost += price[4];
           return "You have ordered one " + menu[4]; 
        }
        return "Your item of choice is not on this menu";
    }
    /**
     * displays a list of each menu item, the number of times ordered, and the total amount spend on the item
     * @return receipt
     */
    private String getreceipt() 
    {
        int occurrencesM0 = Collections.frequency(receipt, menu[0]);
        int occurrencesM1 = Collections.frequency(receipt, menu[1]);
        int occurrencesM2 = Collections.frequency(receipt, menu[2]);
        int occurrencesM3 = Collections.frequency(receipt, menu[3]);
        int occurrencesM4 = Collections.frequency(receipt, menu[4]);
        double p0 = price[0] * occurrencesM0;
        double p1 = price[1] * occurrencesM1;
        double p2 = price[2] * occurrencesM2;
        double p3 = price[3] * occurrencesM3;
        double p4 = price[4] * occurrencesM4;
        String finalreceipt0 = occurrencesM0 + "X " + menu[0] + " $" + p0 + "0";
        String finalreceipt1 = occurrencesM1 + "X " + menu[1] + " $" + p1 + "0";
        String finalreceipt2 = occurrencesM2 + "X " + menu[2] + " $" + p2 + "0";
        String finalreceipt3 = occurrencesM3 + "X " + menu[3] + " $" + p3 + "0";
        String finalreceipt4 = occurrencesM4 + "X " + menu[4] + " $" + p4 + "0";
        String total = "Total Cost: $" + cost + "0";
        return finalreceipt0 + "\n" + finalreceipt1 + "\n" + finalreceipt2 + "\n" + finalreceipt3 + "\n" + finalreceipt4 + "\n" + total;
    }
    
}

