package com.schnurritv.sexmod.events;

import com.schnurritv.sexmod.girls.GirlEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoDamageForGirlsWhileHavingTheSex {
   @SubscribeEvent
   public void NoDamageForGirlsWhileHavingTheSex(LivingAttackEvent event) {
      if (event.getEntity() instanceof GirlEntity) {
      }

   }
}
