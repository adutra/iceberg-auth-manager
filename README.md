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
# Dremio AuthManager for Apache Iceberg

This project contains an implementation of Apache Iceberg's `AuthManager` API for OAuth2.

## Overview

Dremio AuthManager for Apache Iceberg is an OAuth2 manager for Apache Iceberg REST. It is a
general-purpose implementation that is compatible with any Apache Iceberg REST catalog. It aims at
providing a more flexible and extensible OAuth2 manager than the one bundled with Iceberg REST,
while strictly adhering to the OAuth2 standards.

This `OAuthManager` can serve as a drop-in replacement for the built-in `OAuth2Manager` bundled
with Iceberg REST, allowing for easier migration from the latter to the former.

## Getting Started

To get started with Dremio AuthManager for Apache Iceberg, you can follow the instructions in the
[documentation](./docs).

## Maven Artifacts

Maven artifacts are published to the Dremio Free Maven repository. To use them in your project:

```xml
<!-- Add Dremio repository -->
<repositories>
  <repository>
    <id>dremio-free</id>
    <url>https://maven.dremio.com/free/</url>
  </repository>
</repositories>

<!-- Add the OAuth2 dependency -->
<dependency>
  <groupId>com.dremio.iceberg.authmgr</groupId>
  <artifactId>authmgr-oauth2</artifactId>
  <version>0.0.2</version>
</dependency>
```

For Gradle:

```kotlin
repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.dremio.com/free/")
  }
}

dependencies {
  implementation("com.dremio.iceberg.authmgr:authmgr-oauth2:0.0.2")
}
```

## License

Copyright (C) 2025 Dremio Corporation

This project is under the Apache License Version 2.0. See the [LICENSE](LICENSE).
