/*
 * Copyright (C) 2025 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  id("org.jreleaser")
}

jreleaser {

  project {
    description.set("Dremio AuthManager for Apache Iceberg")
    authors.set(listOf("Dremio Corporation"))
    license.set("Apache-2.0")
    links {
      homepage.set("https://github.com/dremio/iceberg-auth-manager")
    }
  }
  
  signing {
    active.set(org.jreleaser.model.Active.ALWAYS)
    armored.set(true)
    passphrase.set("{{DEVBOT_GPG_PASSPHRASE}}")
    secretKey.set("{{DEVBOT_GPG_PRIVATE_KEY}}")
  }

  deploy {
    maven {
      mavenCentral {
        create("sonatype")  {
          active.set(org.jreleaser.model.Active.ALWAYS)
          url.set("https://central.sonatype.com/api/v1/publisher")
          applyMavenCentralRules.set(true)
          username.set("{{DEVBOT_CENTRAL_USERNAME}}")
          password.set("{{DEVBOT_CENTRAL_PASSWORD}}")
          stagingRepositories.set(listOf("build/staging-deploy"))
        }
      }
    }
  }
}
