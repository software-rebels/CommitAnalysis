#!/bin/bash
WORKINGDIR=$(pwd)

if [ "$1" == "--help" ]; then
	echo "First Argument gives the commit ID of the commit to revert to."
	echo "Second Argument should give the location of the git repository"
	echo "Third Argument should give the location of the folder to place the build files"
else
	GITREPO="$1"
	COMMITID="$2"
	cd $GITREPO
	git reset --hard $COMMITID
	cd $WORKINGDIR/Build
	rm -rf *
	cmake $GITREPO
	echo "Running makewrapper"
	$WORKINGDIR/Scripts/makewrapper.sh all 2&> trace.txt
	echo "Makewrapper ended"
	$WORKINGDIR/Scripts/generate_makao_graph.pl -in trace.txt -out trace.gdf -format gdf
fi

