package it.pureorigins.framework.configuration

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

inline fun literal(name: String, block: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit) =
    CommandManager.literal(name).apply(block)!!

inline fun <T> argument(name: String, type: ArgumentType<T>, block: RequiredArgumentBuilder<ServerCommandSource, T>.() -> Unit) =
    CommandManager.argument(name, type).apply(block)!!

inline fun RequiredArgumentBuilder<ServerCommandSource, *>.suggests(crossinline block: CommandContext<ServerCommandSource>.(SuggestionsBuilder) -> Unit) =
    suggests { context, builder -> context.block(builder); builder.buildFuture() }!!

inline fun RequiredArgumentBuilder<ServerCommandSource, *>.success(crossinline block: CommandContext<ServerCommandSource>.() -> Unit) =
    executes { block(it); SINGLE_SUCCESS }!!

inline fun LiteralArgumentBuilder<ServerCommandSource>.success(crossinline block: CommandContext<ServerCommandSource>.() -> Unit) =
    executes { block(it); SINGLE_SUCCESS }!!
