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

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task

import org.gradle.api.tasks.Copy

import ru.myweek_end.FreeMarkerExtension

import ru.myweek_end.FreeMarkerTask

class FreeMarkerPlugin implements Plugin<Project> {

  protected Project project
  protected FreeMarkerExtension extension
  protected Task copyTask
  protected Task freemarkerTask

  void apply(Project project) {
    this.project = project
    this.project.buildscript{
      repositories {
        mavenCentral()
      }
      dependencies {
        //classpath group: 'org.freemarker', name: 'freemarker', version: '2.3.23'
      }
    }
    this.extension = FreeMarkerExtension.getExtension(this.project)
    this.project.getPluginManager().apply('base')
    this.copyTask = FreeMarkerExtension.getCopyTask(this.project, this.extension)
    this.freemarkerTask = FreeMarkerTask.getTask(this.project, this.extension, this.copyTask)
  }

}