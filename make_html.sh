#!/bin/bash

source "$PWD"/venv/bin/activate

for dir in question_*
do
    grip "$dir/$dir.md" --export "./$dir/$dir.html" --title=" "
done
