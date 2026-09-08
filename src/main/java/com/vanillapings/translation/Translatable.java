package com.vanillapings.translation;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.commands.CommandSourceStack;

public class Translatable {
    @FunctionalInterface
    public interface TranslatableDefault<U> {
        U get();
    }

    @FunctionalInterface
    public interface TranslatableSupplier {
        MutableComponent get(Translator translator);
    }


    private final TranslatableSupplier supplier;

    public Translatable(TranslatableSupplier supplier) {
        this.supplier = supplier;
    }

    public MutableComponent constructMessage(Translator translator) {
        return supplier.get(translator);
    }

    public MutableComponent constructMessage() {
        return constructMessage(Translator.getTranslator());
    }

    public MutableComponent constructMessage(CommandSourceStack source) {
        return constructMessage(Translator.getTranslator(source));
    }
}
