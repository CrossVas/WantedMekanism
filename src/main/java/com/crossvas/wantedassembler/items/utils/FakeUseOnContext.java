package com.crossvas.wantedassembler.items.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Extension of {@link UseOnContext} to work around the needed constructor being private. This is
 * useful for simulating the player using an item without needing to place the item in the player's
 * hand.
 *
 * @author SilentChaos512
 */

public class FakeUseOnContext extends UseOnContext {
    public FakeUseOnContext(UseOnContext original, ItemStack fakeItem) {
        super(original.getLevel(), original.getPlayer(), original.getHand(), fakeItem,
                new BlockHitResult(original.getClickLocation(), original.getClickedFace(), original.getClickedPos(), original.isInside()));
    }
}
