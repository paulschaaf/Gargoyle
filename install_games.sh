#!/bin/bash
dest='"/sdcard/Interactive Fiction"'
InfocomGames=int-fic/Infocom/{BeyondZork.dat,Deadline.dat,Enchanter.dat,LeatherGoddessesOfPhobos.dat,HitchhikersGuide.z3}
PublicGames=int-fic/public/Adventure.z5
ScottAdamsGames=int-fic/ScottAdams/MysteryFunhouse.z5

#adb install ~/Documents/Dropbox/ES\ File\ Explorer*.apk

adb shell mkdir "/sdcard/Interactive Fiction"
all_games=$(eval echo ${ScottAdamsGames} ${InfocomGames} ${PublicGames})
echo Pushing games ...
for x in ${all_games}; do
	if [ -f ${x} ]; then
		echo ${x}
		echo -n "    "
		echo "    adb push '${x}' '/sdcard/Interactive Fiction'"
		adb push -p ${x} "/sdcard/Interactive Fiction"
	fi
#	echo "    adb shell rm -f /sdcard/Interactive\\ Fiction/${x}"
#	adb shell rm -f /sdcard/Interactive\ Fiction/${x}
done

