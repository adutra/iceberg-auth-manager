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
  id("org.jreleaser")
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

  hooks {
    condition.set("'{{ Env.CI }}' == true")
    script {
      before {
        filter {
          includes.set(listOf("session"))
        }
        run.set("""
        echo "### {{command}}" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "| Step | Outcome |" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "| ---- | ------- |" >> ${'$'}GITHUB_STEP_SUMMARY
        """.trimIndent())
      }
      success {
        filter {
          excludes.set(listOf("session"))
        }
        run.set("""
        echo "| {{event.name}} | :white_check_mark: |" >> ${'$'}GITHUB_STEP_SUMMARY
        """.trimIndent())
      }
      success {
        filter {
          includes.set(listOf("session"))
        }
        run.set("""
        echo "" >> ${'$'}GITHUB_STEP_SUMMARY
        """.trimIndent())
      }
      failure {
        filter {
          excludes.set(listOf("session"))
        }
        run.set("""
        echo "| {{event.name}} | :x: |" >> ${'$'}GITHUB_STEP_SUMMARY
        """.trimIndent())
      }
      failure {
        filter {
          includes.set(listOf("session"))
        }
        run.set("""
        echo "" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "### Failure" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "\`\`\`" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "{{event.stacktrace}}\`\`\`" >> ${'$'}GITHUB_STEP_SUMMARY
        echo "" >> ${'$'}GITHUB_STEP_SUMMARY
        """.trimIndent())
      }
    }
  }

  release {
    github {
      repoOwner.set("dremio")
      name.set("iceberg-auth-manager")
      branch.set("main")
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
        links.set(true)
        skipMergeCommits.set(true)
        formatted.set(Active.ALWAYS)
        content.set("""
        # Dremio Iceberg AuthManager {{projectVersionNumber}}
        {{changelogChanges}}
        {{changelogContributors}}
        """.trimIndent())
        preset.set("conventional-commits")
        format.set("- {{commitShortHash}} {{commitTitle}}")
        extraProperties.put("categorizeScopes", true)
        category  {
          key.set( "features")
          labels.set(listOf("feat"))
          title.set("🚀 New Features")
          order.set(10)
          format.set("- {{commitShortHash}} {{commitTitle}}{{#conventionalCommitBody}}<br>{{.}}{{/conventionalCommitBody}}")
        }
        contributors {
          format.set("- {{contributorName}}{{#contributorUsernameAsLink}} ({{.}}){{/contributorUsernameAsLink}}")
        }
        hide {
          categories.set(listOf("chore", "build", "tasks", "docs"))
          contributors.set(listOf("[bot]", "renovate-bot", "GitHub"))
        }
      }
    }
  }

  deploy {
    maven {
      mavenCentral {
        create("sonatype")  {
          active.set(Active.ALWAYS)
          url.set("https://central.sonatype.com/api/v1/publisher")
          applyMavenCentralRules.set(true)
          subprojects.forEach { project ->
            stagingRepository(project.layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)
          }
        }
      }
    }
  }
}
