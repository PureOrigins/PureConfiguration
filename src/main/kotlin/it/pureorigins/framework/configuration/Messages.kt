package it.pureorigins.framework.configuration

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.text.Texts

fun ServerCommandSource.sendFeedback(text: Text?) {
    if (text != null) {
        sendFeedback(Texts.parse(this, text, entity, 0), true)
    }
}

@JvmOverloads
fun PlayerEntity.sendFeedback(text: Text?, commandSource: ServerCommandSource? = null) {
    if (text != null) {
        this.commandSource.sendFeedback(Texts.parse(commandSource, text, null, 0), false)
    }
}

@JvmOverloads
fun PlayerEntity.sendChatMessage(text: Text?, commandSource: ServerCommandSource? = null) {
    if (text != null) {
        sendMessage(Texts.parse(commandSource, text, null, 0), false)
    }
}

@JvmOverloads
fun PlayerEntity.sendActionBar(text: Text?, commandSource: ServerCommandSource? = null) {
    if (text != null) {
        sendMessage(Texts.parse(commandSource, text, null, 0), true)
    }
}
