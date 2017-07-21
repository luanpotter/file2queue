#!/bin/bash

URL=$1
queue=$2
td=$3

file=$4
dt=$5

while true; do
  echo -e "`date`\t\t\t`./fetch.sh $URL $queue $td`" >> $file
  sleep $dt
done
