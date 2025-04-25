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
# Publishing Guide

This document describes how to publish Auth Manager artifacts to Maven repositories.

## Maven Repositories

AuthManager artifacts are published to the following Maven repositories:

- **Snapshots & Releases**: `https://maven.dremio.com/free/`

## Publishing Locally

To publish artifacts to your local Maven repository (`~/.m2/repository`), run:

```bash
./gradlew publishToMavenLocal
```

## Publishing to Dremio Maven Repository

There are two ways to publish to the Dremio Maven repository:

### 1. Automated Nightly Snapshots

Snapshots are automatically published nightly from the `main` branch. This is handled by a GitHub 
Action workflow that runs on a cron schedule. You can also manually trigger this workflow from the 
GitHub Actions tab.

### 2. Manual Release

To create a release:

1. Go to the GitHub Actions tab
2. Select the "Publish Release" workflow
3. Click "Run workflow"
4. Enter the release version (e.g., "0.1.0") and the next development version (e.g., "0.1.1-SNAPSHOT")
5. Click "Run workflow"

This will:
1. Update the version.txt file to the release version
2. Build and test the project
3. Sign and publish artifacts to the Dremio Maven repository
4. Create a Git tag for the release
5. Update the version.txt file to the next development version
6. Push all changes to GitHub

## Required Credentials

The following secrets must be configured in the GitHub repository settings for publishing to work:

- `MAVEN_USERNAME`: Username for the Dremio Maven repository
- `MAVEN_PASSWORD`: Password for the Dremio Maven repository
- `GPG_PRIVATE_KEY`: GPG private key for signing release artifacts
- `GPG_PASSPHRASE`: Passphrase for the GPG private key

> Note: For information on consuming published artifacts, see the [Installation](./installation.md#maven-artifacts) guide.