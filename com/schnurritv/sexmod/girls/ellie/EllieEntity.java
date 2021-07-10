package com.schnurritv.sexmod.girls.ellie;

import com.schnurritv.sexmod.Packages.ChangeGirlMovement;
import com.schnurritv.sexmod.Packages.ChangeSkin;
import com.schnurritv.sexmod.Packages.SendEllieToPlayer;
import com.schnurritv.sexmod.Packages.SetAnimationFollowUp;
import com.schnurritv.sexmod.Packages.SetPlayerMovement;
import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.util.Handlers.PacketHandler;
import com.schnurritv.sexmod.util.Handlers.SoundsHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController.ISoundListener;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class EllieEntity extends GirlEntity {
   HashMap playerHornyList = new HashMap();
   float hornyModifier = 0.5F;
   float hornyRange = 10.0F;
   boolean isBusy = false;
   float angle = 0.0F;

   protected EllieEntity(World worldIn) {
      super(worldIn);
      this.func_70105_a(1.0F, 1.95F);
      this.girlName = "Ellie";
   }

   protected EllieEntity(World worldIn, boolean isForPreloading) {
      super(worldIn, isForPreloading);
      this.func_70105_a(1.0F, 1.95F);
      this.girlName = "Ellie";
   }

   public void func_70619_bc() {
      super.func_70619_bc();
      EntityPlayer closestPlayer = this.field_70170_p.func_72890_a(this, (double)this.hornyRange);
      if (!this.isBusy && closestPlayer != null) {
         if (this.playerHornyList.containsKey(closestPlayer.getPersistentID())) {
            float newHornyPercentage = (Float)this.playerHornyList.get(closestPlayer.getPersistentID()) + 0.01F * this.hornyModifier;
            if (newHornyPercentage >= 1.0F) {
               this.isBusy = true;
               this.playerHornyList.replace(closestPlayer.getPersistentID(), 0.0F);
               this.approachPlayer(closestPlayer);
            } else {
               this.playerHornyList.replace(closestPlayer.getPersistentID(), newHornyPercentage);
               System.out.println(newHornyPercentage);
            }
         } else {
            this.playerHornyList.put(closestPlayer.getPersistentID(), 0.0F);
         }
      }

   }

   protected SoundEvent func_184601_bQ(DamageSource edamageSourceIn) {
      return null;
   }

   protected SoundEvent func_184639_G() {
      return null;
   }

   protected SoundEvent func_184615_bR() {
      return null;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
   }

   protected void prepareAction(EntityPlayerMP player) {
      this.field_70714_bg.func_85156_a(this.aiLookAtPlayer);
      this.field_70714_bg.func_85156_a(this.aiWander);
      this.func_70661_as().func_75499_g();
      this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
         {
            this.put(GirlEntity.AnimationParameters.AUTOHEAD, false);
            this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
         }
      });
      this.TurnPlayerIntoCamera(player);
   }

   public void startAnimation(String animationName) {
      if ("strip".equals(animationName)) {
         this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
            {
               this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
               this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
            }
         });
      }

   }

   protected void prepareStrip() {
      this.prepareAction();
      PacketHandler.INSTANCE.sendToAll(new SetAnimationFollowUp("strip", this.func_180425_c()));
   }

   boolean shouldCrouch() {
      return this.field_70170_p.func_180495_p(this.func_180425_c().func_177982_a(0, 2, 0)).func_177230_c() != Blocks.field_150350_a;
   }

   void approachPlayer(EntityPlayer player) {
      this.prepareAction((EntityPlayerMP)player);
      this.playerSheHasSexWith = player;
      this.shouldBeAtTargetYaw = true;
      Vec3d distance = player.func_174791_d().func_178788_d(this.func_174791_d());
      this.targetYaw = (float)(Math.atan(distance.field_72450_a) + Math.atan(distance.field_72449_c));
      PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), (EntityPlayerMP)player);
      this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
         {
            this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
            this.put(GirlEntity.AnimationParameters.DASH, true);
            this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
         }
      });
   }

   public float func_70047_e() {
      return this.shouldCrouch() ? 1.53F : 1.9F;
   }

   protected void checkFollowUp() {
      if (this.animationFollowUp.equals("strip")) {
         this.resetPlayer();
         this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
            {
               this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
               this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
               this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
            }
         });
      }

      this.animationFollowUp = "";
   }

   public Vec3d getBehindOfPlayer(EntityPlayer player) {
      float playerYaw = player.field_70177_z;
      float distance = 1.1F;
      return player.func_174791_d().func_72441_c(-Math.sin((double)playerYaw * 0.017453292519943295D) * (double)(-distance), 0.0D, Math.cos((double)playerYaw * 0.017453292519943295D) * (double)(-distance));
   }

   protected PlayState predicate(AnimationEvent event) {
      String var2 = event.getController().getName();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1422950858:
         if (var2.equals("action")) {
            var3 = 2;
         }
         break;
      case -103677777:
         if (var2.equals("movement")) {
            var3 = 1;
         }
         break;
      case 3128418:
         if (var2.equals("eyes")) {
            var3 = 0;
         }
      }

      switch(var3) {
      case 0:
         if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.AUTOEYES)) {
            this.createAnimation("animation.ellie.null", true, event);
         } else {
            this.createAnimation("animation.ellie.eyes", true, event);
         }
         break;
      case 1:
         if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.MOVEMENTACTIVE)) {
            this.createAnimation("animation.ellie.null", true, event);
         } else if (Math.abs(this.field_70169_q - this.field_70165_t) + Math.abs(this.field_70166_s - this.field_70161_v) > 0.0D) {
            this.createAnimation(this.shouldCrouch() ? "animation.ellie.crouchwalk" : "animation.ellie.walk", true, event);
            this.field_70177_z = this.field_70759_as;
         } else {
            this.createAnimation(this.shouldCrouch() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, event);
         }
         break;
      case 2:
         if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.ISDOINGACTION)) {
            this.createAnimation("animation.ellie.null", true, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTSTRIP)) {
            this.createAnimation("animation.ellie.strip", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTPAYMENT)) {
            this.createAnimation("animation.ellie.payment", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DASH)) {
            this.createAnimation("animation.ellie.dash", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.HUG)) {
            this.createAnimation("animation.ellie.hug", false, event);
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData data) {
      super.registerControllers(data);
      ISoundListener actionSoundListener = new ISoundListener() {
         public void playSound(SoundKeyframeEvent event) {
            String var2 = event.sound;
            byte var3 = -1;
            switch(var2.hashCode()) {
            case -1584219178:
               if (var2.equals("startStrip")) {
                  var3 = 1;
               }
               break;
            case -1540860248:
               if (var2.equals("paymentDone")) {
                  var3 = 8;
               }
               break;
            case -1540620298:
               if (var2.equals("paymentMSG1")) {
                  var3 = 4;
               }
               break;
            case -1540620297:
               if (var2.equals("paymentMSG2")) {
                  var3 = 5;
               }
               break;
            case -1540620296:
               if (var2.equals("paymentMSG3")) {
                  var3 = 6;
               }
               break;
            case -1540620295:
               if (var2.equals("paymentMSG4")) {
                  var3 = 7;
               }
               break;
            case -1062935247:
               if (var2.equals("dashReady")) {
                  var3 = 10;
               }
               break;
            case -558244113:
               if (var2.equals("becomeNude")) {
                  var3 = 0;
               }
               break;
            case -188461382:
               if (var2.equals("stripDone")) {
                  var3 = 2;
               }
               break;
            case -188221432:
               if (var2.equals("stripMSG1")) {
                  var3 = 3;
               }
               break;
            case 1766420020:
               if (var2.equals("dashDone")) {
                  var3 = 11;
               }
               break;
            case 1766659970:
               if (var2.equals("dashMSG1")) {
                  var3 = 9;
               }
            }

            switch(var3) {
            case 0:
               PacketHandler.INSTANCE.sendToServer(new ChangeSkin(0, EllieEntity.this.func_180425_c()));
               PacketHandler.INSTANCE.sendToAll(new ChangeSkin(0, EllieEntity.this.func_180425_c()));
               break;
            case 1:
               PacketHandler.INSTANCE.sendToServer(new ChangeGirlMovement(0.0D, EllieEntity.this.func_180425_c()));
               EllieEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
                     this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
                     this.put(GirlEntity.AnimationParameters.AUTOHEAD, false);
                     this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
                  }
               });
               break;
            case 2:
               EllieEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.STARTSTRIP, false);
               EllieEntity.this.resetGirl();
               EllieEntity.this.checkFollowUp();
               break;
            case 3:
               EllieEntity.this.say("Hihi~");
               EllieEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
               break;
            case 4:
               EllieEntity.this.say("Huh?");
               EllieEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HUH[1]);
               break;
            case 5:
               EllieEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.5F);

               String playerName;

               String var5 = EllieEntity.this.animationFollowUp;
               byte var11 = -1;
               switch(var5.hashCode()) {
               case -20842805:
                  if (var5.equals("blowjob")) {
                     var11 = 1;
                  }
                  break;
               case 95761198:
                  if (var5.equals("doggy")) {
                     var11 = 2;
                  }
                  break;
               case 109773592:
                  if (var5.equals("strip")) {
                     var11 = 0;
                  }
               }

               switch(var11) {
               case 0:
                  EllieEntity.this.say(playerName + "show Bobs and vegana pls", true);
                  return;
               case 1:
                  EllieEntity.this.say(playerName + "Give me the sucky sucky and these are yours", true);
                  return;
               case 2:
                  EllieEntity.this.say(playerName + "Give me the sex pls :)", true);
                  return;
               default:
                  EllieEntity.this.say(playerName + "sex pls", true);
                  return;
               }
            case 6:
               EllieEntity.this.say("Hehe~");
               EllieEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
               break;
            case 7:
               EllieEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.25F);
               break;
            case 8:
               EllieEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.STARTPAYMENT, false);
               EllieEntity.this.checkFollowUp();
               break;
            case 9:
               EllieEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
               break;
            case 10:
               PacketHandler.INSTANCE.sendToServer(new SendEllieToPlayer(EllieEntity.this.func_180425_c()));
               break;
            case 11:
               EllieEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.DASH, false);
                     this.put(GirlEntity.AnimationParameters.HUG, true);
                     this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, true);
                  }
               });
            }

         }
      };
      this.actionController.registerSoundListener(actionSoundListener);
      data.addAnimationController(this.actionController);
   }

   protected void thrust() {
   }

   protected void cum() {
   }
}
