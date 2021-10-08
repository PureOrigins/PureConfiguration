package it.pureorigins.framework.configuration

import net.luckperms.api.LuckPermsProvider
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.PlayerEntity

private val luckperms = LuckPermsProvider.get()

internal fun PlayerEntity.hasPermission(name: String) = luckperms.getPlayerAdapter(PlayerEntity::class.java).getUser(this).cachedData.permissionData.checkPermission(name).asBoolean()
internal fun CommandSource.hasPermission(name: String) = (this as? PlayerEntity)?.hasPermission(name) ?: true
