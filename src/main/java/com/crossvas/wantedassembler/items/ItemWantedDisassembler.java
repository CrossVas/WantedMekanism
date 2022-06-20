package com.crossvas.wantedassembler.items;

import com.crossvas.wantedassembler.items.utils.FakeUseOnContext;
import mekanism.common.item.gear.ItemAtomicDisassembler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemWantedDisassembler extends ItemAtomicDisassembler {

    public ItemWantedDisassembler(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            ItemStack stack = player.getMainHandItem();
            if (player.isCrouching()){
                Map<Enchantment, Integer> enchMap = new IdentityHashMap<>();
                String mode = ChatFormatting.BLUE + I18n.get("info.toolMode") + ": ";
                String modeSilk = ChatFormatting.GREEN + I18n.get("info.mode.silk");
                String modeFortune = ChatFormatting.AQUA + I18n.get("info.mode.fortune");
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player) == 0) {
                    enchMap.put(Enchantments.SILK_TOUCH, 1);
                    player.sendMessage(new TranslatableComponent(mode + modeSilk), Util.NIL_UUID);
                } else {
                    enchMap.put(Enchantments.BLOCK_FORTUNE, 3);
                    player.sendMessage(new TranslatableComponent(mode + modeFortune), Util.NIL_UUID);
                }
                EnchantmentHelper.setEnchantments(enchMap, stack);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Player player = context.getPlayer();
        Direction face = context.getClickedFace();
        if (!world.isClientSide()) {
            assert player != null;
            if (!player.isCrouching()) {
                int torchSlot = getTorchSlot(player.getInventory());
                if (torchSlot != -1) {
                    if (face != Direction.DOWN) {
                        ItemStack fakeStack = new ItemStack(Blocks.TORCH);
                        InteractionResult result = fakeStack.useOn(new FakeUseOnContext(context, fakeStack));
                        if (result.consumesAction()) {
                            SoundType soundType = Blocks.TORCH.defaultBlockState().getBlock().getSoundType(Blocks.TORCH.defaultBlockState(), world, pos, player);
                            world.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0f, 0.8F);
                            int torchCnt = player.getInventory().getItem(torchSlot).getCount();
                            if (player.isCreative()) {
                                torchCnt--;
                            }
                            if (torchCnt > 0) {
                                player.getInventory().getItem(torchSlot).shrink(1);
                            }
                        }
                    }
                }
            }
        }
        return super.useOn(context);
    }

    public int getTorchSlot(Inventory inv) {
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
