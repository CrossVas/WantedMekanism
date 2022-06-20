package com.crossvas.wantedassembler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("wantedmekanism")
public class WantedMekanism {

    public WantedMekanism() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
