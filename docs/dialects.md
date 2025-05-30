<!--
Copyright (C) 2025 Dremio Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
# Dremio AuthManager for Apache Iceberg - Supported Dialects

## Overview

Two "dialects" of OAuth2 are supported:

- `standard`: this dialect is based on the OAuth2 standard, as defined
  in [RFC 6749](https://tools.ietf.org/html/rfc6749) and other RFCs.
- `iceberg_rest`: this dialect reproduces the behavior of Iceberg REST's built-in OAuth2 
  `AuthManager` and exhibits some non-standard behavior.

The dialect to use can be selected with the `rest.auth.oauth2.dialect` property. 
By default, the dialect is set to `iceberg_rest` if either:

* The `rest.auth.oauth2.token` property is set, or
* The `rest.auth.oauth2.token-endpoint` property is set to a relative URL.

In all other cases, the dialect is set to `standard`.

## Differences between "Standard" and "Iceberg" Dialects

### Supported Grant Types

The Iceberg dialect can only handle the `client_credentials` grant type for an initial token fetch.

The standard dialect provides support the following additional grant types:

- Resource Owner Password (`password`) grant type
- Authorization Code (`authorization_code`) grant type
- Device Code (`urn:ietf:params:oauth:grant-type:device_code`) grant type
- Token Exchange (`urn:ietf:params:oauth:grant-type:token-exchange`) grant type

### Token Refreshes

The Iceberg dialect has a non-standard way of handling token refreshes, making it difficult to use
with external OAuth2 providers that respect the OAuth2 standard.

The standard dialect instead follows the OAuth2 standard for token refreshes:

* If a refresh token is provided in the initial token fetch, the token is refreshed using the
  `refresh_token` grant type. This is generally the case for all grant types other than
  `client_credentials`.
* Otherwise, a brand-new token is fetched using the configured initial grant type. This is generally
  the case for `client_credentials`.

### Support for Delegation and Impersonation

The Iceberg dialect does not support delegation or impersonation. 

The standard dialect supports both, allowing a user to act on behalf of another user by
configuring a token exchange grant appropriately. See [Impersonation & Delegation](./impersonation.md)
for more details.

### Support for Context- and Table-level Authentication

Iceberg REST's built-in OAuth2 `AuthManager` supports context- and table-level authentication.

This `AuthManager` also supports contextual and table sessions.

For table-level sessions using the Iceberg dialect, only access tokens vended via the `token` 
property are supported. This `AuthManager` does not support token exchanges on a server-vended typed 
token; a warning will be logged if the server sends such a token.

Moreover, this `AuthManager` encourages REST server implementors to never vend OAuth2 tokens to
clients, as this is a security risk and a violation of the OAuth2 standard. Instead, implementors
should vend OAuth2 _scopes_ to clients, which the clients can then use to fetch tokens from their
OAuth2 provider.

### Support for Externally-provided Tokens

Iceberg REST's built-in OAuth2 `AuthManager` supports externally-provided tokens, mostly via the
`token` property. Such tokens are used as-is, without any validation, and cannot be refreshed when
the OAuth2 provider is an external system.

This `AuthManager` also support externally-provided tokens, but does not encourage their use.
