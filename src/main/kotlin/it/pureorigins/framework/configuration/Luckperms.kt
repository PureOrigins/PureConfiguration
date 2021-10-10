package it.pureorigins.framework.configuration

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.util.Tristate
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.PlayerEntity

private val luckperms = LuckPermsProvider.get()

fun PlayerEntity.hasPermission(name: String, orElsePermissionLevel: Int = 2): Boolean {
    val tristate = luckperms.getPlayerAdapter(PlayerEntity::class.java).getUser(this).cachedData.permissionData.checkPermission(name)
    return if (tristate == Tristate.UNDEFINED) hasPermissionLevel(orElsePermissionLevel) else tristate.asBoolean()
}

fun CommandSource.hasPermission(name: String, orElsePermissionLevel: Int = 2): Boolean {
    return (this as? PlayerEntity)?.hasPermission(name, orElsePermissionLevel) ?: this.hasPermissionLevel(orElsePermissionLevel)
}
