package com.hamusuke.flycommod.command;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class EntityAbilitiesCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("entityabilities").requires((permission) -> permission.hasPermissionLevel(2));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("noGravity").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return noGravity(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.hasNoGravity() != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setGlowing").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setGlowing(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isGlowing() != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvulnerable").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvulnerable(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isInvulnerable() != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.literal("setInvisible").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvisible(command.getSource(), EntityArgumentType.getEntities(command, "targets").stream().filter(entity -> entity.isInvisible() != flag).toList(), flag);
		}))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int noGravity(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach(entity -> {
			entity.setNoGravity(flag);
			if (!flag && entity instanceof LivingEntityInvoker invoker) {
				invoker.markNoFallDamage(!entity.isOnGround());
			}
		});

		if (entities.size() == 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.nogravity." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.nogravity." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setGlowing(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach(entity -> entity.setGlowing(flag));

		if (entities.size() == 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.setglowing." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.setglowing." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvulnerable(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach(entity -> entity.setInvulnerable(flag));

		if (entities.size() == 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvisible(ServerCommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach(entity -> entity.setInvisible(flag));

		if (entities.size() == 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.invisible." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else if (entities.size() > 1) {
			source.sendFeedback(Text.translatable("hamusuke.command.entityabilities.success.invisible." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}
}
