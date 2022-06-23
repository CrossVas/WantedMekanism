package com.crossvas.wantedassembler.items;

import com.crossvas.wantedassembler.items.utils.FakeItemUseContext;
import mekanism.client.render.item.ISTERProvider;
import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemWantedDisassembler extends ItemAtomicDisassembler {

    public ItemWantedDisassembler(Item.Properties stats) {
        super(stats);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ItemStack stack = player.getMainHandItem();
            if (player.isCrouching()) {
                Map<Enchantment, Integer> enchMap = new IdentityHashMap<>();
                String mode = TextFormatting.BLUE + I18n.get("info.toolMode") + ": ";
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player) == 0) {
                    enchMap.put(Enchantments.SILK_TOUCH, 1);
                    player.sendMessage(new StringTextComponent(mode + TextFormatting.GREEN + I18n.get("info.mode.silk")), Util.NIL_UUID);
                } else {
                    enchMap.put(Enchantments.BLOCK_FORTUNE, 3);
                    player.sendMessage(new StringTextComponent(mode + TextFormatting.AQUA + I18n.get("info.mode.fortune")), Util.NIL_UUID);
                }
                EnchantmentHelper.setEnchantments(enchMap, stack);
            }
         }
        return super.use(world, player, hand);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        Direction face = context.getClickedFace();
        if (!world.isClientSide()) {
            assert player != null;
            if (!player.isCrouching()) {
                int torchSlot = getTorchSlot(player.inventory);
                if (torchSlot != -1) {
                    if (face != Direction.DOWN) {
                        ItemStack fakeStack = new ItemStack(Blocks.TORCH);
                        ActionResultType result = fakeStack.useOn(new FakeItemUseContext(context, fakeStack));
                        if (result.consumesAction()) {
                            SoundType soundType = Blocks.TORCH.defaultBlockState().getBlock().getSoundType(Blocks.TORCH.defaultBlockState(), world, pos, player);
                            world.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, 1.0f, 0.8F);
                            int torchCnt = player.inventory.getItem(torchSlot).getCount();
                            if (player.isCreative()) {
                                torchCnt--;
                            }
                            if (torchCnt > 0) {
                                player.inventory.getItem(torchSlot).shrink(1);
                            }
                        }
                    }
                }
            }
        }
        return super.useOn(context);
    }

    public int getTorchSlot(PlayerInventory inv) {
        int torchSlot = -1;
        for (int slot = 0; slot <= inv.getContainerSize(); slot++) {
            String itemName = inv.getItem(slot).getItem().getDescriptionId();
            if (itemName.contains("torch") && (!itemName.contains("redstone"))) {
                torchSlot = slot;
                break;
            }
        }
        return torchSlot;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 45;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return true;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

}
