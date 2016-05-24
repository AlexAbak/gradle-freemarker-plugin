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

import java.io.FileInputStream
import java.io.DataInputStream
import java.io.BufferedReader

import org.junit.Test

import org.gradle.testfixtures.ProjectBuilder

import org.gradle.api.Project
import org.gradle.api.Task

import org.gradle.api.tasks.Copy

import org.gradle.api.tasks.bundling.Tar

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.BuildLauncher

import static org.junit.Assert.*

import ru.myweek_end.FreeMarkerTask
import ru.myweek_end.FreeMarkerPlugin

class FreeMarkerPluginTest {

  @Test
  public void canFreeMarkerLibLive() {
    File test1Dir = new File( 'src/test/projects/test1' ).getAbsoluteFile()
    File test2Dir = new File( 'src/test/projects/test2' ).getAbsoluteFile()
    File test3Dir = new File( 'src/test/projects/test3' ).getAbsoluteFile()
    
    GradleConnector gradleConnection = GradleConnector.newConnector()
    
    GradleConnector gradleConnection1 = gradleConnection.forProjectDirectory(test1Dir)
    ProjectConnection projectConnection1 = gradleConnection1.connect()
    try {
      BuildLauncher buildLauncher = projectConnection1.newBuild()
      buildLauncher.forTasks( 'clean', 'freemarkerLib' )
      buildLauncher.run() 
    } finally {
      projectConnection1.close()
    }
    assertTrue(new File( 'src/test/projects/test1/build/freemarker/test1.ftlh' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test1/build/libs/test1-0.0.0.1-freemarker.tar.gz' ).getAbsoluteFile().exists() )
    
    GradleConnector gradleConnection2 = gradleConnection.forProjectDirectory(test2Dir)
    ProjectConnection projectConnection2 = gradleConnection2.connect()
    try {
      BuildLauncher buildLauncher = projectConnection2.newBuild()
      buildLauncher.forTasks( 'clean', 'freemarkerLib' )
      buildLauncher.run() 
    } finally {
      projectConnection2.close()
    }
    assertTrue(new File( 'src/test/projects/test2/build/freemarker/test1.ftlh' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test2/build/freemarker/test2.ftlh' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test2/build/libs/test2-0.0.0.1-freemarker.tar.gz' ).getAbsoluteFile().exists() )

    GradleConnector gradleConnection3 = gradleConnection.forProjectDirectory(test3Dir)
    ProjectConnection projectConnection3 = gradleConnection3.connect()
    try {
      BuildLauncher buildLauncher = projectConnection3.newBuild()
      buildLauncher.forTasks( 'clean', 'freemarker' )
      buildLauncher.run() 
    } finally {
      projectConnection3.close()
    }
    assertTrue(new File( 'src/test/projects/test3/build/freemarker/test1.ftlh' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test3/build/freemarker/test2.ftlh' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test3/build/test1.txt' ).getAbsoluteFile().exists() )
    assertTrue(new File( 'src/test/projects/test3/build/test2.txt' ).getAbsoluteFile().exists() )
    assertTrue(compareFiles(
      new File( 'src/test/projects/test3/build/test1.txt' ).getAbsoluteFile(),
      new File( 'src/test/test1.txt' ).getAbsoluteFile()
    ))
    assertTrue(compareFiles(
      new File( 'src/test/projects/test3/build/test2.txt' ).getAbsoluteFile(),
      new File( 'src/test/test2.txt' ).getAbsoluteFile()
    ))
  }

  boolean compareFiles(File file1, File file2) {
    FileInputStream fstream1 = new FileInputStream( file1 )
    FileInputStream fstream2 = new FileInputStream( file2 )
 
    DataInputStream in1 = new DataInputStream( fstream1 )
    BufferedReader br1 = new BufferedReader( new InputStreamReader( in1 ) )
    DataInputStream in2 = new DataInputStream( fstream2 )
    BufferedReader br2 = new BufferedReader( new InputStreamReader( in2 ) )
 
    String strLine1
    String strLine2
 
    while (
      ( ( strLine1 = br1.readLine() ) != null )
        &&
      ( ( strLine2 = br2.readLine() ) != null )
    ) {
      if ( ! strLine1.equals( strLine2 ) ) {
        println strLine1
        println strLine2
        return false
      }
    }
    return true
  }

}
