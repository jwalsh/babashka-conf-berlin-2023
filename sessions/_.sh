#!/bin/bash

# create an array of session topics
topics=("Doors Open" "Welcome" "Clojure on SCIs" "Break" "Portuguese Driving Schools and bb" "SCI for Science" "Lunch" "Lightning Talks" "Do or do not Clojure" "Don't forget the REPL" "Break" "Birds of Feather" "Build Your Own Little Memex" "Growing An Ecosystem")

# create an array of session start times
times=("9:00" "9:30" "9:40" "10:20" "10:40" "11:20" "11:50" "13:30" "14:10" "14:50" "15:20" "15:40" "16:40" "17:20")

# create an array of session durations
durations=("0" "0" "40" "0" "30" "30" "0" "30" "30" "30" "20" "50" "30" "40")

# loop through the arrays and create the directories and README files
for i in ${!topics[@]}; do
    # format the time
    time=$(date -d "${times[$i]}" +"%H-%M")
    # create the directory name, lowercase the topic and replace non-alphanumeric characters with -
    dir=$(echo "${time}-${topics[$i]}-${durations[$i]}" | tr '[:upper:]' '[:lower:]' | tr -C '[:alnum:]' '-')
    # create the directory
    mkdir $dir
    # create the README.org file and add the header information
    echo "# ${topics[$i]}" > $dir/README.org
    echo ":PROPERTIES:" >> $dir/README.org
    echo ":date: $(date -d "${times[$i]}" +"%Y-%m-%d %H:%M")" >> $dir/README.org
    echo ":duration: ${durations[$i]} minutes" >> $dir/README.org
    echo ":END:" >> $dir/README.org
done
