package com.schnurritv.sexmod.util;

import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

public class Reference {
   public static final String MOD_ID = "sexmod";
   public static final String NAME = "Sex Mod";
   public static final String VERSION = "1.0";
   public static final String ACCEPTED_VERSION = "[1.12.2]";
   public static final String CLIENT = "com.schnurritv.sexmod.proxy.ClientProxy";
   public static final String COMMON = "com.schnurritv.sexmod.proxy.CommonProxy";
   public static final Random RANDOM = new Random();
   public static final int ENTITY_JENNY = 177013;
   public static final int ENTITY_ELLIE = 228922;
   public static final int GUI_GIRL = 0;
   public static MinecraftServer server;

   public static Vec3d Lerp(Vec3d start, Vec3d end, double step) {
      if (step == 0.0D) {
         return end;
      } else {
         try {
            Vec3d distance = end.func_178788_d(start);
            return start.func_72441_c(distance.field_72450_a / step, distance.field_72448_b / step, distance.field_72449_c / step);
         } catch (NullPointerException var5) {
            System.out.println("couldn't calculate distance @Reference.Lerp");
            System.out.println(start);
            System.out.println(end);
            System.out.println(step);
            return end;
         }
      }
   }

   public static double Lerp(double start, double end, double step) {
      return start + (end - start) * step;
   }
}
