# Rabin Karp Implemented Plagiarism Checker [![GitHub stars](https://img.shields.io/github/stars/badges/shields.svg?style=social&label=Stars)](https://github.com/buraktokman/Rabin-Karp-Implemented-Plagiarism-Checker/)

[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://github.com/buraktokman/Rabin-Karp-Implemented-Plagiarism-Checker)
[![Repo](https://img.shields.io/badge/source-GitHub-303030.svg?maxAge=3600&style=flat-square)](https://github.com/buraktokman/Rabin-Karp-Implemented-Plagiarism-Checker)
[![Requires.io](https://img.shields.io/requires/github/celery/celery.svg)](https://requires.io/github/buraktokman/Rabin-Karp-Implemented-Plagiarism-Checker/requirements/?branch=master)
[![Scrutinizer](https://img.shields.io/scrutinizer/g/filp/whoops.svg)](https://github.com/buraktokman/Rabin-Karp-Implemented-Plagiarism-Checker)
[![DUB](https://img.shields.io/dub/l/vibe-d.svg)](https://choosealicense.com/licenses/mit/)
[![Donate with Bitcoin](https://img.shields.io/badge/Donate-BTC-orange.svg)](https://blockchain.info/address/17dXgYr48j31myKiAhnM5cQx78XBNyeBWM)
[![Donate with Ethereum](https://img.shields.io/badge/Donate-ETH-blue.svg)](https://etherscan.io/address/91dd20538de3b48493dfda212217036257ae5150)

Plagiarism detection system that checks the similarity rate between selected document and a set of documents provided in text form.



### DESIRED OUTPUTS
------

Developed program will print two distinct outputs for each document comparison process.
- Similarity rate between the main and each compared document.
- The most similar 5 sentences in each document.



### DESIRED OUTPUTS
------

Primary objectives of the project ordered by their importance.
1. Running speed (efficiency) of the algorithm.
2. Similarity detection ability.
3. Readability of the code.



### HOW TO USE THE PROGRAM
------

We generated a .jar file since executing the Main class from terminal was bit problematic
because of dependency to other classes.
Program can find and select main document and comparison folder by default if they are in the same directory. Move .jar file to a folder/directory where there are a “main_doc.txt” which will be checked and “Documents” folder containing text documents for plagiarism check.

    `Java -jar termproject.jar`

Executing the jar file also can be done by giving main document and documents folder as
arguments.

    `Java -jar termproject.jar -main=”main_doc.txt” -compare=”Documents/”`



### TOP LEVEL DESIGN OF THE PROGRAM
------

Each phase of the program without getting much into the detail.
1. Receive main file and documents folder as argument.
2. Read all files to Strings.
3. Take content of main file and check similarity with each document.
4. Print document similarity rate and most similar five sentences.



### ANALYSIS OF THE STRING SEARCHING ALGORITHM
------
Our first design was a naïve method (brute force) solution with O(m*n) complexity. However, with the implementation of Rabin-Karp string searching algorithm, at the end we managed to achieve O(n+m) complexity with **501ms average execution time.** Average execution time calculated for single main document and three comparison documents.


### LICENSE
------

MIT License
