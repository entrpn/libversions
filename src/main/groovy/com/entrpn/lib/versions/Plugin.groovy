package com.entrpn.lib.versions

import org.gradle.api.Project

class Plugin implements org.gradle.api.Plugin<Project> {

    @Override
    void apply(Project project) {
        def libversions = project.task("copyDependencies",type: Copy)
        libversions.group = "entrpn-tools"
        libversions.description = "Manages dependency versions"
    }
}
