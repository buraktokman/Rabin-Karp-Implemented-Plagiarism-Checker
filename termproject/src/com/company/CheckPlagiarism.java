/* #-------------------------------------------------------------------------------
Course		: CMP3005 - Analysis of Algorithms
Purpose   	: Term Project - Plagiarism Checker

Created   	: 2020 Dec 28
Modified   	: 2021 Jan 8
Author(s)	: Ercüment Burak Tokman (1315490)
              Sertaç Bağcı  (1802527)
              İbrahim Barış Mumyakmaz (1804352)
Reference	:
#------------------------------------------------------------------------------- */

package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CheckPlagiarism extends Main{

    public final static int CONSECUTIVE_WORD_COUNT = 5;     // 5 and above words considered as plagiarism
    public final static int SKIP_WORD_LENGTH = 1;           // Skip if word is 1 or shorter
    public final static int Q = 127;                        // Prime Number for Hash Func.
    public final static int K = 256;                        // Alphabet size
    public final static String STOPWORDS = readStopwords(); // Load Stopwords (he, she, it...)

    public static void run(int documentNo, String mainContent, String compareContent){

        // SPLIT TO SENTENCES
        String[] mainSentences = getSentences(mainContent);
        String[] comparisonSentences = getSentences(compareContent);

        // STORE EACH SENTENCE WITH MATCH COUNT
        Map<String, Float> hm = new HashMap<String, Float>();
        // TOTAL MATCH COUNT
        int PlagiarismCount = 0;

        /*
        *   BEGIN - ALGORITHM
        */

        // Iterate over each sentence of MAIN document
        for (String mainSentence: mainSentences){

            // Split MAIN sentence to WORDS
            String[] mainWords = getWords(mainSentence);

            // WORD MATCH COUNT IN COMPARISON SENTENCE
            int wordMatchCounter, previousWordMatchCount;
            wordMatchCounter = previousWordMatchCount = 0;

            // MAIN SENTENCE PLAGIARISED OR NOT
            boolean sentencePlagiarised = false;

            // CHECK EACH SENTENCE of COMPARISON document
            for (String comparisonSentence: comparisonSentences) {
                // REMOVE SPACES
                comparisonSentence = removeSpaces(comparisonSentence);
                int comparisonSentenceLength = comparisonSentence.length();

                // RESET PATTERN MATCH COUNTER
                if (wordMatchCounter > previousWordMatchCount)
                    previousWordMatchCount = wordMatchCounter;
                wordMatchCounter = 0;

                /*
                    APPLY RABIN KARP
                 */

//                System.out.println("\n\nMAIN:\t\t" + mainSentence);
//                System.out.println("COMPARISON:\t" + comparisonSentence);

                // Pick a WORD from MAIN sentence as PATTERN
                for (String word: mainWords){
//                  System.out.println("SEARCHING: " + word);
                    boolean patternFound = false;
                    int i,j;
                    int h = 1;
                    int hashWord = 0;
                    int hashTextWindow = 0;

                    // GET WORD (pattern) LENGTH
                    int PatternLength = word.length();

                    // SKIP IF WORD SHORTER THAN 3
                    // SKIP IF WORD IS LARGER THEN COMPARISON SENTENCE
                    if (SKIP_WORD_LENGTH > PatternLength || word.length() > comparisonSentenceLength)
                        continue;

                    // ROLLING-OVER
                    for (i = 0; i < PatternLength - 1; i++)
                        h = (h * K) % Q;

                    // HASH the MainWord & TextWindow
                    for (i = 0; i < PatternLength; i++){
                        hashWord = (K * hashWord + word.charAt(i)) % Q;
                        hashTextWindow = (K * hashTextWindow + comparisonSentence.charAt(i)) % Q;
                    }

                    // CHECK HASH & PATTERN IN WINDOW
                    for (i = 0; i <= comparisonSentenceLength - PatternLength; i++){
                        // CHECK IF HASHES MATCH
                        if ( hashWord == hashTextWindow ){
//                             System.out.println("HASH MATCH! " + hashWord + " = " + hashTextWindow);

                            // CHECK EACH CHARACTER IF WORD AND TEXT REALLY MATCH
                            for (j = 0; j < PatternLength; j++){
                                if (comparisonSentence.charAt(i + j) != word.charAt(j))
                                    break;
                            }
                            if (j == PatternLength) {
//                                System.out.println("PATTERN MATCH! " + word);
                                // Count
                                wordMatchCounter++;
                                // PATTERN MATCHED. EXIT & PICK NEW WORD
                                patternFound = true;
                                break;
                            }
                        }
                        // EXIT PATTERN SEARCH
                        if (patternFound == true)
                            break;

                        // SHIFT WINDOW TO RIGHT & GET HASH
                        if ( i < comparisonSentenceLength - PatternLength )
                        {
                            hashTextWindow = (K * (hashTextWindow - comparisonSentence.charAt(i) * h) + comparisonSentence.charAt(i + PatternLength)) % Q;
                            // IF HASH IS NEGATIVE
                            if (hashTextWindow < 0)
                                hashTextWindow = (hashTextWindow + Q);
                        }
                    }
                }

                // COUNT AS PLAGIARISED
                if (wordMatchCounter >= CONSECUTIVE_WORD_COUNT && sentencePlagiarised == false){
//                    System.out.println("PLAGIARISED !!");
                    sentencePlagiarised = true;
                    PlagiarismCount++;
                }
                // Put MAIN sentence to HashMap with SIMILARITY RATE
                if (wordMatchCounter > previousWordMatchCount){
//                    System.out.println("MatchCount: " + wordMatchCounter + "\nWordCount: " + mainWords.length);
//                    System.out.println("PreviousMAtchCount: " + previousWordMatchCount);
//                    System.out.println("sim: " + (float) wordMatchCounter / mainWords.length);
                    // Insert
                    hm.put(mainSentence, (float) wordMatchCounter / mainWords.length);
                }
            }
        }

        /*
        *   END
        */




        // SORT HASHMAP DESCENDING
        // https://howtodoinjava.com/java/sort/java-sort-map-by-values/
        LinkedHashMap<String, Float> reverseSortedMap = new LinkedHashMap<>();
        hm.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        // CALCULATE SIMILARITY RATE OF THE DOCUMENT
        float similarityRate = (float) PlagiarismCount*100 / mainSentences.length;
//        System.out.println("total sentence count: " + mainSentences.length);
//        System.out.println("plagia count: " + PlagiarismCount + "\n\n");

        // PRINT RESULTS
        System.out.printf("DOCUMENT: %d\nSIMILARITY RATE: %.2f%%\nRATE\tSENTENCE\n", documentNo, similarityRate);
        int i = 0;
        for (Map.Entry me : reverseSortedMap.entrySet()) {
            System.out.printf("%.2f\t%s\n", me.getValue(), me.getKey());
            if (i == 4)
                break;
            i++;
        }
        System.out.println("\n");
    }




    // HASH THE STRING -- INCOMPLETE
    public static int getHash(String text){
        return 1;
    }

    // SPLIT TEXT TO SENTENCES & TRIM
    public static String[] getSentences(String content){
        // Split
        String[] sentences2 = content.trim().split("\n;|((?<!\\d)\\.(?!\\d))");
        // Remove nulls
        // https://stackoverflow.com/questions/31583523/best-way-to-remove-null-values-from-string-array
        // https://stackoverflow.com/questions/56127505/how-to-remove-digits-and-whitespaces-from-a-stream
        String[] sentences = Arrays.stream(sentences2)
                .filter(s -> (s != null && s.length() > 0))
                // Upper to lowercase
                .map(String::toLowerCase)
                // Remove Punctuation
                .map(s->s.replaceAll("\\p{P}", ""))
                // Remove Nextline
                .map(s->s.replaceAll("\n", " "))
                // Trim
                .map(String::trim)
                .toArray(String[]::new);
        return sentences;
    }

    // GET WORDS FROM SENTENCE
    public static String[] getWords(String sentence){
        // Split to words
        String[] words = sentence.split("\\s+");

        // Skip stopwords (it, he, can, do...)
        StringBuilder cleaned = new StringBuilder();
        for(String word : words) {
            if(!STOPWORDS.contains(word)) {
                cleaned.append(word);
                cleaned.append(' ');
            }
        }
        return cleaned.toString().trim().split("\\s+");
    }

    // REMOVE WHITE SPACES FROM SENTENCE
    public static String removeSpaces(String sentence){
        return sentence.replaceAll("\\s+", "");
    }

    // READ STOPWORDS FROM FILE
    public static String readStopwords(){
        String stopwords = "";
        try{
            stopwords = new String(Files.readAllBytes(Paths.get("res/english_stopwords.txt")), StandardCharsets.UTF_8);
        }catch (IOException e){
            System.out.println("ERROR: Cannot read stopwords file:\n" + e);
        }
        return stopwords;
    }


}

