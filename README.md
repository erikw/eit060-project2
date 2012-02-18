# EIT060 Project 2
Second project in the course EIT060 Computer Security at Lund University. The goal is to design and implement a system for medical records with secure connections. For further documentation; visit [doc/](https://github.com/erikw/eit060_project2/tree/master/doc) and especially *report/* and (`make` to) read the PDF.

## How to build and run
### Server
Compile and run by typing the following commands:

	$cd it060_project2
	$cd server
	$ant
	$./run.sh [-d]

-d Enables TLS handshake debugging.

### Client
Compile and run by typing the following commands:

	$cd it060_project2
	$cd client
	$ant
	$./run.sh [-d] [<user-type>]

-d Enables TLS handshake debugging.
<user-type> lets you run the program as the indicated user-type and must be from the set {"", patient, nurse, doctor, agency}. Default is patient. 

##Contributors
[Erik Jansson](https://github.com/Meldanya)

[Erik Westrup](https://github.com/erikw)

[Gustaf Waldemarson](https://github.com/xaldew)

[Tommy Olofsson](https://github.com/tommyolofsson)
