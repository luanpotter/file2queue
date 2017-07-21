#!/bin/bash

URL=$1
queue=$2
td=$3

wget -O - -o /dev/null "$URL/admin/queues.jsp" | grep -v DOCTYPE | sed 's/\&/&amp;/g' | awk '{print tolower($0)}' | xpath -q -e '//table[@id="queues"]/*/tr[td/a[normalize-space(text())="'$queue'"]]/td['$td']/text()'
