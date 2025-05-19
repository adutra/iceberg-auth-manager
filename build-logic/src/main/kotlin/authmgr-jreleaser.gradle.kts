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

import org.jreleaser.model.Active
import org.jreleaser.model.api.common.Apply

plugins {
  `maven-publish`
  id("org.jreleaser")
}

publishing {
  publications {
    create<MavenPublication>("staging-maven") {

      // FIXME remove
      groupId = "dev.alexdutra"

      from(components["java"])

      pom {
        name = "Auth Manager for Apache Iceberg"
        description = "Dremio AuthManager for Apache Iceberg is an OAuth2 manager for Apache Iceberg REST. It is a general-purpose implementation that is compatible with any Apache Iceberg REST catalog."
        url.set("https://github.com/dremio/iceberg-auth-manager")
        inceptionYear = "2025"

        licenses {
          license {
            name = "The Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }

        developers {
          developer {
            id = "dremio"
            name = "Dremio"
            email = "oss@dremio.com"
            organization = "Dremio Corporation"
            organizationUrl = "https://www.dremio.com"
          }
        }

        scm {
          connection = "scm:git:git://github.com/dremio/iceberg-auth-manager.git"
          developerConnection = "scm:git:ssh://github.com:dremio/iceberg-auth-manager.git"
          url = "https://github.com/dremio/iceberg-auth-manager"
        }
      }

      // Suppress test fixtures capability warnings
      suppressPomMetadataWarningsFor("testFixturesApiElements")
      suppressPomMetadataWarningsFor("testFixturesRuntimeElements")
    }
  }
  repositories {
    maven {
      name = "localStaging"
      url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
    }
  }
}

jreleaser {

  gitRootSearch.set(true)

  dryrun.set(true)

  project {
    description.set("Dremio AuthManager for Apache Iceberg")
    authors.set(listOf("Dremio Corporation"))
    license.set("Apache-2.0")
    links {
      homepage.set("https://github.com/dremio/iceberg-auth-manager")
    }
  }
  
  signing {
    active.set(Active.ALWAYS)
    verify.set(false) // requires the GPG public key to be set up
    armored.set(true)
  }

  release {
    github {
      repoOwner.set("dremio")
      name.set("iceberg-auth-manager")
      tagName.set("authmgr-{{projectVersion}}")
      commitAuthor {
        name.set("AuthManager Release Workflow [bot]")
        email .set("authmgr-release-workflow-noreply@dremio.co")
      }
      milestone {
        close.set(true)
        name.set( "{{tagName}}")
      }
      issues {
        enabled.set(true)
        comment.set( "🎉 This issue has been resolved in `{{tagName}}` ([Release Notes]({{releaseNotesUrl}}))")
        applyMilestone.set(Apply.ALWAYS)
      }
      changelog {
        links.set(false)
        skipMergeCommits.set(true)
        formatted.set(Active.ALWAYS)
        preset.set( "gitmoji")
        extraProperties.put("categorizeScopes", true)
        hide {
          contributors.set(listOf("[bot]", "renovate-bot"))
          categories.set(listOf("chore"))
        }
      }
    }
  }

  deploy {
    maven {
      mavenCentral {
        create("sonatype")  {
          active.set(Active. ALWAYS)
          url.set("https://central.sonatype.com/api/v1/publisher")
          applyMavenCentralRules.set(true)
          stagingRepositories.set(listOf(layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath))
        }
      }
    }
  }
}
