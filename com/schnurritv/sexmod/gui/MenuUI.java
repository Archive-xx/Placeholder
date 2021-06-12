package com.schnurritv.sexmod.gui;

import com.schnurritv.sexmod.events.HandlePlayerMovement;
import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.util.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MenuUI extends GuiContainer {
   private static final ResourceLocation SOPHI_HEAD = new ResourceLocation("sexmod:textures/gui/sophihead.png");
   private final InventoryPlayer player;
   public static GirlEntity entityGirl;
   private GirlEntity girl;
   static float buttonTransitionStep = 0.0F;
   static float priceTransitionStep = 0.0F;

   public MenuUI(InventoryPlayer player, int x, int y, int z) {
      super(new ContainerUI());
      this.girl = entityGirl;
      this.player = player;
   }

   protected void func_146979_b(int mouseX, int mouseY) {
      this.field_146292_n.clear();
      ScaledResolution resolution = new ScaledResolution(this.field_146297_k);
      int screenWidth = resolution.func_78326_a();
      int screenHeight = resolution.func_78328_b();
      if (buttonTransitionStep < 1.0F) {
         buttonTransitionStep += this.field_146297_k.func_193989_ak() / 5.0F;
      } else {
         buttonTransitionStep = 1.0F;
      }

      int x = (int)Reference.Lerp(-30.0D, 120.0D, (double)buttonTransitionStep);
      this.field_146292_n.add(new GuiButton(1, screenWidth - x, screenHeight - 150, 100, 20, "Blowjob"));
      this.field_146292_n.add(new GuiButton(2, screenWidth - x, screenHeight - 120, 100, 20, "Sex"));
      this.field_146292_n.add(new GuiButton(3, screenWidth - x, screenHeight - 90, 100, 20, "Strip"));
   }

   public void func_146281_b() {
      buttonTransitionStep = 0.0F;
      priceTransitionStep = 0.0F;
      super.func_146281_b();
   }

   protected void func_146284_a(GuiButton button) {
      ArrayList girlList = GirlEntity.getGirlsByPos(this.girl.func_180425_c());

      GirlEntity girl;
      for(Iterator var3 = girlList.iterator(); var3.hasNext(); girl.playerSheHasSexWith = this.player.field_70458_d) {
         girl = (GirlEntity)var3.next();
      }

      this.player.field_70458_d.func_71053_j();
      switch(button.field_146127_k) {
      case 1:
         HandlePlayerMovement.active = false;
         this.girl.startAnimation("blowjob");
         break;
      case 2:
         HandlePlayerMovement.active = false;
         this.girl.startAnimation("doggy");
         break;
      case 3:
         HandlePlayerMovement.active = false;
         this.girl.startAnimation("strip");
      }

   }

   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
      ScaledResolution resolution = new ScaledResolution(this.field_146297_k);
      int screenWidth = resolution.func_78326_a();
      int screenHeight = resolution.func_78328_b();
      if (priceTransitionStep < 1.0F) {
         priceTransitionStep += this.field_146297_k.func_193989_ak() / 5.0F;
      } else {
         if (priceTransitionStep < 2.0F) {
            priceTransitionStep += this.field_146297_k.func_193989_ak() / 5.0F;
         } else {
            priceTransitionStep = 2.0F;
         }

         int xText = (int)Reference.Lerp(120.0D, 161.0D, (double)(priceTransitionStep - 1.0F));
         int xItem = (int)Reference.Lerp(96.0D, 137.0D, (double)(priceTransitionStep - 1.0F));
         this.func_146279_a("3x    ", screenWidth - xText, screenHeight - 132);
         this.field_146296_j.func_175042_a(new ItemStack(Items.field_151166_bC, 1), screenWidth - xItem, screenHeight - 148);
         this.func_146279_a("2x    ", screenWidth - xText, screenHeight - 102);
         this.field_146296_j.func_175042_a(new ItemStack(Items.field_151045_i, 1), screenWidth - xItem, screenHeight - 118);
         this.func_146279_a("1x    ", screenWidth - xText, screenHeight - 72);
         this.field_146296_j.func_175042_a(new ItemStack(Items.field_151043_k, 1), screenWidth - xItem, screenHeight - 88);
      }
   }
}
