/* #-------------------------------------------------------------------------------
Course		: CMP3005 - Analysis of Algorithms
Purpose   	: Term Project - Plagiarism Checker

Created   	: 2020 Dec 21
Author(s)	: Ercüment Burak Tokman (1315490)
              Sertaç Bağcı  (1802527)
              İbrahim Barış Mumyakmaz (1804352)
Reference	:

#------------------------------------------------------------------------------- */

package com.company;

import com.company.CheckPlagiarism;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //  PROGRAM START TIME
        long initialTime = System.currentTimeMillis();

        /*
        *    LOAD MAIN AND COMPARISON DOCUMENTS
        */
//        String mainFile = null; //args[0];
//        String compareFolder = null; //args[1];
        String mainFile = "src/com/company/main_doc.txt";       // DEFAULT
        String compareFolder = "src/com/company/Documents/";    // DEFAULT

        for (int i=0; i < args.length; i++){
            // .equals("-main")
            if (args[i].contains("-main"))
                mainFile = args[i].replace("-main=", "");
            else if (args[i].contains("-compare"))
                compareFolder = args[i].replace("-compare=", "");
            else {
                // Unrecognized argument
            }
        }
        // Check if files are valid
        if (!mainFile.contains(".txt") || !compareFolder.contains("/")){
            System.out.println("WARNING: Please provide main txt file and folder to compare");
            return;
        } else {
            System.out.println("Checking for plagiarism:\t" + mainFile);
            System.out.println("Documents folder:\t\t" + compareFolder);
        }



        /*
        *    READ CONTENT
        */
        // Main Document
        String mainContent = readFileToString(mainFile);
//        System.out.println(mainContent);
        // Find .txt files in folder
        File[] documentPaths = getTxtFiles(compareFolder);
        String[] compareContent = new String[documentPaths.length];
        for (int i=0; i< documentPaths.length; i++) {
            compareContent[i] = readFileToString(String.valueOf(documentPaths[i]));
        }



        /*
        *    START PLAGIARISM CHECK
        */
        System.out.println("Starting plagiarism check\n");
        int documentNo = 1;
        try {
            for (String content : compareContent){
                // START TIMER
//                long docIInitTime = System.currentTimeMillis();

                CheckPlagiarism check = new CheckPlagiarism();
                check.run(documentNo, mainContent, content);
                documentNo++;

                // DOCUMENT COMPARED IN
//                System.out.println("Document comparison time: " + (System.currentTimeMillis() - docIInitTime) + "ms");

                // DELETE THIS !! - CHECK ONLY FIRST DOC.
//                if (documentNo == 2) return;

            }
        }catch (Exception e) {
            System.out.println("\nERROR: Something went wrong while checking.\n" + e);
        }

        // PROGRAM STOP TIME
        long finalTime = System.currentTimeMillis();
        System.out.println("Total Execution time: " + (finalTime - initialTime) + "ms");
    }


    // FIND TXT FILES
    // https://stackoverflow.com/questions/8921987/how-to-get-all-text-files-from-one-folder-using-java
    public static File[] getTxtFiles(String folderPath){
        File txtFile = new File(folderPath);
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = txtFile.listFiles(textFilter);
        // Sort
        Arrays.sort(files);
        for (File file : files) {
            try {
//                System.out.println("Document: " + file.getCanonicalPath());
                System.out.println("Loaded: " + file.getName());
            }catch (Exception e){
                // IOException
            }
        }
        return files;
    }


    // READ FILE TO STRING
    public static String readFileToString(String filePath){
        String contents = "";
        try{
            Path pathToFile = Paths.get(filePath).toAbsolutePath();
            contents = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);
//            System.out.println("Contents: " + contents);
        }catch (IOException e){
            System.out.println("Error file read:\n" + e);
        }
        return contents;
    }


}
