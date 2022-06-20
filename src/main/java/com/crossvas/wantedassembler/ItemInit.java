package com.crossvas.wantedassembler;

import com.crossvas.wantedassembler.items.ItemWantedDisassembler;
import com.crossvas.wantedassembler.items.ItemWantedJetpack;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public class ItemInit {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister("wantedmekanism");

    public static final ItemRegistryObject<ItemWantedDisassembler> assembler = ITEMS.register("wantedassembler", ItemWantedDisassembler::new);
    public static final ItemRegistryObject<ItemWantedJetpack> jetpack = ITEMS.register("wantedjetpack", ItemWantedJetpack::new);

    public static void register(IEventBus event) {
        ITEMS.register(event);
    }
}
