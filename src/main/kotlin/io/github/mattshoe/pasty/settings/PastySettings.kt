package io.github.mattshoe.pasty.settings

import com.intellij.openapi.components.BaseState

class PastySettings: BaseState() {
    var proxyAddress by string()
    var pollInterval by property(3)
}
