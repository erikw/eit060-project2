KEYSTORE_FILE="truststore"
KEYSTORE_PASS="password3"
KEYSTORE_KEYPAIR_ALIAS="patient"
KEYSTORE_KEYPAIR_PASS="password3"

noargs() {
	echo -e "\033[31mNo or wrong arguments given. \033[0m"
	echo -e "\033[31mUsage:"
	echo -e "$0 [new|csr|add] -n name -p [-c]"
	echo -e "    password  - the password used to encrypt the private key"
	echo -e "    name      - the name of the files to create, eg. name.(key|crt)"
	echo -e "    -c        - only applies when using the new command, don't create a CSR when creating a new keypair.\033[0m"
	exit
}


if [ $# == 0 ]; then
	noargs
fi

for arg; do
	case $arg in
		new)
			shift $((1))
			while getopts ":p:n:c" OPTION; do
				case $OPTION in
					p)
						PASSW="$OPTARG"
						;;
					n)
						NAME="$OPTARG"
						;;
					c)
						NO_CSR="1"
						;;
					:)
						echo "Option -$OPTARG requires an argument." >&2
						exit
						;;
					\?)
						echo "Unknown option: -$OPTARG" >&2
						exit
						;;
				esac
			done
			if [ -z $PASSW ] || [ -z $NAME ]; then
				echo "I'm sorry Dave, I can't to that."
				exit
			fi

			keytool -keystore $KEYSTORE_FILE -storepass $KEYSTORE_PASS -genkeypair -alias $NAME -keysize 1024 -keypass $PASSW
			if [ -z $NO_CSR ]; then
				keytool -keystore $KEYSTORE_FILE -storepass $KEYSTORE_PASS -certreq -alias $NAME -file "${NAME}.csr" -keypass $PASSW
			fi

			;;
		csr)
			shift $((1))
			while getopts ":p:n:" OPTION; do
				case $OPTION in
					p)
						PASSW="$OPTARG"
						;;
					n)
						NAME="$OPTARG"
						;;
					:)
						echo "Option -$OPTARG requires an argument." >&2
						exit
						;;
					\?)
						echo "Unknown option: -$OPTARG" >&2
						exit
						;;
				esac
			done
			if [ -z $PASSW ] || [ -z $NAME ]; then
				echo "I'm sorry Dave, I can't to that."
				exit
			fi
				keytool -keystore $KEYSTORE_FILE -storepass $KEYSTORE_PASS -certreq -alias $NAME -file "${NAME}.csr" -keypass $PASSW
			;;
		add)
			shift $((1))
			while getopts ":p:n:" OPTION; do
				case $OPTION in
					p)
						PASSW="$OPTARG"
						;;
					n)
						NAME="$OPTARG"
						;;
					:)
						echo "Option -$OPTARG requires an argument." >&2
						exit
						;;
					\?)
						echo "Unknown option: -$OPTARG" >&2
						exit
						;;
				esac
			done
			if [ -z $PASSW ] || [ -z $NAME ]; then
				echo "I'm sorry Dave, I can't to that."
				exit
			fi
			echo $PASSW

			keytool -keystore $KEYSTORE_FILE -storepass $KEYSTORE_PASS -importcert -alias $NAME -keypass $PASSW -file "${NAME}.crt"
			;;
	esac
done
