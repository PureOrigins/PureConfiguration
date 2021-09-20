package it.pureorigins.framework.configuration

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandOutput
import net.minecraft.text.Text
import net.minecraft.util.Util.NIL_UUID

fun CommandOutput.sendSystemMessage(text: Text?) {
    if (text != null) {
        sendSystemMessage(text, NIL_UUID)
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
