package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.events.HandlePlayerMovement;
import com.schnurritv.sexmod.events.NoDamageForGirlsWhileHavingTheSex;
import com.schnurritv.sexmod.events.PreloadModels;
import com.schnurritv.sexmod.events.RemoveEntityFromList;
import com.schnurritv.sexmod.events.SetFOVForSex;
import com.schnurritv.sexmod.gui.BlackScreenUI;
import com.schnurritv.sexmod.gui.SexUI;
import net.minecraftforge.common.MinecraftForge;

public class EventHandler {
   public static void registerEvents() {
      MinecraftForge.EVENT_BUS.register(new NoDamageForGirlsWhileHavingTheSex());
      MinecraftForge.EVENT_BUS.register(new RemoveEntityFromList());
      MinecraftForge.EVENT_BUS.register(new HandlePlayerMovement());
      MinecraftForge.EVENT_BUS.register(new PreloadModels());
      MinecraftForge.EVENT_BUS.register(new SetFOVForSex());
      MinecraftForge.EVENT_BUS.register(new SexUI());
      MinecraftForge.EVENT_BUS.register(new BlackScreenUI());
   }
}
