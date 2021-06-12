package com.schnurritv.sexmod.gui;

import com.schnurritv.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlackScreenUI extends Gui {
   private static boolean shouldBeRendered = false;
   private static double step = 0.0D;
   static ResourceLocation transitionScreen = new ResourceLocation("sexmod", "textures/gui/transitionscreen.png");
   static ResourceLocation mirroredTransitionScreen = new ResourceLocation("sexmod", "textures/gui/mirroredtransitionscreen.png");
   static ResourceLocation blackScreen = new ResourceLocation("sexmod", "textures/gui/blackscreen.png");

   public static void activate() {
      shouldBeRendered = true;
   }

   @SubscribeEvent
   public void renderUI(RenderGameOverlayEvent event) {
      if (shouldBeRendered && event.getType() == ElementType.TEXT) {
         Minecraft mc = Minecraft.func_71410_x();
         step += (double)(mc.func_193989_ak() * 0.75F);
         float xOffset = (float)Reference.Lerp(-900.0D, 450.0D, 0.5D * Math.cos(step / 25.0D) + 0.5D);
         mc.field_71446_o.func_110577_a(transitionScreen);
         this.func_175174_a(xOffset, 0.0F, 0, (int)(step * 1.5D), 256, 256);
         mc.field_71446_o.func_110577_a(mirroredTransitionScreen);
         this.func_175174_a(xOffset + 600.0F, 0.0F, 0, (int)(step * 1.5D), 256, 256);
         mc.field_71446_o.func_110577_a(blackScreen);
         this.func_175174_a(xOffset + 200.0F, 0.0F, 0, 0, 400, 256);
         if (step > 30.0D) {
            SexUI.shouldBeRendered = false;
         }

         if (step > 69.0D) {
            step = 0.0D;
            shouldBeRendered = false;
         }
      }

   }
}
