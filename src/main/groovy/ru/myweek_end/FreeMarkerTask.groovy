/*
 * Copyright © 2016. Все права защищены.
 * company: Моя неделя завершилась <https://myweek-end.ru/>
 * author: Алексей Кляузер <alexey.abak@yandex.ru>
 *
 * This file is part of "FreeMarker для Gradle".
 *
 * "FreeMarker для Gradle" is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "FreeMarker для Gradle" is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with "FreeMarker для Gradle".  If not, see <http://www.gnu.org/licenses/>.
 *
 * Этот файл — часть "FreeMarker для Gradle".
 *
 * "FreeMarker для Gradle" - свободная программа: вы можете перераспространять ее и/или
 * изменять ее на условиях Афферо Стандартной общественной лицензии GNU в
 * том виде, в каком она была опубликована Фондом свободного программного
 * обеспечения; либо версии 3 лицензии, либо (по вашему выбору) любой более
 * поздней версии.
 *
 * "FreeMarker для Gradle" распространяется в надежде, что она будет полезной, но БЕЗО
 * ВСЯКИХ ГАРАНТИЙ; даже без неявной гарантии ТОВАРНОГО ВИДА или ПРИГОДНОСТИ
 * ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Подробнее см. в Афферо Стандартной общественной
 * лицензии GNU.
 *
 * Вы должны были получить копию Афферо Стандартной общественной лицензии GNU
 * вместе с этой программой. Если это не так, см.
 * <http://www.gnu.org/licenses/>.
 */
package ru.myweek_end

import java.util.Map
import java.util.HashMap

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task

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

import ru.myweek_end.FreeMarkerExtension 

class FreeMarkerTask extends DefaultTask {

  FreeMarkerExtension extension = FreeMarkerExtension.getExtension( getProject() )

  @InputDirectory
  File templateDir = extension.binDir

  @InputFiles
  FileCollection templates

  @OutputFiles
  FileCollection results

  @Input
  Map<String, ?> model = new HashMap<String, ?>()

  @TaskAction
  def make() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_23)
    cfg.setDirectoryForTemplateLoading(templateDir)
    cfg.setDefaultEncoding("UTF-8")

    Iterator<File> templateIterator = templates.iterator()
    Iterator<File> resultIterator = results.iterator()
    while ((templateIterator.hasNext()) && (resultIterator.hasNext())) {
      File template = templateIterator.next()
      File result = resultIterator.next()
      String name = project.projectDir.toPath().relativize( template.toPath() ).toString()
      Template temp = cfg.getTemplate(name)
      Writer out = new FileWriter(result)
      temp.process(model, out)
    }
  }

  static Task getTask(Project project, FreeMarkerExtension extension, Task copyTask) {
    def taskName = 'freemarker'
    def task = project.tasks.findByPath(taskName)
    if (task == null) {
      Map<String, ?> args = new HashMap<String, ?>()
      args.put('type', FreeMarkerTask)
      args.put('group', 'Build')
      args.put('description', 'Apply template files')
      task = project.task( args,  taskName )
      task.dependsOn(copyTask)
    }
    return task
  }

}
