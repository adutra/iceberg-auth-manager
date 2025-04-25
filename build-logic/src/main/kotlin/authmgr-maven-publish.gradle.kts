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

import java.net.URI

plugins {
  `maven-publish`
  signing
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
      
      pom {
        name.set(project.name)
        description.set("Auth Manager for Apache Iceberg")
        url.set("https://github.com/dremio/iceberg-auth-manager")
        
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        
        developers {
          developer {
            id.set("dremio")
            name.set("Dremio Corporation")
            email.set("oss@dremio.com")
            organization.set("Dremio Corporation")
            organizationUrl.set("https://www.dremio.com")
          }
        }
        
        scm {
          connection.set("scm:git:git://github.com/dremio/iceberg-auth-manager.git")
          developerConnection.set("scm:git:ssh://github.com:dremio/iceberg-auth-manager.git")
          url.set("https://github.com/dremio/iceberg-auth-manager")
        }
      }
      
      // Suppress test fixtures capability warnings
      suppressPomMetadataWarningsFor("testFixturesApiElements")
      suppressPomMetadataWarningsFor("testFixturesRuntimeElements")
    }
  }
  
  repositories {
    maven {
      name = "DremioFree"
      val releasesRepoUrl = URI("https://maven.dremio.com/free/")
      val snapshotsRepoUrl = URI("https://maven.dremio.com/free/")
      url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
      
      credentials {
        username = System.getenv("MAVEN_USERNAME")
        password = System.getenv("MAVEN_PASSWORD")
      }
    }
  }
}

// Only attempt to sign if GPG key is available (usually in CI)
signing {
  setRequired {
    !version.toString().endsWith("SNAPSHOT") && gradle.taskGraph.hasTask("publish")
  }
  
  val signingKey: String? = System.getenv("GPG_PRIVATE_KEY")
  val signingPassword: String? = System.getenv("GPG_PASSPHRASE")
  
  if (signingKey != null && signingPassword != null) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["maven"])
  }
}