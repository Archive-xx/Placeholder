package com.schnurritv.sexmod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PreloadModels {
   public boolean didIt = false;

   @SubscribeEvent
   public void PenisSauce(LivingUpdateEvent event) {
      if (!this.didIt && event.getEntity() instanceof EntityPlayer) {
         this.SpawnPreloders((EntityPlayer)event.getEntity(), event.getEntity().field_70170_p);
         this.didIt = true;
      }

   }

   private void SpawnPreloders(EntityPlayer player, World world) {
   }
}
