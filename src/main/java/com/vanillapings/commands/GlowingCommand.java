package com.vanillapings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.vanillapings.VanillaPings;
import com.vanillapings.translation.Translations;
import net.minecraft.commands.CommandSourceStack;

public class GlowingCommand {
    public static int setGlowing(CommandContext<CommandSourceStack> ctx, boolean value) {
        VanillaPingsCommands.sendCommandFeedBack(Translations.GLOWING_ENABLED.constructMessage(ctx.getSource(), value, VanillaPings.SETTINGS.setGlowing(value)), ctx.getSource());
        return Command.SINGLE_SUCCESS;
    }

    public static int setGlowingFlash(CommandContext<CommandSourceStack> ctx, boolean value) {
        VanillaPingsCommands.sendCommandFeedBack(Translations.GLOWING_FLASH_ENABLED.constructMessage(ctx.getSource(), value, VanillaPings.SETTINGS.setGlowingFlash(value)), ctx.getSource());
        return Command.SINGLE_SUCCESS;
    }
}
