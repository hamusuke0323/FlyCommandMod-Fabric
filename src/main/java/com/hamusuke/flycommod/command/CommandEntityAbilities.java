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
import java.util.stream.Collectors;

public class CommandEntityAbilities {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("entityabilities").requires((permission) -> permission.hasPermissionLevel(2));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("noGravity").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return noGravity(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.hasNoGravity() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setGlowing").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setGlowing(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isGlowing() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvulnerable").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvulnerable(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isInvulnerable() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvisible").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvisible(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isInvisible() != flag).collect(Collectors.toList()), flag);
		}))));

		dispatcher.register(literalArgumentBuilder);
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
		} else {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.nogravity." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setGlowing(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setGlowing(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setglowing." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setglowing." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvulnerable(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvulnerable(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvisible(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvisible(flag));

		if (entities.size() == 1) {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.invisible." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(new TranslatableText("hamusuke.command.entityabilities.success.invisible." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}
}
