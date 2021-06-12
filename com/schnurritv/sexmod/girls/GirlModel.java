package com.schnurritv.sexmod.girls;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public abstract class GirlModel extends AnimatedGeoModel {
   boolean madeSkin = false;
   public ResourceLocation skin;
   static final int XOFFSET = 192;

   public abstract ResourceLocation getModel(int var1);

   public abstract ResourceLocation getSkin();

   public abstract ResourceLocation getAnimationFile();

   public abstract String getSkinName();

   public ResourceLocation getAnimationFileLocation(GirlEntity animatable) {
      return this.getAnimationFile();
   }

   public ResourceLocation getModelLocation(GirlEntity girl) {
      return this.getModel(girl.currentModel);
   }

   public ResourceLocation getTextureLocation(GirlEntity girl) {
      if (!this.madeSkin) {
         BufferedImage playerSkin = null;
         BufferedImage defaultSkin = null;
         EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
         boolean foundPlayerSkin = false;

         try {
            defaultSkin = ImageIO.read(Minecraft.func_71410_x().func_110442_L().func_110536_a(this.getSkin()).func_110527_b());
         } catch (IOException var17) {
            System.out.println("Couldn't load the default skin... which is kinda odd... Did you change Stuff inside the .jar file?");
            var17.printStackTrace();
         }

         File manualPlayerSkin = new File("sexmod/skin.png");

         try {
            playerSkin = ImageIO.read(manualPlayerSkin);
            foundPlayerSkin = true;
         } catch (IOException var16) {
            System.out.println("couldn't read a skin in sexmod/skin.png, which is why i am constructing a new one");
         }

         if (!foundPlayerSkin) {
            try {
               URL skinUrl = new URL("https://crafatar.com/skins/" + player.getPersistentID());
               playerSkin = ImageIO.read(skinUrl);
               foundPlayerSkin = true;
            } catch (IOException var15) {
               player.func_145747_a(new TextComponentString("§dI'm s-sorry but... crafatar.com cannot grab your Skin §5UwU §d.. This could either be because your playing on a pirated version of Minecraft or crafatar.com is currently on stupido mode. If you want to fix that yourself: put your Minecraft Skin into %appdata%/.minecraft/sexmod as skin.png and restart Minecraft"));
            }
         }

         if (!foundPlayerSkin) {
            String skinType = player.func_175154_l().equals("slim") ? "alex" : "steve";

            try {
               playerSkin = ImageIO.read(Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("minecraft", "textures/entity/" + skinType + ".png")).func_110527_b());
            } catch (IOException var14) {
               System.out.println("couldn't load default Steve/alex... tbh have no fucking clue how this is even possible... maybe your mc version is fucked up?");
               var14.printStackTrace();
            }
         }

         Rectangle skinTexture = playerSkin.getData().getBounds();

         for(int x = 0; x < skinTexture.width; ++x) {
            for(int y = 0; y < skinTexture.height; ++y) {
               for(int extraX = 0; extraX < 3; ++extraX) {
                  for(int extraY = 0; extraY < 3; ++extraY) {
                     defaultSkin.setRGB(192 + x * 3 + extraX, y * 3 + extraY, playerSkin.getRGB(x, y));
                  }
               }
            }
         }

         File dir = new File("sexmod");
         dir.mkdir();
         File skin = new File("sexmod/" + this.getSkinName() + ".png");

         try {
            ImageIO.write(defaultSkin, "png", skin);
         } catch (IOException var13) {
            System.out.println("couldn't save file");
            var13.printStackTrace();
         }

         try {
            this.skin = Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110578_a(skin.getName(), new DynamicTexture(ImageIO.read(skin)));
         } catch (IOException var12) {
            System.out.println("couldn't load skin");
            var12.printStackTrace();
         }

         this.madeSkin = true;
      }

      return this.skin;
   }

   public void setLivingAnimations(GirlEntity girl, Integer uniqueID, AnimationEvent customPredicate) {
      super.setLivingAnimations(girl, uniqueID, customPredicate);
      if (girl.shouldBeAtTarget) {
         girl.func_180426_a(girl.targetPos.field_72450_a, girl.targetPos.field_72448_b, girl.targetPos.field_72449_c, girl.targetYaw, 0.0F, 1, true);
      }

      AnimationProcessor processor = this.getAnimationProcessor();

      try {
         processor.getBone("steve").setHidden(!(Boolean)girl.animationParameters.get(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED));
      } catch (NullPointerException var11) {
      }

      if ((Boolean)girl.animationParameters.get(GirlEntity.AnimationParameters.AUTOHEAD)) {
         EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
         IBone neck = processor.getBone("neck");
         neck.setRotationY(extraData.netHeadYaw * 0.5F * 0.017453292F);
         IBone head = processor.getBone("head");
         head.setRotationY(extraData.netHeadYaw * 0.017453292F);
         head.setRotationX(extraData.headPitch * 0.017453292F);
         IBone body = processor.getBone("body");
         body.setRotationY(0.0F);
      }

      try {
         processor.getBone("items").getName();
      } catch (NullPointerException var10) {
         return;
      }

      if (girl.paymentItemsAmount == 0) {
         GirlEntity.PaymentItems[] var12 = GirlEntity.PaymentItems.values();
         int var14 = var12.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            GirlEntity.PaymentItems item = var12[var15];

            for(int i = 1; i <= 3; ++i) {
               processor.getBone(item.name().toLowerCase() + i).setHidden(true);
            }
         }
      } else {
         for(int i = 1; i <= girl.paymentItemsAmount; ++i) {
            processor.getBone(girl.paymentItem.name().toLowerCase() + i).setHidden(false);
         }
      }

   }
}
