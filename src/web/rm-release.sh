#!/bin/sh
#
# rm-release.sh
#
# Removes the files for the XINS release specified in the parameter
# This script should be used on the webserver where the release files are
# uploaded.

if [ "$1" = "" ]; then
	echo "ERROR: Please specify which release should be removed."
	exit 1
fi

echo -n "Removing release $1..."

rm -f changes-$1.txt
rm -f xins-$1.tgz
rm -rf j2h/$1
rm -rf javadoc/$1
rm -rf logdoc/$1
rm -rf checkstyle/$1

echo " [ DONE ]"
