CA_CERT="../../root_ca/root.crt"
CA_KEY="../../root_ca/root.key"
CA_SEQ="../../root_ca/.seq"
KEYSTORE_FILE="keystore"
TRUSTSTORE_FILE="truststore"
KEYSTORE_PASS=
NAME=
PASSWD=
TRUST_PASSWD=

#stty -echo
read -p "Passwort für die keystore, bitte: " -r PASSWD
#stty echo
echo
read -p "Passwort für die truststore, bitte: " -r TRUST_PASSWD
echo
read -p "Name: " -r NAME
echo

CRT_PATH="${NAME}/${NAME}.crt"
CSR_PATH="${NAME}/${NAME}.csr"


# if [ -f "${NAME}/${NAME}.crt" ]; then
#     keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -importcert -alias $NAME -keypass $PASSWD -file "${NAME}/${NAME}.crt"
# else
#     keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -genkeypair -alias $NAME -keysize 1024 -keypass $PASSWD
#     keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -certreq -alias $NAME -file "${NAME}/${NAME}.csr" -keypass $PASSWD
# fi

#generate key
keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -genkeypair -alias $NAME -keysize 1024 -keypass $PASSWD
# generate csr
keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -certreq -alias $NAME -file $CSR_PATH -keypass $PASSWD
# sign with CA
openssl x509 -req -in $CSR_PATH -CA $CA_CERT -CAkey $CA_KEY -out $CRT_PATH -days 365  -CAcreateserial -CAserial $CA_SEQ
# import CA cert to keystore
keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -importcert -alias root -file $CA_CERT
# import signed cert
keytool -keystore $NAME/$KEYSTORE_FILE -storepass $PASSWD -importcert -alias $NAME -keypass $PASSWD -file $CRT_PATH

# import CA cert to truststore
keytool -keystore $NAME/$TRUSTSTORE_FILE -storepass $TRUST_PASSWD -importcert -alias root -file $CA_CERT
