package com.schnurritv.sexmod.girls.jenny;

import com.schnurritv.sexmod.Main;
import com.schnurritv.sexmod.Packages.ChangeGirlMovement;
import com.schnurritv.sexmod.Packages.ChangeSkin;
import com.schnurritv.sexmod.Packages.SendJennyToDoggy;
import com.schnurritv.sexmod.Packages.SendPaymentItems;
import com.schnurritv.sexmod.Packages.SetAnimationFollowUp;
import com.schnurritv.sexmod.Packages.SetPlayerForGirl;
import com.schnurritv.sexmod.Packages.SetPlayerMovement;
import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.gui.BlackScreenUI;
import com.schnurritv.sexmod.gui.MenuUI;
import com.schnurritv.sexmod.gui.SexUI;
import com.schnurritv.sexmod.util.Reference;
import com.schnurritv.sexmod.util.Handlers.PacketHandler;
import com.schnurritv.sexmod.util.Handlers.SoundsHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController.ISoundListener;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class JennyEntity extends GirlEntity {
   private boolean lookingForBed = false;
   private boolean isPreparingPayment = false;
   int bedSearchTick = 0;
   int preparingPaymentTick = 0;
   int flip = 0;

   public JennyEntity(World worldIn) {
      super(worldIn);
      this.func_70105_a(0.6F, 1.95F);
      this.girlName = "Jenny";
   }

   public JennyEntity(World worldIn, boolean isForPreLoading) {
      super(worldIn, isForPreLoading);
      this.func_70105_a(0.6F, 1.95F);
      this.girlName = "Jenny";
   }

   public float func_70047_e() {
      return 1.64F;
   }

   protected SoundEvent func_184639_G() {
      return null;
   }

   protected SoundEvent func_184615_bR() {
      int whichOne = Reference.RANDOM.nextInt(2);
      return whichOne != 0 ? SoundsHandler.GIRLS_JENNY_SIGH[0] : SoundsHandler.GIRLS_JENNY_SIGH[1];
   }

   protected SoundEvent func_184601_bQ(DamageSource source) {
      return null;
   }

   public void func_70619_bc() {
      super.func_70619_bc();
      if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.WAITDOGGY) && this.playerSheHasSexWith.func_174791_d().func_72438_d(this.func_174791_d()) < 0.5D) {
         EntityPlayerMP playerMP = this.func_184102_h().func_184103_al().func_177451_a(this.playerSheHasSexWith.getPersistentID());
         playerMP.func_70634_a(this.func_174791_d().field_72450_a, this.func_174791_d().field_72448_b, this.func_174791_d().field_72449_c);
         this.TurnPlayerIntoCamera(playerMP, false);
         playerMP.func_191958_b(0.0F, 0.0F, 0.0F, 0.0F);
         this.moveCamera(0.0D, 0.0D, 0.4D, 0.0F, 60.0F);
         this.playerCamPos = null;
         this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
            {
               this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, true);
               this.put(GirlEntity.AnimationParameters.WAITDOGGY, false);
               this.put(GirlEntity.AnimationParameters.DOGGYSTART, true);
               this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
            }
         });
         this.ChangeTransitionTicks(2);
         PacketHandler.INSTANCE.sendTo(new SetPlayerMovement(false), playerMP);
      }

      if (this.lookingForBed) {
         if (this.func_174791_d().func_72438_d(this.targetPos) >= 0.6D && this.bedSearchTick <= 200) {
            ++this.bedSearchTick;
            if (this.bedSearchTick == 60 || this.bedSearchTick == 120) {
               this.func_70661_as().func_75499_g();
               this.func_70661_as().func_75492_a(this.targetPos.field_72450_a, this.targetPos.field_72448_b, this.targetPos.field_72449_c, this.walkSpeed);
            }
         } else {
            this.lookingForBed = false;
            this.shouldBeAtTarget = true;
            this.bedSearchTick = 0;
            this.field_70145_X = true;
            this.func_189654_d(true);
            this.func_70016_h(0.0D, 0.0D, 0.0D);
            this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
               {
                  this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
                  this.put(GirlEntity.AnimationParameters.AUTOHEAD, false);
                  this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
                  this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
                  this.put(GirlEntity.AnimationParameters.STARTDOGGY, true);
               }
            });
         }
      }

      if (this.isPreparingPayment) {
         ++this.preparingPaymentTick;
         if (!this.func_174791_d().equals(this.targetPos) && this.preparingPaymentTick <= 40) {
            this.field_70177_z = this.targetYaw;

            try {
               this.targetPos.equals((Object)null);
            } catch (NullPointerException var2) {
               this.targetPos = this.getInFrontOfPlayer();
            }

            this.func_189654_d(false);
            Vec3d nextPos = Reference.Lerp(this.func_174791_d(), this.targetPos, (double)(40 - this.preparingPaymentTick));
            this.func_70107_b(nextPos.field_72450_a, nextPos.field_72448_b, nextPos.field_72449_c);
         } else {
            this.isPreparingPayment = false;
            this.shouldGetDamage = false;
            this.preparingPaymentTick = 0;
            this.targetYaw = this.playerSheHasSexWith.field_70177_z + 180.0F;
            this.shouldBeAtTarget = true;
            this.func_70661_as().func_75499_g();
            System.out.println("Ready For Payment Animation");
            this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
               {
                  this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
                  this.put(GirlEntity.AnimationParameters.STARTPAYMENT, true);
                  this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
                  this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, true);
                  this.put(GirlEntity.AnimationParameters.DRAWITEMS, true);
               }
            });
         }
      }

   }

   public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.func_184586_b(hand);
      boolean hasNameTag = itemstack.func_77973_b() == Items.field_151057_cb;
      if (hasNameTag) {
         itemstack.func_111282_a(player, this, hand);
         return true;
      } else {
         MenuUI.entityGirl = this;
         FMLNetworkHandler.openGui(player, Main.instance, 0, this.field_70170_p, player.func_180425_c().func_177958_n(), player.func_180425_c().func_177956_o(), player.func_180425_c().func_177952_p());
         return true;
      }
   }

   public void startAnimation(String animationName) {
      byte var3 = -1;
      switch(animationName.hashCode()) {
      case -20842805:
         if (animationName.equals("blowjob")) {
            var3 = 0;
         }
         break;
      case 95761198:
         if (animationName.equals("doggy")) {
            var3 = 1;
         }
         break;
      case 109773592:
         if (animationName.equals("strip")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         this.prepareBlowjob();
         break;
      case 1:
         this.prepareDoggy();
         break;
      case 2:
         this.prepareStrip();
      }

   }

   protected void prepareAction() {
      super.prepareAction();
      this.isPreparingPayment = true;
   }

   protected void prepareStrip() {
      this.prepareAction();
      PacketHandler.INSTANCE.sendToAll(new SetAnimationFollowUp("strip", this.func_180425_c()));
      PacketHandler.INSTANCE.sendToAll(new SendPaymentItems(GirlEntity.PaymentItems.GOLD.name(), 1, this.func_180425_c()));
   }

   private void prepareBlowjob() {
      this.prepareAction();
      PacketHandler.INSTANCE.sendToAll(new SetAnimationFollowUp("blowjob", this.func_180425_c()));
      PacketHandler.INSTANCE.sendToAll(new SendPaymentItems(GirlEntity.PaymentItems.EMERALD.name(), 3, this.func_180425_c()));
   }

   private void prepareDoggy() {
      this.prepareAction();
      PacketHandler.INSTANCE.sendToAll(new SetAnimationFollowUp("doggy", this.func_180425_c()));
      PacketHandler.INSTANCE.sendToAll(new SendPaymentItems(GirlEntity.PaymentItems.DIAMOND.name(), 2, this.func_180425_c()));
   }

   public void goForDoggy() {
      BlockPos temp = this.findBlock(this.func_180425_c());
      if (temp == null) {
         this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[2]);
         this.say("no bed in sight...");
      } else {
         this.field_70714_bg.func_85156_a(this.aiWander);
         this.field_70714_bg.func_85156_a(this.aiLookAtPlayer);
         Vec3d bedPos = new Vec3d((double)temp.func_177958_n(), (double)temp.func_177956_o(), (double)temp.func_177952_p());
         int[] yaws = new int[]{0, 180, -90, 90};
         Vec3d[][] potentialSpaces = new Vec3d[][]{{new Vec3d(0.5D, 0.0D, -0.5D), new Vec3d(0.0D, 0.0D, -1.0D)}, {new Vec3d(0.5D, 0.0D, 1.5D), new Vec3d(0.0D, 0.0D, 1.0D)}, {new Vec3d(-0.5D, 0.0D, 0.5D), new Vec3d(-1.0D, 0.0D, 0.0D)}, {new Vec3d(1.5D, 0.0D, 0.5D), new Vec3d(1.0D, 0.0D, 0.0D)}};
         int whichOne = -1;

         for(int i = 0; i < potentialSpaces.length; ++i) {
            Vec3d searchPos = bedPos.func_178787_e(potentialSpaces[i][1]);
            if (this.field_70170_p.func_180495_p(new BlockPos(searchPos.field_72450_a, searchPos.field_72448_b, searchPos.field_72449_c)).func_177230_c() == Blocks.field_150350_a) {
               if (whichOne == -1) {
                  whichOne = i;
               } else {
                  double oldDistance = this.func_180425_c().func_177954_c(bedPos.func_178787_e(potentialSpaces[whichOne][0]).field_72450_a, bedPos.func_178787_e(potentialSpaces[whichOne][0]).field_72448_b, bedPos.func_178787_e(potentialSpaces[whichOne][0]).field_72449_c);
                  double newDistance = this.func_180425_c().func_177954_c(bedPos.func_178787_e(potentialSpaces[i][0]).field_72450_a, bedPos.func_178787_e(potentialSpaces[i][0]).field_72448_b, bedPos.func_178787_e(potentialSpaces[i][0]).field_72449_c);
                  if (newDistance < oldDistance) {
                     whichOne = i;
                  }
               }
            }
         }

         if (whichOne == -1) {
            this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[2]);
            this.say("bed is obscured...");
            return;
         }

         Vec3d targetPos = bedPos.func_178787_e(potentialSpaces[whichOne][0]);
         this.targetYaw = (float)yaws[whichOne];
         this.targetPos = new Vec3d(targetPos.field_72450_a, targetPos.field_72448_b, targetPos.field_72449_c);
         this.playerYaw = this.targetYaw;
         this.func_70661_as().func_75499_g();
         this.func_70661_as().func_75492_a(targetPos.field_72450_a, targetPos.field_72448_b, targetPos.field_72449_c, this.walkSpeed);
         this.lookingForBed = true;
         this.bedSearchTick = 0;
      }

   }

   protected void cum() {
      if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.SUCKBLOWJOB) && !(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.THRUSTBLOWJOB)) {
         if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYSLOW) || (Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYFAST)) {
            this.playerIsCumming = true;
            this.ChangeTransitionTicks(2);
            this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
               {
                  this.put(GirlEntity.AnimationParameters.DOGGYSLOW, false);
                  this.put(GirlEntity.AnimationParameters.DOGGYFAST, false);
                  this.put(GirlEntity.AnimationParameters.DOGGYCUM, true);
               }
            });
         }
      } else {
         this.playerIsCumming = true;
         this.ChangeTransitionTicks(2);
         this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
            {
               this.put(GirlEntity.AnimationParameters.THRUSTBLOWJOB, false);
               this.put(GirlEntity.AnimationParameters.SUCKBLOWJOB, false);
               this.put(GirlEntity.AnimationParameters.CUMBLOWJOB, true);
            }
         });
         this.moveCamera(0.0D, 0.0D, 0.0D, 0.0F, 70.0F);
      }

   }

   protected void thrust() {
      if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.SUCKBLOWJOB) && !(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.THRUSTBLOWJOB)) {
         if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYSLOW) || (Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYFAST)) {
            this.playerIsThrusting = true;
            this.ChangeTransitionTicks(2);
            if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYFAST)) {
               this.actionController.clearAnimationCache();
            } else {
               this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.DOGGYFAST, true);
                     this.put(GirlEntity.AnimationParameters.DOGGYSLOW, false);
                  }
               });
            }
         }
      } else {
         this.playerIsThrusting = true;
         this.ChangeTransitionTicks(2);
         if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.THRUSTBLOWJOB)) {
            this.actionController.clearAnimationCache();
         } else {
            this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
               {
                  this.put(GirlEntity.AnimationParameters.THRUSTBLOWJOB, true);
                  this.put(GirlEntity.AnimationParameters.SUCKBLOWJOB, false);
               }
            });
         }
      }

   }

   protected void checkFollowUp() {
      String var1 = this.animationFollowUp;
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -20842805:
         if (var1.equals("blowjob")) {
            var2 = 1;
         }
         break;
      case 95761198:
         if (var1.equals("doggy")) {
            var2 = 2;
         }
         break;
      case 109773592:
         if (var1.equals("strip")) {
            var2 = 0;
         }
      }

      switch(var2) {
      case 0:
         this.resetPlayer();
         this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
            {
               this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
               this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
               this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
               this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, false);
            }
         });
         break;
      case 1:
         this.ChangeAnimationParameter(GirlEntity.AnimationParameters.STARTBLOWJOB, true);
         break;
      case 2:
         if (this.currentModel != 0) {
            this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
               {
                  this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
                  this.put(GirlEntity.AnimationParameters.ISDOINGACTION, true);
                  this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, false);
               }
            });
            this.resetPlayer();
            return;
         }

         this.resetGirl();
         PacketHandler.INSTANCE.sendToServer(new SendJennyToDoggy(this.func_180425_c()));
      }

      this.animationFollowUp = "";
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
            this.createAnimation("animation.jenny.null", true, event);
         } else {
            this.createAnimation("animation.jenny.fhappy", true, event);
         }
         break;
      case 1:
         if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.MOVEMENTACTIVE)) {
            this.createAnimation("animation.jenny.null", true, event);
         } else if (Math.abs(this.field_70169_q - this.field_70165_t) + Math.abs(this.field_70166_s - this.field_70161_v) > 0.0D) {
            this.createAnimation("animation.jenny.walk", true, event);
            this.field_70177_z = this.field_70759_as;
         } else {
            this.createAnimation("animation.jenny.idle", true, event);
         }
         break;
      case 2:
         if (!(Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.ISDOINGACTION)) {
            this.createAnimation("animation.jenny.null", true, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTSTRIP)) {
            this.createAnimation("animation.jenny.strip", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTPAYMENT)) {
            this.createAnimation("animation.jenny.payment", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTBLOWJOB)) {
            this.createAnimation("animation.jenny.blowjobintro", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.SUCKBLOWJOB)) {
            this.createAnimation("animation.jenny.blowjobsuck", true, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.THRUSTBLOWJOB)) {
            this.createAnimation("animation.jenny.blowjobthrust", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.CUMBLOWJOB)) {
            this.createAnimation("animation.jenny.blowjobcum", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.STARTDOGGY)) {
            this.createAnimation("animation.jenny.doggygoonbed", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.WAITDOGGY)) {
            this.createAnimation("animation.jenny.doggywait", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYSTART)) {
            this.createAnimation("animation.jenny.doggystart", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYSLOW)) {
            this.createAnimation("animation.jenny.doggyslow", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYFAST)) {
            this.createAnimation("animation.jenny.doggyfast", false, event);
         } else if ((Boolean)this.animationParameters.get(GirlEntity.AnimationParameters.DOGGYCUM)) {
            this.createAnimation("animation.jenny.doggycum", false, event);
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
            case -2038339681:
               if (var2.equals("doggyslowMSG1")) {
                  var3 = 46;
               }
               break;
            case -2038339680:
               if (var2.equals("doggyslowMSG2")) {
                  var3 = 47;
               }
               break;
            case -1649091690:
               if (var2.equals("doggystartDone")) {
                  var3 = 45;
               }
               break;
            case -1648851740:
               if (var2.equals("doggystartMSG1")) {
                  var3 = 40;
               }
               break;
            case -1648851739:
               if (var2.equals("doggystartMSG2")) {
                  var3 = 41;
               }
               break;
            case -1648851738:
               if (var2.equals("doggystartMSG3")) {
                  var3 = 42;
               }
               break;
            case -1648851737:
               if (var2.equals("doggystartMSG4")) {
                  var3 = 43;
               }
               break;
            case -1648851736:
               if (var2.equals("doggystartMSG5")) {
                  var3 = 44;
               }
               break;
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
            case -1370194640:
               if (var2.equals("bjcBlackScreen")) {
                  var3 = 33;
               }
               break;
            case -572151107:
               if (var2.equals("doggycumMSG1")) {
                  var3 = 50;
               }
               break;
            case -572151106:
               if (var2.equals("doggycumMSG2")) {
                  var3 = 51;
               }
               break;
            case -572151105:
               if (var2.equals("doggycumMSG3")) {
                  var3 = 52;
               }
               break;
            case -572151104:
               if (var2.equals("doggycumMSG4")) {
                  var3 = 53;
               }
               break;
            case -572151103:
               if (var2.equals("doggycumMSG5")) {
                  var3 = 54;
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
            case -90697923:
               if (var2.equals("bjcDone")) {
                  var3 = 34;
               }
               break;
            case -90457973:
               if (var2.equals("bjcMSG1")) {
                  var3 = 26;
               }
               break;
            case -90457972:
               if (var2.equals("bjcMSG2")) {
                  var3 = 27;
               }
               break;
            case -90457971:
               if (var2.equals("bjcMSG3")) {
                  var3 = 28;
               }
               break;
            case -90457970:
               if (var2.equals("bjcMSG4")) {
                  var3 = 29;
               }
               break;
            case -90457969:
               if (var2.equals("bjcMSG5")) {
                  var3 = 30;
               }
               break;
            case -90457968:
               if (var2.equals("bjcMSG6")) {
                  var3 = 31;
               }
               break;
            case -90457967:
               if (var2.equals("bjcMSG7")) {
                  var3 = 32;
               }
               break;
            case -85156797:
               if (var2.equals("bjiDone")) {
                  var3 = 22;
               }
               break;
            case -84916847:
               if (var2.equals("bjiMSG1")) {
                  var3 = 9;
               }
               break;
            case -84916846:
               if (var2.equals("bjiMSG2")) {
                  var3 = 10;
               }
               break;
            case -84916845:
               if (var2.equals("bjiMSG3")) {
                  var3 = 11;
               }
               break;
            case -84916844:
               if (var2.equals("bjiMSG4")) {
                  var3 = 12;
               }
               break;
            case -84916843:
               if (var2.equals("bjiMSG5")) {
                  var3 = 13;
               }
               break;
            case -84916842:
               if (var2.equals("bjiMSG6")) {
                  var3 = 14;
               }
               break;
            case -84916841:
               if (var2.equals("bjiMSG7")) {
                  var3 = 15;
               }
               break;
            case -84916840:
               if (var2.equals("bjiMSG8")) {
                  var3 = 16;
               }
               break;
            case -84916839:
               if (var2.equals("bjiMSG9")) {
                  var3 = 17;
               }
               break;
            case -74998066:
               if (var2.equals("bjtDone")) {
                  var3 = 23;
               }
               break;
            case -74758116:
               if (var2.equals("bjtMSG1")) {
                  var3 = 21;
               }
               break;
            case 13829932:
               if (var2.equals("doggyfastDone")) {
                  var3 = 49;
               }
               break;
            case 14069882:
               if (var2.equals("doggyfastMSG1")) {
                  var3 = 48;
               }
               break;
            case 441346873:
               if (var2.equals("doggyfastReady")) {
                  var3 = 25;
               }
               break;
            case 1092262223:
               if (var2.equals("doggyCumDone")) {
                  var3 = 55;
               }
               break;
            case 1662545087:
               if (var2.equals("bjiMSG10")) {
                  var3 = 18;
               }
               break;
            case 1662545088:
               if (var2.equals("bjiMSG11")) {
                  var3 = 19;
               }
               break;
            case 1662545089:
               if (var2.equals("bjiMSG12")) {
                  var3 = 20;
               }
               break;
            case 1982646231:
               if (var2.equals("bjtReady")) {
                  var3 = 24;
               }
               break;
            case 2105823406:
               if (var2.equals("doggyGoOnBedDone")) {
                  var3 = 39;
               }
               break;
            case 2106063356:
               if (var2.equals("doggyGoOnBedMSG1")) {
                  var3 = 35;
               }
               break;
            case 2106063357:
               if (var2.equals("doggyGoOnBedMSG2")) {
                  var3 = 36;
               }
               break;
            case 2106063358:
               if (var2.equals("doggyGoOnBedMSG3")) {
                  var3 = 37;
               }
               break;
            case 2106063359:
               if (var2.equals("doggyGoOnBedMSG4")) {
                  var3 = 38;
               }
            }

            switch(var3) {
            case 0:
               PacketHandler.INSTANCE.sendToServer(new ChangeSkin(0, JennyEntity.this.func_180425_c()));
               PacketHandler.INSTANCE.sendToAll(new ChangeSkin(0, JennyEntity.this.func_180425_c()));
               break;
            case 1:
               PacketHandler.INSTANCE.sendToServer(new ChangeGirlMovement(0.0D, JennyEntity.this.func_180425_c()));
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
                     this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, false);
                     this.put(GirlEntity.AnimationParameters.AUTOHEAD, false);
                     this.put(GirlEntity.AnimationParameters.STARTSTRIP, true);
                  }
               });
               break;
            case 2:
               JennyEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.STARTSTRIP, false);
               JennyEntity.this.resetGirl();
               JennyEntity.this.checkFollowUp();
               break;
            case 3:
               JennyEntity.this.say("Hihi~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
               break;
            case 4:
               JennyEntity.this.say("Huh?");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HUH[1]);
               break;
            case 5:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.5F);

               String playerName;

               String var12 = JennyEntity.this.animationFollowUp;
               byte var14 = -1;
               switch(var12.hashCode()) {
               case -20842805:
                  if (var12.equals("blowjob")) {
                     var14 = 1;
                  }
                  break;
               case 95761198:
                  if (var12.equals("doggy")) {
                     var14 = 2;
                  }
                  break;
               case 109773592:
                  if (var12.equals("strip")) {
                     var14 = 0;
                  }
               }

               switch(var14) {
               case 0:
                  JennyEntity.this.say(playerName + "show Bobs and vegana pls", true);
                  return;
               case 1:
                  JennyEntity.this.say(playerName + "Give me the sucky sucky and these are yours", true);
                  return;
               case 2:
                  JennyEntity.this.say(playerName + "Give me the sex pls :)", true);
                  return;
               default:
                  JennyEntity.this.say(playerName + "sex pls", true);
                  return;
               }
            case 6:
               JennyEntity.this.say("Hehe~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
               break;
            case 7:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.25F);
               break;
            case 8:
               JennyEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.STARTPAYMENT, false);
               JennyEntity.this.checkFollowUp();
               break;
            case 9:
               JennyEntity.this.say("What are you...");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_MMM[8]);
               JennyEntity.this.playerYaw = JennyEntity.this.field_70177_z + 180.0F;
               break;
            case 10:
               JennyEntity.this.say("eh... boys...");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
               break;
            case 11:
               JennyEntity.this.say("OHOhh...!");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
               break;
            case 12:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_BELLJINGLE[0]);
               break;
            case 13:
               JennyEntity.this.say("Was this really necessary?!");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[1], 0.5F);
               break;
            case 14:
               JennyEntity.this.say("Oh~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
               break;
            case 15:
               JennyEntity.this.say("You like it?~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[4]);
               break;
            case 16:
               try {
                  JennyEntity.this.say("<" + JennyEntity.this.playerSheHasSexWith.func_70005_c_() + "> Yee", true);
               } catch (NullPointerException var9) {
                  JennyEntity.this.say("<Gamer> Give me the sweet release of death", true);
               }

               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.5F);
               break;
            case 17:
               JennyEntity.this.say("Hihihi~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[2]);
               break;
            case 18:
               JennyEntity.this.moveCamera(-0.4D, -0.8D, -0.2D, 60.0F, -3.0F);
               JennyEntity.this.ChangeTransitionTicks(0);
               break;
            case 19:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
               SexUI.addCumPercentage(0.02D);
               break;
            case 20:
               if (Reference.RANDOM.nextInt(5) == 0) {
                  JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_BJMOAN));
               }

               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
               SexUI.addCumPercentage(0.02D);
               break;
            case 21:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MMM));
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
               SexUI.addCumPercentage(0.04D);
               break;
            case 22:
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.STARTBLOWJOB, false);
                     this.put(GirlEntity.AnimationParameters.SUCKBLOWJOB, true);
                  }
               });
               SexUI.shouldBeRendered = true;
               break;
            case 23:
               JennyEntity.this.ChangeTransitionTicks(0);
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.SUCKBLOWJOB, true);
                     this.put(GirlEntity.AnimationParameters.THRUSTBLOWJOB, false);
                  }
               });
               break;
            case 24:
            case 25:
               JennyEntity.this.playerIsThrusting = false;
               break;
            case 26:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_BJMOAN[1]);
               break;
            case 27:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_BJMOAN[7]);
               SexUI.shouldBeRendered = false;
               break;
            case 28:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[1]);
               break;
            case 29:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[0]);
               break;
            case 30:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[1]);
               break;
            case 31:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[2]);
               break;
            case 32:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[3]);
               break;
            case 33:
               BlackScreenUI.activate();
               break;
            case 34:
               JennyEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.CUMBLOWJOB, false);
               SexUI.resetCumPercentage();
               JennyEntity.this.ChangeTransitionTicks(0);
               JennyEntity.this.resetGirl();
               break;
            case 35:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_BEDRUSTLE[0]);
               JennyEntity.this.playerYaw = JennyEntity.this.field_70177_z;
               break;
            case 36:
               JennyEntity.this.say("what are you waiting for?~");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
               break;
            case 37:
               JennyEntity.this.say("this ass ain't gonna fuck itself...");
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
               break;
            case 38:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_SLAP[0], 0.75F);
               break;
            case 39:
               PacketHandler.INSTANCE.sendToServer(new SetPlayerForGirl(JennyEntity.this.func_180425_c(), Minecraft.func_71410_x().field_71439_g.getPersistentID()));
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.WAITDOGGY, true);
                     this.put(GirlEntity.AnimationParameters.STARTDOGGY, false);
                     this.put(GirlEntity.AnimationParameters.AUTOEYES, true);
                  }
               });
               break;
            case 40:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_TOUCH[0]);
               break;
            case 41:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_TOUCH[1]);
               break;
            case 42:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_BEDRUSTLE[1], 0.5F);
               break;
            case 43:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_SMALLINSERTS));
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_MMM[1]);
               break;
            case 44:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.33F);
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
               break;
            case 45:
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.DOGGYSTART, false);
                     this.put(GirlEntity.AnimationParameters.DOGGYSLOW, true);
                  }
               });
               SexUI.shouldBeRendered = true;
               break;
            case 46:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.33F);
               int rand = Reference.RANDOM.nextInt(4);
               if (rand == 0) {
                  rand = Reference.RANDOM.nextInt(2);
                  if (rand == 0) {
                     JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MMM));
                  } else {
                     JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                  }
               } else {
                  JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
               }

               SexUI.addCumPercentage(0.00666D);
               break;
            case 47:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5F);
               break;
            case 48:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.75F);
               SexUI.addCumPercentage(0.02D);
               ++JennyEntity.this.flip;
               if (JennyEntity.this.flip % 2 == 0) {
                  int random = Reference.RANDOM.nextInt(2);
                  if (random == 0) {
                     JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                  } else {
                     JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                  }
               } else {
                  JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_AHH));
               }
               break;
            case 49:
               JennyEntity.this.ChangeTransitionTicks(0);
               JennyEntity.this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
                  {
                     this.put(GirlEntity.AnimationParameters.DOGGYSLOW, true);
                     this.put(GirlEntity.AnimationParameters.DOGGYFAST, false);
                  }
               });
               break;
            case 50:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.MISC_CUMINFLATION[0], 2.0F);
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 2.0F);
               JennyEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
               break;
            case 51:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[4]);
               break;
            case 52:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[5]);
               break;
            case 53:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[6]);
               break;
            case 54:
               JennyEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[7]);
               JennyEntity.this.ChangeTransitionTicks(1);
               break;
            case 55:
               JennyEntity.this.ChangeAnimationParameter(GirlEntity.AnimationParameters.DOGGYCUM, false);
               SexUI.resetCumPercentage();
               JennyEntity.this.resetGirl();
            }

         }
      };
      this.actionController.registerSoundListener(actionSoundListener);
      data.addAnimationController(this.actionController);
   }
}
