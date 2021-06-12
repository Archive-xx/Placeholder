package com.schnurritv.sexmod;

import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.proxy.CommonProxy;
import com.schnurritv.sexmod.util.Handlers.RegistryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import software.bernie.geckolib3.GeckoLib;

@Mod(
   modid = "sexmod",
   name = "Sex Mod",
   version = "1.0",
   acceptedMinecraftVersions = "[1.12.2]"
)
public class Main {
   @Instance
   public static Main instance;
   @SidedProxy(
      clientSide = "com.schnurritv.sexmod.proxy.ClientProxy",
      serverSide = "com.schnurritv.sexmod.proxy.CommonProxy"
   )
   public static CommonProxy proxy;

   public Main() {
      GeckoLib.initialize();
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      RegistryHandler.preInitRegistries();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      RegistryHandler.initRegistries();
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
   }

   @EventHandler
   public static void clearEntityList(FMLServerStoppedEvent event) {
      GirlEntity.girlEntities.clear();
   }
}
