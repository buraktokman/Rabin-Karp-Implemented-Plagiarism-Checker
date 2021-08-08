/* #-------------------------------------------------------------------------------
Course		: CMP3005 - Analysis of Algorithms
Purpose   	: Term Project - Plagiarism Checker

Created   	: 2020 Dec 21
Author		: Ercument Burak Tokman 1315490
Reference	:

#------------------------------------------------------------------------------- */

package com.company;

import java.util.*;

public class CheckPlagiarismBruteforce extends Main{
//    public CheckPlagiarism(String mainContent, String compareContent){
////        super(mainContent, compareContent);
//    }

    public static void run(int documentNo, String mainContent, String compareContent){

        // SPLIT TO SENTENCES
        String[] main = getSentences(mainContent);
        String[] compare = getSentences(compareContent);



        /*
        *
        *   BEGIN - ALGORITHM -> BRUTE FORCE
        *
        */

        // HashMap to store occurrence of sentences
        Map<String, Integer> hm = new HashMap<String, Integer>();
        int totalMatchCount = 0;

        // Iterate over each sentence of MAIN document
        for(int i=0; i<main.length; i++){

            int singleMatchCount = 0;
            // Iterate over each sentence of COMPARISON document
            for (int j=0; j<compare.length; j++){

                if (main[i].equals(compare[j])){
//                  System.out.println("i: "+i+"\tj:"+j+"\tMATCH\t"+ main[i]);
                    // System.out.println("Compare: " + compare[j]);

                    // Count
                    totalMatchCount++;
                    singleMatchCount++;
                    }
                }
            // Put to HashMap
            if (singleMatchCount != 0)
                hm.put(main[i], singleMatchCount);
        }

        /*
        *   END
        */




        // Sort DESCENDING
        // https://howtodoinjava.com/java/sort/java-sort-map-by-values/
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        hm.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));


        // SIMILARITY RATE
        float similarityRate = (float) totalMatchCount*100 / main.length;
//      System.out.println(matchCount + " / " + main.length);

        // PRINT RESULTS
        System.out.printf("%d) Similarity: %.2f%%\n", documentNo, similarityRate);
        int i = 0;
        for (Map.Entry me : reverseSortedMap.entrySet()) {
            System.out.println(me.getValue() + "\t" + me.getKey());
            if (i == 4)
                break;
            i++;
        }
        System.out.println("\n");
    }

    // SORT MAP

    // SPLIT & TRIM SENTENCES
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
                // Trim
                .map(String::trim)
                .toArray(String[]::new);
        return sentences;
    }

    // REMOVE WHITE SPACES FROM SENTENCE
    public static String removeWhitespaces(String sentence){
        return sentence.replace("\\s+", "");
    }


}

