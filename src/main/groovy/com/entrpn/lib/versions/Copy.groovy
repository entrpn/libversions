package com.entrpn.lib.versions

import org.gradle.api.DefaultTask
import org.gradle.api.Nullable
import org.gradle.api.tasks.TaskAction

class Copy extends DefaultTask {
    private static final String DEPENDENCIES_GRADLE_FILE_NAME = "dependencies.gradle"
    private static final String LIVE_TEMPLATE_FILE_NAME = "gradle.xml"
    private static final String SCRIPT_FILE_NAME = "moveTemplates.sh"
    private static final String BUILD_GRADLE = "build.gradle"
    private final ClassLoader loader = getClass().getClassLoader()

    @TaskAction
    def action() {
        def outputFile = new File(project.rootDir.path+File.separator+DEPENDENCIES_GRADLE_FILE_NAME)
        def exists = outputFile.exists()
        if (exists) {
            outputFile.delete()
        }
        if (outputFile.createNewFile()) {
            outputFile << loader.getResource(DEPENDENCIES_GRADLE_FILE_NAME).text
            copyTemplates()
            if (!exists) {
                def buildGradleFile = new File(project.rootDir.path + File.separator + BUILD_GRADLE)
                buildGradleFile.append(System.lineSeparator() + "apply from: '" + DEPENDENCIES_GRADLE_FILE_NAME + "'")
            }
            println "(\"(•_•) ( •_•)>⌐■-■ (⌐■_■)\")"
        } else {
            println DEPENDENCIES_GRADLE_FILE_NAME+" could not be created."
        }
    }
    private copyTemplates() {

        def liveTemplateFile = copyFile(LIVE_TEMPLATE_FILE_NAME)
        if (liveTemplateFile.exists()) {
            liveTemplateFile << loader.getResource(LIVE_TEMPLATE_FILE_NAME).text
            def scriptFile = copyFile(SCRIPT_FILE_NAME)
            if (scriptFile.exists()) {
                scriptFile << loader.getResource(SCRIPT_FILE_NAME).text
                def sout = new StringBuffer()
                def serr = new StringBuffer()
                scriptFile.setExecutable(true)
                String tempFile = "./"+SCRIPT_FILE_NAME
                Process proc = tempFile.execute()
                proc.consumeProcessOutput(sout, serr)
                proc.waitForOrKill(1000)
                println "Live templated was added to this number of android studio versions: "+sout
                deleteFiles([liveTemplateFile,scriptFile] as File[])
            } else {
                println "Error, could not create "+SCRIPT_FILE_NAME+". Stopping."
                deleteFiles([liveTemplateFile] as File[])
            }
        } else {
            println "Error, could not create "+LIVE_TEMPLATE_FILE_NAME+". Stopping."
        }
    }
    private File copyFile(String fileName) {
        def file = new File(project.rootDir.path + File.separator + fileName)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }
    private void deleteFiles(File[] files) {
        for (File file: files) {
            file.delete()
        }
    }
}
