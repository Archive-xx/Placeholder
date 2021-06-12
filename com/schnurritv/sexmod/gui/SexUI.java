package com.schnurritv.sexmod.gui;

import com.schnurritv.sexmod.events.HandlePlayerMovement;
import com.schnurritv.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class SexUI extends Gui {
   static ResourceLocation buttons = new ResourceLocation("sexmod", "textures/gui/buttons.png");
   static ResourceLocation hornyMeter = new ResourceLocation("sexmod", "textures/gui/hornymeter.png");
   public static boolean shouldBeRendered = false;
   static double cumPercentage = 0.0D;
   static double drawnCumPercentage;
   static float transitionStep;
   static float cumStep;
   static boolean keepSpacePressed;
   static float i;

   @SubscribeEvent
   public void renderUI(RenderGameOverlayEvent event) {
      if (shouldBeRendered && event.getType() == ElementType.TEXT) {
         Minecraft minecraft = Minecraft.func_71410_x();
         if (transitionStep < 1.0F) {
            transitionStep += minecraft.func_193989_ak() / 25.0F;
         } else {
            transitionStep = 1.0F;
         }

         GL11.glPushMatrix();
         minecraft.field_71446_o.func_110577_a(buttons);
         GL11.glScalef(0.35F, 0.35F, 0.35F);
         int height;
         if (cumPercentage >= 1.0D) {
            if (HandlePlayerMovement.isCumming) {
               keepSpacePressed = true;
            }

            height = keepSpacePressed ? 54 : 0;
            this.func_73729_b(240, 160, 0, 108 + height, 256, 52);
         }

         if (!keepSpacePressed) {
            height = HandlePlayerMovement.isThrusting ? 54 : 0;
            this.func_73729_b((int)Reference.Lerp(-200.0D, 98.0D, (double)transitionStep), 405, 0, height, 158, 54);
         }

         GL11.glScalef(2.857143F, 2.857143F, 2.857143F);
         minecraft.field_71446_o.func_110577_a(hornyMeter);
         GL11.glScalef(0.75F, 0.75F, 0.75F);
         this.func_73729_b(10, (int)Reference.Lerp(-200.0D, 10.0D, (double)transitionStep), 0, 0, 146, 175);
         drawnCumPercentage = Reference.Lerp(drawnCumPercentage, cumPercentage, (double)minecraft.func_193989_ak());
         height = (int)Reference.Lerp(0.0D, 160.0D, drawnCumPercentage);
         int textureY = (int)Reference.Lerp(167.0D, 8.0D, drawnCumPercentage);
         double y = Reference.Lerp(178.0D, 18.0D, drawnCumPercentage);
         if (!keepSpacePressed) {
            this.func_73729_b(67, (int)Reference.Lerp(-45.0D, y, (double)transitionStep), 159, textureY, 32, height);
            this.func_73729_b(120, (int)Reference.Lerp(-58.0D, Reference.Lerp(178.0D, 149.0D, 1.0D - drawnCumPercentage), (double)transitionStep), 212, (int)Reference.Lerp(169.0D, 141.0D, 1.0D - drawnCumPercentage), 28, (int)Reference.Lerp(1.0D, 29.0D, 1.0D - drawnCumPercentage));
            this.func_73729_b(18, (int)Reference.Lerp(-58.0D, Reference.Lerp(178.0D, 149.0D, 1.0D - drawnCumPercentage), (double)transitionStep), 212, (int)Reference.Lerp(169.0D, 141.0D, 1.0D - drawnCumPercentage), 28, (int)Reference.Lerp(1.0D, 29.0D, 1.0D - drawnCumPercentage));
         } else {
            cumStep += minecraft.func_193989_ak() / 15.0F;
            this.func_73729_b(67, (int)Reference.Lerp(18.0D, -300.0D, (double)cumStep), 159, 8, 32, 160);
         }

         GL11.glPopMatrix();
      }

   }

   public static void addCumPercentage(double amount) {
      cumPercentage += amount;
      cumPercentage = cumPercentage > 1.0D ? 1.0D : cumPercentage;
   }

   public static void resetCumPercentage() {
      cumPercentage = 0.0D;
      keepSpacePressed = false;
   }

   static {
      drawnCumPercentage = cumPercentage;
      transitionStep = 0.0F;
      cumStep = 0.0F;
      keepSpacePressed = false;
      i = 0.0F;
   }
}
