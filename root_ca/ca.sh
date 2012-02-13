CA_KEY="root.key"  # SPECIFY THE PATH TO YOUR CERTIFICATE AUTHORITY PRIVATE KEY HERE
CA_CERT="root.crt" # SPECIFY THE PATH TO YOUR CERTIFICATE AUTHORITY CERTIFICATE HERE

DEFAULT_KEY_SIZE=4096
DEFAULT_KEY_NAME="ca"

noargs() {
	echo -e "\033[31mNo or wrong arguments given. \033[0m"
	echo -e "\033[31mUsage:"
	echo -e "$0 ca -n name [-p password]"
	echo -e "    password  - the password used to encrypt the private key"
	echo -e "    name      - the name of the files to create, eg. name.(key|crt)\033[0m"
	exit
}


if [ $# == 0 ]; then
	noargs
fi

for arg; do
	case $arg in
		new)
			shift $((1))
			NAME=$DEFAULT_KEY_NAME

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

			if [ $PASSW ]; then
				PASSW_CMD1="-des3 -passout pass:${PASSW}"
				PASSW_CMD2="-passin pass:${PASSW}"
			fi

			echo -en "\033[33mGenerating CA private key... \033[0m"
			openssl genrsa -out "${NAME}.key" $PASSW_CMD1 $DEFAULT_KEY_SIZE 2> /dev/null
			echo -e "\033[32mDone!\033[0m"
			echo -e "\033[33mAsking for information to put in certificate... \033[0m"
			openssl req -new -key "${NAME}.key" $PASSW_CMD2 -x509 -days 3650 -out "${NAME}.crt"
			echo -e "\033[32mDone creating a new CA! Private key saved in ${NAME}.key, certificate saved in ${NAME}.crt.\033[0m"

			;;
		sign)
			if [ -z $CA_KEY ] || [ -z $CA_CERT ]; then
				echo "Please specify the \$CA_KEY and \$CA_CERT variables in this script before signing."
				exit
			fi
			shift $((1))

			while getopts ":p:f:" OPTION; do
				case $OPTION in
					p)
						PASSW="$OPTARG"
						;;
					f)
						FILE="$OPTARG"
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

			echo -e "\033[33mAbout to sign certificate ${FILE}.csr.\033[0m"
			openssl x509 -req -in "${FILE}.csr" -CA $CA_CERT -CAkey $CA_KEY -out "${FILE}.crt" -days 365  -CAcreateserial -CAserial .seq
			echo -e "\033[32mDone! Signed certificate saved in ${FILE}.crt.\033[0m"
			;;
	esac
done
