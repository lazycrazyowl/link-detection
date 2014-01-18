#!/bin/bash

./eval/compare-sets-inTermsOf-PrecisionRecallFScore.pl \
    -r eval/in-reply-to-list.digest \
    -c output/result.txt \
    2>&1 \
    | grep -v 'Info' \
    | sed -e 's/^ *//' -e 's/ *& */\t/g' \
    | column -t -s $'\t'
