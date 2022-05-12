package de.swe.oo.test.tests;

import de.swe.oo.client.connection.ConnectionManager;
import de.swe.oo.test.bots.ChatBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class BotTest {
    public static void main(String[] args) {
        String HOST = "localhost";
        int PORT = 4444;
        int minWait = 20;
        int maxWait = 100;
        int logoutChancePercent = 3;
        String[] NAMES = {"Jannik", "Korbin", "Gerhard", "Luisa", "Anna", "Jaqueline"};
        String[] MANYNAMES = {"Michael", "Christopher", "Jessica", "Matthew", "Ashley", "Jennifer", "Joshua",
                "Amanda", "Daniel", "David", "James", "Robert", "John", "Joseph", "Andrew", "Ryan", "Brandon",
                "Jason", "Justin", "Sarah", "William", "Jonathan", "Stephanie", "Brian", "Nicole", "Nicholas",
                "Anthony", "Heather", "Eric", "Elizabeth", "Adam", "Megan", "Melissa", "Kevin", "Steven", "Thomas",
                "Timothy", "Christina", "Kyle", "Rachel", "Laura", "Lauren", "Amber", "Brittany", "Danielle",
                "Richard", "Kimberly", "Jeffrey", "Amy", "Crystal", "Michelle", "Tiffany", "Jeremy", "Benjamin",
                "Mark", "Emily", "Aaron", "Charles", "Rebecca", "Jacob", "Stephen", "Patrick", "Sean", "Erin",
                "Zachary", "Jamie", "Kelly", "Samantha", "Nathan", "Sara", "Dustin", "Paul", "Angela", "Tyler",
                "Scott", "Katherine", "Andrea", "Gregory", "Erica", "Mary", "Travis", "Lisa", "Kenneth", "Bryan",
                "Lindsey", "Kristen", "Jose", "Alexander", "Jesse", "Katie", "Lindsay", "Shannon", "Vanessa",
                "Courtney", "Christine", "Alicia", "Cody", "Allison", "Bradley", "Samuel", "Shawn", "April",
                "Derek", "Kathryn", "Kristin", "Chad", "Jenna", "Tara", "Maria", "Krystal", "Jared", "Anna",
                "Edward", "Julie", "Peter", "Holly", "Marcus", "Kristina", "Natalie", "Jordan", "Victoria",
                "Jacqueline", "Corey", "Keith", "Monica", "Juan", "Donald", "Cassandra", "Meghan", "Joel", "Shane",
                "Phillip", "Patricia", "Brett", "Ronald", "Catherine", "George", "Antonio", "Cynthia", "Stacy",
                "Kathleen", "Raymond", "Carlos", "Brandi", "Douglas", "Nathaniel", "Ian", "Craig", "Brandy", "Alex",
                "Valerie", "Veronica", "Cory", "Whitney", "Gary", "Derrick", "Philip", "Luis", "Diana", "Chelsea",
                "Leslie", "Caitlin", "Leah", "Natasha", "Erika", "Casey", "Latoya", "Erik", "Dana", "Victor", "Brent",
                "Dominique", "Frank", "Brittney", "Evan", "Gabriel", "Julia", "Candice", "Karen", "Melanie", "Adrian",
                "Stacey", "Margaret", "Sheena", "Wesley", "Vincent", "Alexandra", "Katrina", "Bethany", "Nichole",
                "Larry", "Jeffery", "Curtis", "Carrie", "Todd", "Blake", "Christian", "Randy", "Dennis", "Alison",
                "Trevor", "Seth", "Kara", "Joanna", "Rachael", "Luke", "Felicia", "Brooke", "Austin", "Candace",
                "Jasmine", "Jesus", "Alan", "Susan", "Sandra", "Tracy", "Kayla", "Nancy", "Tina", "Krystle", "Russell",
                "Jeremiah", "Carl", "Miguel", "Tony", "Alexis", "Gina", "Jillian", "Pamela", "Mitchell", "Hannah",
                "Renee", "Denise", "Molly", "Jerry", "Misty", "Mario", "Johnathan", "Jaclyn", "Brenda", "Terry",
                "Lacey", "Shaun", "Devin", "Heidi", "Troy", "Lucas", "Desiree", "Jorge", "Andre", "Morgan", "Drew",
                "Sabrina", "Miranda", "Alyssa", "Alisha", "Teresa", "Johnny", "Meagan", "Allen", "Krista", "Marc",
                "Tabitha", "Lance", "Ricardo", "Martin", "Chase", "Theresa", "Melinda", "Monique", "Tanya", "Linda",
                "Kristopher", "Bobby", "Caleb", "Ashlee", "Kelli", "Henry", "Garrett", "Mallory", "Jill", "Jonathon",
                "Kristy", "Anne", "Francisco", "Danny", "Robin", "Lee", "Tamara", "Manuel", "Meredith", "Colleen",
                "Lawrence", "Christy", "Ricky", "Randall", "Marissa", "Ross", "Mathew", "Jimmy"};
        String[] MESSAGES = {"Hallo", "Hey", "Toller Chat.", "Das ist ein wirklich guter Chat."};
        LinkedList<ChatBot> botList = new LinkedList<ChatBot>();

        try {
            sleep(200);  //Bots currently just shut down when the login doesn't work, so we have to wait for
                                // the server to be ready before we log in.
        } catch (InterruptedException e) {
            System.err.println("Error trying to sleep while setting up server. " + e.getMessage());
        }
        for (String name : MANYNAMES) {
            ChatBot newBot = new ChatBot(HOST, PORT, name, MESSAGES, minWait, maxWait, logoutChancePercent);
            newBot.start();
            botList.add(newBot);
            try {
                sleep(15);  //As the login prodecure isn't multithreaded currently we have to wait
            } catch (InterruptedException e) {
                System.err.println("Error while sleeping during bot creation.");
            }
        }
    }
}