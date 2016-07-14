#!/bin/bash

export HALE_OPTS="-Duser.country=DE -Duser.language=de"

hale project alignment export-doc
hale project alignment export-table
hale project alignment export-json

