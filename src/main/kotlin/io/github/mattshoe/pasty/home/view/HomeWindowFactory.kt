package io.github.mattshoe.pasty.home.view

import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import io.github.mattshoe.pasty.di.PastyApp
import io.github.mattshoe.pasty.home.viewmodel.HomeViewModel
import javax.inject.Inject
import javax.swing.JComponent


class HomeWindowFactory : ToolWindowFactory {

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        PastyApp.initializePlugin(toolWindow)

        PastyApp.dagger.inject(this)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        Disposer.register(toolWindow.disposable) {
            viewModel.onCleared()
        }

        toolWindow.contentManager.addContent(
            buildContent()
        )
    }

    override fun shouldBeAvailable(project: Project) = true

    private fun buildContent(): Content {
        return ContentFactory.getInstance().createContent(
            buildWindow(),
            null,
            false
        )
    }

    private fun buildWindow(): JComponent {
        return ComposePanel().apply {
            setContent {
                HomeWindow(viewModel)
            }
        }
    }
}
