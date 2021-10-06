package it.pureorigins.framework.configuration

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

fun ServerCommandSource.sendFeedback(text: Text?) {
    if (text != null) {
        sendFeedback(text, true)
    }
}

fun PlayerEntity.sendChatMessage(text: Text?) {
    if (text != null) {
        sendMessage(text, false)
    }
}

fun PlayerEntity.sendActionBar(text: Text?) {
    if (text != null) {
        sendMessage(text, true)
    }
}
