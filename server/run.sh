#!/bin/sh
if [ "$1" == "-d" ]; then
	debugopt="-Djavax.net.debug=ssl:handshake:data"
fi
java -cp bin $debugopt JournalServer
