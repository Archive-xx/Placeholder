package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.Main;
import com.schnurritv.sexmod.girls.ellie.EllieEntity;
import com.schnurritv.sexmod.girls.jenny.JennyEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
   public static void registerEntities() {
      registerEntity("jenny", JennyEntity.class, 177013, 50, 3286592, 12655237);
      registerEntity("ellie", EllieEntity.class, 228922, 50, 1447446, 9961472);
   }

   private static void registerEntity(String name, Class entity, int id, int range, int color1, int color2) {
      EntityRegistry.registerModEntity(new ResourceLocation("sexmod:" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
   }
}
