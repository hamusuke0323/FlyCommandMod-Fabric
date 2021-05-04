package com.hamusuke.flycommod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class CommandFlying {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("fly").requires((permission) -> permission.hasPermissionLevel(2));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("allow").executes(e -> allow(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("noAllow").executes(e -> disallow(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("noGravity").executes(e -> noGravity(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("gravity").executes(e -> gravity(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("getFlySpeed").executes(e -> getFlySpeed(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("getWalkSpeed").executes(e -> getWalkSpeed(e, EntityArgumentType.getPlayers(e, "targets")))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setFlySpeed").then(CommandManager.argument("speed", FloatArgumentType.floatArg()).executes(e -> setFlySpeed(e, EntityArgumentType.getPlayers(e, "targets"), FloatArgumentType.getFloat(e, "speed"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setWalkSpeed").then(CommandManager.argument("speed", FloatArgumentType.floatArg()).executes(e -> setWalkSpeed(e, EntityArgumentType.getPlayers(e, "targets"), FloatArgumentType.getFloat(e, "speed"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("allowEdit").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> allowEdit(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("disableDamage").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> disableDamage(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("isFlying").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> isFlying(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setGlowing").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> isGlowing(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setInvulnerable").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> isInvulnerable(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		literalargumentbuilder.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("setInvisible").then(CommandManager.argument("boolean", BoolArgumentType.bool()).executes(e -> isInvisible(e, EntityArgumentType.getPlayers(e, "targets"), BoolArgumentType.getBool(e, "boolean"))))));

		dispatcher.register(literalargumentbuilder);
	}

	private static int allow(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		int i = 0;
		for (ServerPlayerEntity serverplayerentity : players) {
			if (!serverplayerentity.abilities.allowFlying) {
				serverplayerentity.abilities.allowFlying = true;
				serverplayerentity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allow.single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allow.multiple", i), true);
		}
		return i;
	}

	private static int disallow(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		int i = 0;
		for (ServerPlayerEntity serverplayerentity : players) {
			if (serverplayerentity.abilities.allowFlying) {
				serverplayerentity.abilities.allowFlying = false;
				serverplayerentity.abilities.flying = false;
				serverplayerentity.sendAbilitiesUpdate();
				serverplayerentity.fallDistance = -(float) (serverplayerentity.getY() + 10.0D);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.noallow.single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.noallow.multiple", i), true);
		}
		return i;
	}

	private static int noGravity(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		int i = 0;
		for (ServerPlayerEntity serverplayerentity : players) {
			if (!serverplayerentity.hasNoGravity()) {
				serverplayerentity.setNoGravity(true);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.nogravity.single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.nogravity.multiple", i), true);
		}
		return i;
	}

	private static int gravity(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		int i = 0;
		for (ServerPlayerEntity serverplayerentity : players) {
			if (serverplayerentity.hasNoGravity()) {
				serverplayerentity.setNoGravity(false);
				serverplayerentity.fallDistance = -(float) (serverplayerentity.getY() + 10.0D);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.gravity.single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.gravity.multiple", i), true);
		}
		return i;
	}

	private static int getFlySpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverplayerentity -> source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.getflyspeed", serverplayerentity.getDisplayName(), serverplayerentity.abilities.getFlySpeed()), false));
		return players.size();
	}

	private static int getWalkSpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverplayerentity -> source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.getwalkspeed", serverplayerentity.getDisplayName(), serverplayerentity.abilities.getWalkSpeed()), false));
		return players.size();
	}

	private static int setFlySpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, float flyspeed) {
		int i = 0;
		if (!source.getSource().getMinecraftServer().isSinglePlayer()) {
			source.getSource().sendError(new TranslatableText("hamusuke.command.fly.error"));
			return i;
		}
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.abilities.getFlySpeed() != flyspeed) {
				serverPlayerEntity.abilities.setFlySpeed(flyspeed);
				serverPlayerEntity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setflyspeed.single", players.iterator().next().getDisplayName(), flyspeed), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setflyspeed.multiple", i, flyspeed), true);
		}
		return i;
	}

	private static int setWalkSpeed(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, float walkspeed) {
		int i = 0;
		if (!source.getSource().getMinecraftServer().isSinglePlayer()) {
			source.getSource().sendError(new TranslatableText("hamusuke.command.fly.error"));
			return i;
		}
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.abilities.getWalkSpeed() != walkspeed) {
				serverPlayerEntity.abilities.setWalkSpeed(walkspeed);
				serverPlayerEntity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setwalkspeed.single", players.iterator().next().getDisplayName(), walkspeed), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setwalkspeed.multiple", i, walkspeed), true);
		}
		return i;
	}

	private static int allowEdit(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.abilities.allowModifyWorld != flag) {
				serverPlayerEntity.abilities.allowModifyWorld = flag;
				serverPlayerEntity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allowedit." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.allowedit." + flag + ".multiple", i), true);
		}
		return i;
	}

	private static int disableDamage(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.abilities.invulnerable != flag) {
				serverPlayerEntity.abilities.invulnerable = flag;
				serverPlayerEntity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.disabledamage." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.disabledamage." + flag + ".multiple", i), true);
		}
		return i;
	}

	private static int isFlying(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.abilities.flying != flag) {
				serverPlayerEntity.abilities.flying = flag;
				serverPlayerEntity.sendAbilitiesUpdate();
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.isflying." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.isflying." + flag + ".multiple", i), true);
		}
		return i;
	}

	private static int isGlowing(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.isGlowing() != flag) {
				serverPlayerEntity.setGlowing(flag);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setglowing." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setglowing." + flag + ".multiple", i), true);
		}
		return i;
	}

	private static int isInvulnerable(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.isInvulnerable() != flag) {
				serverPlayerEntity.setInvulnerable(flag);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setinvulnerable." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.setinvulnerable." + flag + ".multiple", i), true);
		}
		return i;
	}

	private static int isInvisible(CommandContext<ServerCommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		int i = 0;
		for (ServerPlayerEntity serverPlayerEntity : players) {
			if (serverPlayerEntity.isInvisible() != flag) {
				serverPlayerEntity.setInvisible(flag);
				++i;
			}
		}

		if (i == 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.invisible." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else if (i > 1) {
			source.getSource().sendFeedback(new TranslatableText("hamusuke.command.fly.success.invisible." + flag + ".multiple", i), true);
		}
		return i;
	}
}
