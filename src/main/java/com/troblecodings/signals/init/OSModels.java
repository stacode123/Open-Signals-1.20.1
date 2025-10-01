package com.troblecodings.signals.init;

import com.troblecodings.signals.OpenSignalsMain;
import com.troblecodings.signals.blocks.Signal;
import com.troblecodings.signals.core.SignalAngel;
import com.troblecodings.signals.models.CustomModelLoader;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class OSModels {

    private OSModels() {
    }

    @SubscribeEvent
    public static void registerAdditional(final ModelEvent.RegisterAdditional event) {
        // Register models from CustomModelLoader
        CustomModelLoader.getRegisteredModels().forEach((name, loaderList) -> {
            // Register the base model and inventory variant
            event.register(new ModelResourceLocation(OpenSignalsMain.MODID, name, "inventory"));
            event.register(new ModelResourceLocation(OpenSignalsMain.MODID, name, ""));
            
            // Register angel variants
            for (final SignalAngel angel : SignalAngel.values()) {
                event.register(new ModelResourceLocation(OpenSignalsMain.MODID, name,
                        "angel=" + angel.getNameWrapper()));
            }
        });
        
        // Register ghostblock
        event.register(new ModelResourceLocation(OpenSignalsMain.MODID, "ghostblock", "inventory"));
        event.register(new ModelResourceLocation(OpenSignalsMain.MODID, "ghostblock", ""));
        
        // Trigger the reload to populate CustomModelLoader
        CustomModelLoader.INSTANCE.onResourceManagerReload(null);
    }

    @SubscribeEvent
    public static void registerReload(final RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(CustomModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void addColor(final RegisterColorHandlersEvent.Block event) {
        final BlockColors colors = event.getBlockColors();
        OSBlocks.BLOCKS_TO_REGISTER.forEach(block -> {
            if (block instanceof Signal) {
                final Signal signal = (Signal) block;
                if (signal.hasCostumColor())
                    colors.register((_u1, _u2, _u3, index) -> signal.colorMultiplier(index), block);
            }
        });
    }
}