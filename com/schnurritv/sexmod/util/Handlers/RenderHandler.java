package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.girls.GirlRenderer;
import com.schnurritv.sexmod.girls.ellie.EllieEntity;
import com.schnurritv.sexmod.girls.ellie.EllieModel;
import com.schnurritv.sexmod.girls.jenny.JennyEntity;
import com.schnurritv.sexmod.girls.jenny.JennyModel;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
   public static void registerEntityRenders() {
      RenderingRegistry.registerEntityRenderingHandler(JennyEntity.class, (manager) -> {
         return new GirlRenderer(manager, new JennyModel(), -0.15D);
      });
      RenderingRegistry.registerEntityRenderingHandler(EllieEntity.class, (manager) -> {
         return new GirlRenderer(manager, new EllieModel(), 0.05D);
      });
   }
}
