#!/bin/sh
MainClass=com.tianze.Main
cp=../config/:$(echo ../libs/*.jar|sed 's/ /:/g')
java  -server -classpath ${cp} ${MainClass} >> logs/out.log 2>&1 &

