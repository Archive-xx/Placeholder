package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.util.Reference;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler {
   public static final SoundEvent[] GIRLS_JENNY_AFTERSESSIONMOAN = new SoundEvent[5];
   public static final SoundEvent[] GIRLS_JENNY_AHH = new SoundEvent[10];
   public static final SoundEvent[] GIRLS_JENNY_BJMOAN = new SoundEvent[13];
   public static final SoundEvent[] GIRLS_JENNY_GIGGLE = new SoundEvent[5];
   public static final SoundEvent[] GIRLS_JENNY_HAPPYOH = new SoundEvent[3];
   public static final SoundEvent[] GIRLS_JENNY_HEAVYBREATHING = new SoundEvent[8];
   public static final SoundEvent[] GIRLS_JENNY_HMPH = new SoundEvent[5];
   public static final SoundEvent[] GIRLS_JENNY_HUH = new SoundEvent[2];
   public static final SoundEvent[] GIRLS_JENNY_LIGHTBREATHING = new SoundEvent[12];
   public static final SoundEvent[] GIRLS_JENNY_LIPSOUND = new SoundEvent[10];
   public static final SoundEvent[] GIRLS_JENNY_MMM = new SoundEvent[9];
   public static final SoundEvent[] GIRLS_JENNY_MOAN = new SoundEvent[8];
   public static final SoundEvent[] GIRLS_JENNY_SADOH = new SoundEvent[2];
   public static final SoundEvent[] GIRLS_JENNY_SIGH = new SoundEvent[2];
   public static final SoundEvent[] MISC_PLOB = new SoundEvent[1];
   public static final SoundEvent[] MISC_BELLJINGLE = new SoundEvent[1];
   public static final SoundEvent[] MISC_BEDRUSTLE = new SoundEvent[2];
   public static final SoundEvent[] MISC_SLAP = new SoundEvent[2];
   public static final SoundEvent[] MISC_TOUCH = new SoundEvent[2];
   public static final SoundEvent[] MISC_POUNDING = new SoundEvent[10];
   public static final SoundEvent[] MISC_SMALLINSERTS = new SoundEvent[5];
   public static final SoundEvent[] MISC_CUMINFLATION = new SoundEvent[1];
   private static SoundEvent[][] soundCategorys;
   public static final SoundsHandler INSTANCE;
   static HashMap lastRandomSound;

   public static void registerSounds() {
      for(int soundCategoryIndex = 0; soundCategoryIndex < soundCategorys.length; ++soundCategoryIndex) {
         SoundEvent[] soundCategory = soundCategorys[soundCategoryIndex];

         for(int soundIndex = 0; soundIndex < soundCategory.length; ++soundIndex) {
            String path = INSTANCE.getClass().getDeclaredFields()[soundCategoryIndex].getName().toLowerCase().replace("_", ".");

            String categoryName;
            try {
               categoryName = path.split("\\.")[2];
            } catch (ArrayIndexOutOfBoundsException var6) {
               categoryName = path.split("\\.")[1];
            }

            soundCategory[soundIndex] = registerSound(path + "." + categoryName + soundIndex);
         }
      }

   }

   public static SoundEvent registerSound(String path) {
      ResourceLocation location = new ResourceLocation("sexmod", path);
      SoundEvent event = new SoundEvent(location);
      event.setRegistryName(path);
      ForgeRegistries.SOUND_EVENTS.register(event);
      return event;
   }

   public static SoundEvent Random(SoundEvent[] soundArray) {
      if (lastRandomSound.get(soundArray[0]) == null) {
         lastRandomSound.put(soundArray[0], -69);
      }

      int random;
      do {
         random = Reference.RANDOM.nextInt(soundArray.length);
      } while(random == (Integer)lastRandomSound.get(soundArray[0]));

      lastRandomSound.replace(soundArray[0], random);
      return soundArray[random];
   }

   static {
      soundCategorys = new SoundEvent[][]{GIRLS_JENNY_AFTERSESSIONMOAN, GIRLS_JENNY_AHH, GIRLS_JENNY_BJMOAN, GIRLS_JENNY_GIGGLE, GIRLS_JENNY_HAPPYOH, GIRLS_JENNY_HEAVYBREATHING, GIRLS_JENNY_HMPH, GIRLS_JENNY_HUH, GIRLS_JENNY_LIGHTBREATHING, GIRLS_JENNY_LIPSOUND, GIRLS_JENNY_MMM, GIRLS_JENNY_MOAN, GIRLS_JENNY_SADOH, GIRLS_JENNY_SIGH, MISC_PLOB, MISC_BELLJINGLE, MISC_BEDRUSTLE, MISC_SLAP, MISC_TOUCH, MISC_POUNDING, MISC_SMALLINSERTS, MISC_CUMINFLATION};
      INSTANCE = new SoundsHandler();
      lastRandomSound = new HashMap();
   }
}
