# vault-java-client

## Warning
* This is under PoC phase, don't treat code as final. Java API interface may change completely.
* Currently a test util `ConfigUtils` is written to test the e-2-e working. It needs at least 4 VM parameter:   
  * VAULT_BASE_URL (e.g. https://localhost:8200),
  * VAULT_AUTH_PATH (e.g. auth or your own defined path),
  * VAULT_USR (username for authentication)
  * VAULT_PWD (password for authentication)
  * SECRETS_ROOT (root path of secret engine with trailing `/`, e.g. `hello-world-dev/`) 

# Feature
Implementation of https://www.vaultproject.io/api/overview
* Auth API using LDAPUser pass method.
* KV engine api to get, add & update Secret file.

## Fine prints
1. After authentication, client token must be sent as either the `X-Vault-Token` HTTP Header or as `Authorization` HTTP Header using the `Bearer <token>` scheme in subsequent call. This client usages `X-Vault-Token` over `Authorization` header.

# TODO
* Namespace
* 