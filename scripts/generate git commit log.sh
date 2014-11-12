#!/bin/bash

DATE=$(date.exe +"%Y-%m-%d")
LOGFILE="../logs/git-$DATE.log"
eval "git log --pretty=format:"%s" --since=1.weeks --graph --stat >> $LOGFILE"