package com.hamusuke.flycommod.command;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class CommandFlying {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("fly").requires((permission) -> permission.hasPermissionLevel(2));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("allow").executes(e -> allow(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> !serverPlayerEntity.getAbilities().allowFlying).toList()))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("noAllow").executes(e -> disallow(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().allowFlying).toList()))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("noGravity").executes(e -> noGravity(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> !serverPlayerEntity.hasNoGravity()).toList()))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("gravity").executes(e -> gravity(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(Entity::hasNoGravity).toList()))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("getFlySpeed").executes(e -> sendFlySpeedToCommandSource(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("getWalkSpeed").executes(e -> sendWalkSpeedToCommandSource(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setFlySpeed").then(CommandManager.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setFlySpeed(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().getFlySpeed() != speed).toList(), speed);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setWalkSpeed").then(CommandManager.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setWalkSpeed(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().getWalkSpeed() != speed).toList(), speed);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("allowEdit").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return allowEdit(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().allowModifyWorld != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("disableDamage").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return disableDamage(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().invulnerable != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("isFlying").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return setFlying(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.getAbilities().flying != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setGlowing").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return setGlowing(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isGlowing() != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setInvulnerable").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return setInvulnerable(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isInvulnerable() != flag).toList(), flag);
		}))));

		literalArgumentBuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setInvisible").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return setInvisible(e, EntityArgumentType.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isInvisible() != flag).toList(), flag);
		}))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int allow(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().allowFlying = true;
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allow.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allow.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int disallow(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().allowFlying = false;
			serverPlayerEntity.getAbilities().flying = false;
			serverPlayerEntity.sendAbilitiesUpdate();
			((LivingEntityInvoker) serverPlayerEntity).markNoFallDamage(!serverPlayerEntity.isOnGround());
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.noallow.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.noallow.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int noGravity(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setNoGravity(true));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.nogravity.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.nogravity.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int gravity(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.setNoGravity(false);
			((LivingEntityInvoker) serverPlayerEntity).markNoFallDamage(!serverPlayerEntity.isOnGround());
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.gravity.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.gravity.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int sendFlySpeedToCommandSource(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.getflyspeed", serverPlayerEntity.getDisplayName(), serverPlayerEntity.getAbilities().getFlySpeed()), false));
		return players.size();
	}

	private static int sendWalkSpeedToCommandSource(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.getwalkspeed", serverPlayerEntity.getDisplayName(), serverPlayerEntity.getAbilities().getWalkSpeed()), false));
		return players.size();
	}

	private static int setFlySpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, float flySpeed) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().setFlySpeed(flySpeed);
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setflyspeed.single", players.iterator().next().getDisplayName(), flySpeed), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setflyspeed.multiple", players.size(), flySpeed), true);
		}

		return players.size();
	}

	private static int setWalkSpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, float walkSpeed) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().setWalkSpeed(walkSpeed);
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setwalkspeed.single", players.iterator().next().getDisplayName(), walkSpeed), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setwalkspeed.multiple", players.size(), walkSpeed), true);
		}

		return players.size();
	}

	private static int allowEdit(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().allowModifyWorld = flag;
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allowedit." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allowedit." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int disableDamage(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().invulnerable = flag;
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.disabledamage." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.disabledamage." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int setFlying(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.getAbilities().flying = flag;
			serverPlayerEntity.sendAbilitiesUpdate();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.isflying." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.isflying." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int setGlowing(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setGlowing(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setglowing." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setglowing." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int setInvulnerable(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setInvulnerable(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setinvulnerable." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setinvulnerable." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int setInvisible(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setInvisible(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.invisible." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.invisible." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}
}
