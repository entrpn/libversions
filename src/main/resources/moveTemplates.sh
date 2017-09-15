#!/bin/bash
if [ `uname -s` = "Darwin" ]; then
    dirList=(`ls -d ~/Library/Preferences/AndroidStudio*`)
    echo ${#dirList[@]}
    for dirName in "${dirList[@]}"
    do
        templates="${dirName}/templates/"
        mkdir -p $templates
        cp gradle.xml $templates
    done
else
    echo "This script only supports Mac OS."
fi