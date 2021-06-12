package com.schnurritv.sexmod.events;

import com.schnurritv.sexmod.girls.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlePlayerMovement {
   public static boolean active = true;
   public static boolean isThrusting = false;
   public static boolean isCumming = false;

   @SubscribeEvent
   public void PreventPlayerFromMoving(InputUpdateEvent event) {
      MovementInput movement = event.getMovementInput();
      isThrusting = movement.field_78899_d;
      if (!active) {
         if (movement.field_78899_d) {
            GirlEntity.sendThrust(Minecraft.func_71410_x().field_71439_g.getPersistentID());
         }

         if (movement.field_78901_c) {
            GirlEntity.sendCum(Minecraft.func_71410_x().field_71439_g.getPersistentID());
         }

         isThrusting = movement.field_78899_d;
         isCumming = movement.field_78901_c;
         movement.field_187256_d = false;
         movement.field_187255_c = false;
         movement.field_187257_e = false;
         movement.field_187258_f = false;
         movement.field_78899_d = false;
         movement.field_78901_c = false;
         movement.field_192832_b = 0.0F;
         movement.field_78902_a = 0.0F;
      }

   }

   @SubscribeEvent
   public void PreventPlayerFromTakingAction(MouseEvent event) {
      event.setCanceled(!active);
   }
}
