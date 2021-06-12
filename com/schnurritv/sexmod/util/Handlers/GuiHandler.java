package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.gui.ContainerUI;
import com.schnurritv.sexmod.gui.MenuUI;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return ID == 0 ? new ContainerUI() : null;
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (ID == 0) {
         ArrayList girls = GirlEntity.getGirlsByPos(new BlockPos(x, y, z));
         Iterator var8 = girls.iterator();

         GirlEntity girl;
         do {
            if (!var8.hasNext()) {
               return new MenuUI(player.field_71071_by, x, y, z);
            }

            girl = (GirlEntity)var8.next();
         } while(!(Boolean)girl.animationParameters.get(GirlEntity.AnimationParameters.ISDOINGACTION));

         return null;
      } else {
         return null;
      }
   }
}
