package ru.myweek_end.gradle.freemaker

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*
import ru.myweek_end.gradle.freemaker.FreeMakerTask

class FreeMakerTaskTest {

    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('freemaker', type: FreeMakerTask)
        assertTrue(task instanceof FreeMakerTask)
    }

}