#!/bin/bash

export HALE_OPTS="-Duser.country=DE -Duser.language=de"

for file in *.json
do
  filename=$(basename "$file")
  filename="${filename%.*}"
  echo "Applying filter $filename..."
  hale project alignment filter --name $filename --json-filter $file --skip-empty --skip-no-type-cells
done
