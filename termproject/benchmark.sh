#!/bin/bash
# START=$(date +%s)
# for i in {1..4}
# do
# java -jar termproject_jar.jar
# done
# END=$(date +%s)
# DIFF=$(echo "$END - $START" | bc)
# echo "Average execution time: $DIFF seconds"

time for i in {1..20}; do java -jar termproject.jar; done
# cat perl | grep real | awk '{print $2}'