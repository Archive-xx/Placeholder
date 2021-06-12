package com.schnurritv.sexmod.girls.ellie;

import com.schnurritv.sexmod.girls.GirlModel;
import net.minecraft.util.ResourceLocation;

public class EllieModel extends GirlModel {
   ResourceLocation[] models = new ResourceLocation[]{new ResourceLocation("sexmod", "geo/ellie/nude.geo.json"), new ResourceLocation("sexmod", "geo/ellie/dressed.geo.json")};

   public ResourceLocation getModel(int whichOne) {
      if (whichOne > this.models.length) {
         System.out.println("Ellie doesn't have an outfit Nr." + whichOne + " so im just making her nude lol");
         return this.models[0];
      } else {
         return this.models[whichOne];
      }
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("sexmod", "textures/entity/ellie/ellie.png");
   }

   public ResourceLocation getAnimationFile() {
      return new ResourceLocation("sexmod", "animations/ellie/ellie.animation.json");
   }

   public String getSkinName() {
      return "ellieskin";
   }
}
