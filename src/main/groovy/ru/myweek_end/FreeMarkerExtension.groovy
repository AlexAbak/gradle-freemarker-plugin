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

import org.gradle.api.Project
import org.gradle.api.Task

import org.gradle.api.tasks.Copy

class FreeMarkerExtension {

  File srcDir

  File binDir

  File libsDir

  static FreeMarkerExtension getExtension(Project project) {
    def extension = project.extensions.findByType(FreeMarkerExtension)
    if (extension == null) {
      extension = project.extensions.create('freemarker', FreeMarkerExtension)
      extension.with {
        srcDir = new File(project.projectDir, 'src/main/freemarker')
        binDir = new File(project.buildDir, 'freemarker')
        libsDir = new File(project.buildDir, 'libs')
      }
      project.configurations {
	    freemarker
	  }
    }
    return extension
  }

  static Task getCopyTask(Project project, FreeMarkerExtension extension) {
    def taskName = 'freemarkerCopy'
    def task = project.tasks.findByPath(taskName)
    if (task == null) {
      Map<String, ?> args = new HashMap<String, ?>()
      args.put('type', Copy)
      args.put('group', 'Build')
      args.put('description', 'Copy template files')
      task = project.task( args,  taskName )
      task.into(extension.binDir).from(extension.srcDir)
      project.afterEvaluate {
        for ( def f : project.configurations.getByName('freemarker').getFiles()  ) {
          task.into(extension.binDir).from(project.tarTree(f))
        }
      }
    }
    return task
  }

}