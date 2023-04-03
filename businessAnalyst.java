import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.ArrayDeque;

public class businessAnalyst {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        ArrayDeque<String> queue = new ArrayDeque<>(); // Initialize a Queue to store user commands in order
        List<String> zipCodes; // Declare a List variable to hold zip codes
        if (args[1].equals("AL")) { // Check if the second command line argument is "AL"
            zipCodes = new ArrayList<String>(); // If so, initialize the zip codes as an ArrayList
        } 
        else {
            zipCodes = new LinkedList<String>(); // Otherwise, initialize the zip codes as a LinkedList
        }
        System.out.println("Enter Command (or 'quit' to exit): ");
        String input = scanner.nextLine(); // Read user input from the console
        while (!input.toLowerCase().equals("quit")) { // Repeat until the user types "quit"
            if (!input.equals("history")) {
                queue.add(input); // Add user command to the queue if it is not "history"
            }
            String[] inputArray = input.split(" "); // Split user input into an array of strings by spaces
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) { // Open the file specified in the first command line argument for reading
                br.readLine(); // Skip the first line of the file (the header)
                List<String> businessTypes;
                List<String> neighborhoods;
                List<String> naicsCodes;
                List<String> totalZipCodes;
                if (args[1].equals("AL")) { // Check if the second command line argument is "AL"
                    businessTypes = new ArrayList<String>(); // initialize the business types ArrayList
                    neighborhoods = new ArrayList<String>(); // initialize the neighborhoods ArrayList
                    naicsCodes = new ArrayList<String>(); // initialize the NAICS codes ArrayList
                    totalZipCodes = new ArrayList<String>(); // initialize the total zip codes ArrayList

                }
                else {
                    businessTypes = new LinkedList<String>(); // initialize the business types LinkedList
                    neighborhoods = new LinkedList<String>(); // initialize the neighborhoods LinkedList
                    naicsCodes = new LinkedList<String>(); // initialize the NAICS codes LinkedList
                    totalZipCodes = new LinkedList<String>(); // initialize the total zip codes LinkedList
                }
                int zipTotalBusinesses = 0; // Initialize variable to hold the total number of businesses in the specified zip code
                int zipTotalBusinessTypes = 0; // Initialize variable to hold the total number of business types in the specified zip code
                int zipTotalNeighborhoods = 0; // Initialize variable to hold the total number of neighborhoods in the specified zip code
                int naicsNeighborhoods = 0; // Initialize variable to hold the total number of neighborhoods for the specified NAICS code
                int naicsZipCodes = 0; // Initialize variable to hold the total number of zip codes for the specified NAICS code
                int naicsTotalBusinesses = 0; // Initialize variable to hold the total number of businesses for the specified NAICS code
                int total = 0; // Initialize variable to hold the total number of businesses
                int closed = 0; // Initialize variable to hold the total number of closed businesses
                int openedLastYear = 0; // Initialize variable to hold the total number of newly opened businesses
                while ((line = br.readLine()) != null) {
                    /* zip */
                    if (inputArray[0].toLowerCase().equals("zip")) {
                        String zipCode = inputArray[1]; // User zipcode
                        String[] column = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Splitting the line into its columns
                        String zip = column[7];
                        if (zip.equals("")) {
                            continue;
                        }
                        if (!zipCodes.contains(zip)) { // checks if the List contains the zip code
                            zipCodes.add(zip); // if not then add 
                        }
                        if (zip.equals(zipCode)) { 
                            zipTotalBusinesses++; // total businesses in zip code increments 
                            String businessDescription = column[17];
                            if (!businessDescription.equals("")) { 
                                /* add unique businessDescription */
                                if (!businessTypes.contains(businessDescription)) { // checks if List contains BusinessType
                                    businessTypes.add(businessDescription); // if not then add
                                    zipTotalBusinessTypes++;  
                                }
                            }
                            String neighborhood = column[23];
                            if (!neighborhood.equals("")) {
                                /* add unique neighborhoods */
                                if (!neighborhoods.contains(neighborhood)) { // checks if List contains Neighborhood
                                    neighborhoods.add(neighborhood); // if not then add
                                    zipTotalNeighborhoods++;
                                }
                            }
                        }
                    }
                    if (inputArray[0].toUpperCase().equals("NAICS")) {
                        String[] column = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Splitting the line into its columns
                        String inputCode = inputArray[1]; // User NAICS input
                        int inputCodeInt = Integer.parseInt(inputCode); // turn inputCode from string into integer
                        String codes = column[16];
                        if (codes.equals("")) {
                            continue;
                        }
                        if (!naicsCodes.contains(codes)) { // checks if List contains NAICS coed
                            naicsCodes.add(codes); // if not add
                        }
                        // this part was my downfall and gave me anxiety for 20 years
                        for (int i = 0; i < naicsCodes.size(); i++) { 
                            String[] splitCode = naicsCodes.get(i).split("[\\s-]+"); // splits the ranges into two
                            int lowerBound = Integer.parseInt(splitCode[0]); // turns lowerbound into int
                            int upperBound = Integer.parseInt(splitCode[1]); // turns upperbound into int
                            if (inputCodeInt >= lowerBound && inputCodeInt <= upperBound) { // checks is user input is between range

                                naicsTotalBusinesses++; // increments total businesses that is in the range
                                String naicsZip = column[7];
                                if (!naicsZip.equals("")) {
                                    /* add unique businessDescription */
                                    if (!totalZipCodes.contains(naicsZip)) { // checks if list contains zip
                                        totalZipCodes.add(naicsZip); // if not then add
                                        naicsZipCodes++; // increment total amount of zipcodes for businessType
                                    }
                                    String neighborhood = column[23];
                                    if (!neighborhood.equals("")) {
                                    /* add unique neighborhoods */
                                        if (!neighborhoods.contains(neighborhood)) { // checks if list contains neighborhood
                                            neighborhoods.add(neighborhood); // if not then add
                                            naicsNeighborhoods++; // increment total amount of neighborhoods per businesstype
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /* closed and new businesses */
                    if (inputArray[0].toLowerCase().equals("summary")) {
                        String[] column = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); 
                        String endDate = column[9];
                        String startDate = column[8];
                        if (endDate != "") { // if business does not have enddate
                            closed++; // closed businesses increments
                        }
                        String year = startDate.substring(startDate.length() - 4);
                        if (year.equals("2022")) { // if business opened in 2022
                            openedLastYear++; // businesses last year increments
                        }
                    }
                    total++; // increment total businesses in entire file
                }
                if (inputArray[0].toLowerCase().equals("zip") && inputArray[2].toLowerCase().equals("summary")) {
                    System.out.println(inputArray[1] + " Business Summary");
                    System.out.println("Total Businesses: " + zipTotalBusinesses);
                    System.out.println("Business Types: " + zipTotalBusinessTypes);
                    System.out.println("Neighborhood: " + zipTotalNeighborhoods);
                }
                if (inputArray[0].toUpperCase().equals("NAICS") && inputArray[2].toLowerCase().equals("summary")) {
                    System.out.println("Total Businesses: " + naicsTotalBusinesses);
                    System.out.println("Zip Codes: " + naicsZipCodes);
                    System.out.println("Neighborhood: " + naicsNeighborhoods);
                }
                if (inputArray[0].toLowerCase().equals("summary")) {
                    System.out.println("Total Businesses: " + total);
                    System.out.println("Closed Businesses: " + closed);
                    System.out.println("New Businesses: " + openedLastYear);
                }
                if (inputArray[0].toLowerCase().equals("history")) {
                    for (String command : queue) {
                        System.out.println();
                        System.out.println(command);
                        System.out.println();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Enter Command (or 'quit' to exit): ");
            input = scanner.nextLine();
        }
        scanner.close();
    }
}