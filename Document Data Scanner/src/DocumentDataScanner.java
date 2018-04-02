import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class DocumentDataScanner {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream output1 = new PrintStream(new File("OutputData.txt"));

        //Initiates method that scans document for words and sentences, then calculates and outputs the requested information
        Scanner input1 = new Scanner(new File("warw10.txt"));

        String wordsSentences = readWordsAndSentences(input1);
        System.out.println(wordsSentences);
        output1.println(wordsSentences);

        input1 = new Scanner(new File("warw10.txt"));
        String longShort = (sentenceLength(input1));
        System.out.println(longShort);
        output1.println(longShort);

        //Asks the user if they would like to search the document for a specific word
        //If user inputs 'y' it will return the number of times a given word appears in the document
        Scanner console = new Scanner(new File("warw10.txt"));
        Scanner input = new Scanner(System.in); //used for user input
        String gutenHeader = "";
        String find = "";
        String findWordResult = "";

        // prompts for user to interface
        System.out.print("Would you like to search this document for how many times word is used? (y/n) ");
        find = input.next();
        while (!find.equalsIgnoreCase("n") && !find.equalsIgnoreCase("y")) {
            System.out.println("Please enter a valid response. (y/n) ");
            find = input.next();
        }
        if (find.equalsIgnoreCase("y")) {
            System.out.print("Would you like to include the the header in your search? (y/n) ");
            gutenHeader = input.next();
            while (!gutenHeader.equalsIgnoreCase("n") && !gutenHeader.equalsIgnoreCase("y")) {
                System.out.println("Please enter a valid response. (y/n) ");
                gutenHeader = input.next();
            }

            // method launch
            System.out.print("What single word would you like to search for? (Please do not use \" \" unless it part of your search request) ");
            find = input.next();
            findWordResult = findWord(console, find, gutenHeader);


            System.out.println(findWordResult);
            output1.println(findWordResult);
        }
    }

    public static String readWordsAndSentences(Scanner console) throws FileNotFoundException {
        int sentences = 0;
        int words = 0;
        boolean isActualWork = false;
        int actualSentences = 0;
        int actualWords = 0;

        while(console.hasNextLine()) {
            String line = console.nextLine();
            while(line.length() == 0 && console.hasNextLine()) {
                line = console.nextLine();
            }
            Scanner lineScan = new Scanner(line);

            if (isActualWork == false && line.toLowerCase().contains("the war of the worlds")) {
                isActualWork = true;
            }

            //Will make sure that if the line ends with a . or ! or ? or ." or ?"
            //it will count it as a sentence.
            //this code will ignore cases like St. or Dr. or Mrs. as well as abbreviations of names
            if(line.length() != 0) {
                int positionInLine = 0;
                if(line.endsWith(".") || line.endsWith("!") || line.endsWith("?") || line.endsWith(". ") || line.endsWith(".\"") ) {
                    sentences++;
                    if(isActualWork) {
                        actualSentences++;
                    }
                }
                //Most sentences in etexts end with two spaces and this part checks it.
                char first = line.charAt(0);
                while (positionInLine < line.length()) {
                    char second = line.charAt(positionInLine);
                    //will stop at the first period in the line that is not at the end of the line and will check if there are 2 spaces proceeding it.
                    if(second == '.' && !(line.endsWith(".") || (line.endsWith(". ")) || line.endsWith(".\"")   )) {
                        //the range from period to 2 further down to check if it is either a .  or a ."
                        String sentenceCheck = line.substring(positionInLine, positionInLine+3);
                        if(sentenceCheck.equals(".  ") || sentenceCheck.equals(".\" ")) {
                            sentences++;
                            if(isActualWork) {
                                actualSentences++;
                            }
                        }
                    }

                    //cases where sentences don't end in a period
                    if((first == '?' && second == '\'') || (first == '?' && second == '"') ||
                            (first == '!' && second == '"') || (first == '!' && second == ' ')) {
                        sentences++;
                        if(isActualWork) {
                            actualSentences++;
                        }
                    }
                    first = second;
                    positionInLine++;
                }

                while(lineScan.hasNext()) {
                    String words2 = lineScan.next();
                    words++;
                    if(isActualWork) {
                        actualWords++;
                    }
                }
            }
            lineScan.close();
        }

        System.out.println();

        Scanner input1 = new Scanner(new File("warw10.txt"));
        String longShort = (sentenceLength(input1));

        String data = "Report on 'The War of the Worlds' by H(erbert) G(eorge) Wells" +
                "\nDone by CS210(c) Group 1 for In Class Lab Project #2" +
                "\nFile Name: warw10.txt" +
                "\nLocation Information: http://www.textfiles.com/etext/FICTION/warw10.txt" +
                "\nTotal Words of Actual Work: " + actualWords +
                "\nTotal Sentences of Actual Work: " + actualSentences +
                "\nAverage words per sentence: " + (((double)actualWords / (double)actualSentences) / 100 * 100) +
                "\nThe percentage of file not part of work is: " + (((double) words - (double) actualWords) / (double) words) * 100 + "%\n\n";

        return data;
    }

    public static String sentenceLength(Scanner input1) {
        int counter = 0;
        int shortest = 999999999;
        int longest = 0;
        // used to look at the next token
        String peeking = "";
        // used to indicate you looked at the next token
        Boolean peekTest = false;
        String currentLong = "";
        String currentShort = "";
        String currentLine = "";
        String currentWord = "";
        String formatLong = "";
        int i = 0;
        int charaCount = 0;

        Boolean end = false; // indicates end of a sentence
        // scans past the legal jargon header, gets text to starting place
        while (input1.hasNextLine() && !end) {
            currentLine = input1.nextLine();
            if (currentLine.contains("*END*THE SMALL PRINT! FOR PUBLIC DOMAIN ETEXTS*")) {
                end = true;
            }
        }

        end = false;
        currentLine = "";
        while (input1.hasNext() || peekTest) {
            // handles if peek test was used and consumed the token on the last loop
            if (peekTest) {
                currentWord = peeking;
                currentLine = currentLine + " " + currentWord;
                peekTest = false;
            } else {
                currentWord = input1.next();
                currentLine = currentLine + " " + currentWord;
            }
            // increase word count
            counter++;
            // end of sentence tests
            if (currentWord.endsWith(".\"") ||
                    currentWord.endsWith("?\"") ||
                    currentWord.endsWith("!\"") ||
                    currentWord.endsWith("?") ||
                    currentWord.endsWith("!")) {
                end = true;
            } else if (currentWord.endsWith(".")) {
                // corner cases
                if (!currentWord.equalsIgnoreCase("mr.") &&
                        !currentWord.equalsIgnoreCase("mrs.") &&
                        !currentWord.equalsIgnoreCase("ms.") &&
                        !currentWord.equalsIgnoreCase("dr.")&&
                        !currentWord.equalsIgnoreCase("st.")) {
                    if (input1.hasNext()) {
                        // this "peeks" at the next token and handles the extra token
                        // consumption to examine what's on the next token
                        peeking = input1.next();
                        peekTest = true;
                        if (Character.isUpperCase(peeking.charAt(0)) || Character.isDigit(peeking.charAt(0))) {
                            // end of the sentence
                            end = true;
                        }
                    }
                }
            }

            // checks if current is longest/shortest, performs end sentence variable resets
            if (end) {
                if (shortest > counter) {
                    shortest = counter;
                    currentShort = currentLine;
                } else if (longest < counter) {
                    longest = counter;
                    currentLong = currentLine;
                }
                currentLine = "";
                counter = 0;
                end = false;
            }
        }

        // creates a formatted version of longString
        while(currentLong.length() > (i * 60 + 60 + charaCount) || end) {
            formatLong = formatLong + currentLong.substring(i * 60 + charaCount, i * 60 + 60 + charaCount);
            i++;
            // prevents a line from ending midword if it doesn't contain a hyphen
            while (!formatLong.endsWith(" ") && !formatLong.endsWith("-")) {
                formatLong = formatLong + currentLong.substring(i * 60 + charaCount, i * 60 + charaCount + 1);
                // tracks character advances if the line doesn't end on a space or -
                charaCount++;
            }
            formatLong = formatLong + "\n";
            // prints any remaining characters that aren't a full line of 60 characters
            if (currentLong.length() - i * 60 + charaCount < 60) {
                formatLong = formatLong + currentLong.substring(i * 60 + charaCount);
            }
        }

        // removes extra quotation marks from the Strings
        if(!Character.isLetter(currentShort.charAt(0))) {
            currentShort = currentShort.substring(1);
        }
        if(!Character.isLetter(currentShort.charAt(0))) {
            currentLong = currentLong.substring(1);
        }
        if(currentShort.endsWith("\"")) {
            currentShort = currentShort.substring(0, currentShort.length()-1);
        }
        if(currentShort.startsWith("\"")) {
            currentShort = currentShort.substring(1);
        }
        if(currentLong.endsWith("\"")){
            currentLong = currentLong.substring(0, currentLong.length()-1);
        }
        if(currentLong.startsWith("\"")) {
            currentLong = currentLong.substring(1);
        }

        return ("Shortest sentence: " + shortest + " words\n\"" + currentShort +
                "\"\n\nLongest sentence: " + longest + " words\n\"" + formatLong + "\"\n\n");
    }

    public static String findWord(Scanner console, String find, String gutenHeader) {
        int counter = 0;
        String token = "";
        boolean end = false;
        find = find.toLowerCase();

        // checks if the user elected to not include the header in the search
        if (gutenHeader.equalsIgnoreCase("n")) {
            //scans past the legal jargon header, gets text to starting place
            while (console.hasNextLine() && !end) {
                token = console.nextLine();
                if (token.contains("*END*THE SMALL PRINT! FOR PUBLIC DOMAIN ETEXTS*")) {
                    end = true;
                }
            }
            end = false;
            token = "";
        }

        // the search function, works token by token which is a limitation.
        while (console.hasNext()) {
            token = console.next();
            token = token.toLowerCase();
            if (token.contains(find)) {
                counter++;
            }
        }

        return "The word \"" + find + "\" appears " + counter + " times!";
    }
}
