#!bin/bash
cat $1 | sed -e 's/, /\. /g' | sed -e 's/{/\ /g'  | sed -e 's/}/\ /g' > $2
