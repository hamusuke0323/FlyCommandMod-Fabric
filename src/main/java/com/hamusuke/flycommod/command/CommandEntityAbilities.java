package com.hamusuke.flycommod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class CommandEntityAbilities {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("entityabilities").requires((permission) -> permission.hasPermissionLevel(2));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("noGravity").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> noGravity(command.getSource(), EntityArgumentType.getEntities(command, "targets"), BoolArgumentType.getBool(command, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setGlowing").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> setGlowing(command.getSource(), EntityArgumentType.getEntities(command, "targets"), BoolArgumentType.getBool(command, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvulnerable").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> setInvulnerable(command.getSource(), EntityArgumentType.getEntities(command, "targets"), BoolArgumentType.getBool(command, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvisible").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> setInvisible(command.getSource(), EntityArgumentType.getEntities(command, "targets"), BoolArgumentType.getBool(command, "boolean"))))));

		dispatcher.register(literalargumentbuilder);
	}

	private static int noGravity(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> {
			entity.setNoGravity(flag);
			if (!flag) {
				entity.fallDistance = -(float) (entity.getY() + 10.0D);
			}
		});

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.nogravity." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.nogravity." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setGlowing(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setGlowing(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setglowing." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setglowing." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvulnerable(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvulnerable(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvisible(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvisible(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.invisible." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.invisible." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}
}
