# vault-java-client

## Warning
* This is under PoC phase, don't treat code as final. Java API interface may change completely.
* Currently a test util `GetSecretJson` is written to test getting all secrets from secret-engine path. It needs following parameter in environment variable:   
  * VAULT_BASE_URL (e.g. https://localhost:8200),
  * VAULT_TOKEN, if you have vault token then pass it as environment variable. or add following additional parameter so that VAULT_TOKEN can be retrieved by LDAP auth method
    * VAULT_AUTH_PATH (e.g. auth or your own defined path)
    * VAULT_USR (username for authentication)
    * VAULT_PWD (password for authentication)
  * TOKEN_ENC_KEY . any secret text. which will be used to encrypt the value in Json map of secret store.
  * make sure your input file `secret-root.dat` is in your current working directory and populated appropriately.
  * after running `mvn clean install` run `java -jar target/vault-java-client-0.0.1-SNAPSHOT.jar`

# Feature
Implementation of https://www.vaultproject.io/api/overview
* Auth API using LDAPUser pass method.
* KV engine api to get, add & update Secret file.

## Fine prints
1. After authentication, client token must be sent as either the `X-Vault-Token` HTTP Header or as `Authorization` HTTP Header using the `Bearer <token>` scheme in subsequent call. This client usages `X-Vault-Token` over `Authorization` header.

# TODO
* Namespace
* 

# Contributing
*

# Roadmap
* 
