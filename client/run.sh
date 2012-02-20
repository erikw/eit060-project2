#!/bin/sh
if [ "$1" = "-d" ]; then
	debugopt="-Djavax.net.debug=ssl:handshake:data"
	user=$2
else 
	user=$1
fi
java -cp bin $debugopt Client $user
