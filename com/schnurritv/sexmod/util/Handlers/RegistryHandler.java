package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.Main;
import com.schnurritv.sexmod.world.gen.WorldGenCustomStructures;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryHandler {
   public static void preInitRegistries() {
      GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
      EntityInit.registerEntities();
      RenderHandler.registerEntityRenders();
      EventHandler.registerEvents();
   }

   public static void initRegistries() {
      SoundsHandler.registerSounds();
      NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
      PacketHandler.registerMessages();
   }
}
