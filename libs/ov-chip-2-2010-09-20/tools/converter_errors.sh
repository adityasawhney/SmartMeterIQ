#!/bin/sh

if [ -x `dirname $0`/converter_errors ] ; then
    `dirname $0`/converter_errors $*
else
    cat -
fi
