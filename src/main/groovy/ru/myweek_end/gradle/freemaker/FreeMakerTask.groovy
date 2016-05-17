package ru.myweek_end.gradle.freemaker

import org.gradle.api.DefaultTask

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFiles 

import org.gradle.api.file.FileCollection

import freemarker.template.Configuration
import freemarker.template.Template

import java.util.Set
import java.util.Iterator 

class FreeMakerTask extends DefaultTask {

    @InputDirectory
    File templateDir = new File(getProject().projectDir, 'src/main/templates')

    @InputFiles
    FileCollection templates

    @OutputFiles
    FileCollection results

    @Input
    Object model

    @TaskAction
    def make() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setDefaultEncoding("UTF-8");

        Iterator<File> templateIterator = iterator();
        Iterator<File> resultIterator = iterator();
        while ((templateIterator.hasNext()) && (resultIterator.hasNext())) {
            File template = templateIterator.next()
            File result = resultIterator.next()
            Template temp = cfg.getTemplate(template);
            Writer out = new FileWriter(result);
            temp.process(model, out);
        }
    }

}