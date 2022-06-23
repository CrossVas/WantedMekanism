package com.crossvas.wantedassembler.items;

import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.item.gear.ItemArmoredJetpack;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;

public class ItemWantedJetpack extends ItemArmoredJetpack {

    public ItemWantedJetpack(Properties properties) {
        super(properties);
    }

    @Override
    public void changeMode(@Nonnull PlayerEntity player, @Nonnull ItemStack stack, int shift, boolean displayChangeMessage) {
        JetpackMode mode = getMode(stack);
        Minecraft mc = Minecraft.getInstance();
        boolean jumpKeyDown = mc.options.keyJump.isDown();
        if (MekanismKeyHandler.chestModeSwitchKey.isDown()) {
            if (!jumpKeyDown) {
                if (mode == JetpackMode.NORMAL || mode == JetpackMode.HOVER) {
                    setMode(stack, JetpackMode.DISABLED);
                    player.sendMessage(MekanismUtils.logFormat(MekanismLang.JETPACK_MODE_CHANGE.translate(JetpackMode.DISABLED)), Util.NIL_UUID);
                } else if (mode == JetpackMode.DISABLED) {
                    setMode(stack, JetpackMode.HOVER);
                    player.sendMessage(MekanismUtils.logFormat(MekanismLang.JETPACK_MODE_CHANGE.translate(JetpackMode.HOVER)), Util.NIL_UUID);
                }
            } else {
                if (mode == JetpackMode.NORMAL) {
                    setMode(stack, JetpackMode.HOVER);
                    player.sendMessage(MekanismUtils.logFormat(MekanismLang.JETPACK_MODE_CHANGE.translate(JetpackMode.HOVER)), Util.NIL_UUID);
                }
                if (mode == JetpackMode.HOVER) {
                    setMode(stack, JetpackMode.NORMAL);
                    player.sendMessage(MekanismUtils.logFormat(MekanismLang.JETPACK_MODE_CHANGE.translate(JetpackMode.NORMAL)), Util.NIL_UUID);
                }
            }
        }
    }
}
