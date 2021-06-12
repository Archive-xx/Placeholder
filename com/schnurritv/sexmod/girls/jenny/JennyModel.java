package com.schnurritv.sexmod.girls.jenny;

import com.schnurritv.sexmod.girls.GirlModel;
import net.minecraft.util.ResourceLocation;

public class JennyModel extends GirlModel {
   ResourceLocation[] models = new ResourceLocation[]{new ResourceLocation("sexmod", "geo/jenny/jennynude.geo.json"), new ResourceLocation("sexmod", "geo/jenny/jennydressed.geo.json")};

   public ResourceLocation getSkin() {
      return new ResourceLocation("sexmod", "textures/entity/jenny/jenny.png");
   }

   public ResourceLocation getAnimationFile() {
      return new ResourceLocation("sexmod", "animations/jenny/jenny.animation.json");
   }

   public String getSkinName() {
      return "jennyskin";
   }

   public ResourceLocation getModel(int whichOne) {
      if (whichOne > this.models.length) {
         System.out.println("Jenny doesn't have an outfit Nr." + whichOne + " so im just making her nude lol");
         return this.models[0];
      } else {
         return this.models[whichOne];
      }
   }
}
