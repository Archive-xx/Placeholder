package com.schnurritv.sexmod.girls;

import com.google.common.collect.Sets;
import com.schnurritv.sexmod.Packages.ChangeAnimationParameter;
import com.schnurritv.sexmod.Packages.ChangeGirlMovement;
import com.schnurritv.sexmod.Packages.ChangeSkin;
import com.schnurritv.sexmod.Packages.ChangeTransitionTicks;
import com.schnurritv.sexmod.Packages.ResetGirl;
import com.schnurritv.sexmod.Packages.SendChatMessage;
import com.schnurritv.sexmod.Packages.TeleportPlayer;
import com.schnurritv.sexmod.events.HandlePlayerMovement;
import com.schnurritv.sexmod.util.Reference;
import com.schnurritv.sexmod.util.Handlers.LootTableHandler;
import com.schnurritv.sexmod.util.Handlers.PacketHandler;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.controller.AnimationController.ISoundListener;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class GirlEntity extends EntitySheep implements IAnimatable {
   private final AnimationFactory factory = new AnimationFactory(this);
   public EntityAIBase aiWander;
   public EntityAIBase aiLookAtPlayer;
   public static ArrayList girlEntities = new ArrayList();
   public int currentModel = 1;
   protected String girlName = "girl";
   public boolean shouldBeAtTarget = false;
   public boolean shouldBeAtTargetYaw = false;
   public boolean shouldGetDamage = true;
   public Vec3d targetPos;
   public float targetYaw;
   public boolean playerIsInPosition = false;
   public Vec3d playerCamPos;
   protected float playerYaw;
   public GameType playerGameMode;
   public boolean isForPreloading = false;
   public EntityPlayer playerSheHasSexWith;
   public double walkSpeed;
   public String animationFollowUp = "";
   public boolean playerIsThrusting = false;
   public boolean playerIsCumming = false;
   public int paymentItemsAmount = 0;
   public GirlEntity.PaymentItems paymentItem;
   private static final Set TEMPTATION_ITEMS;
   public AnimationController actionController;
   public AnimationController movementController;
   public AnimationController eyesController;
   public EnumMap animationParameters;
   int preloadTick;

   private EnumMap createAnimationParameters() {
      return new EnumMap(GirlEntity.AnimationParameters.class) {
         {
            this.put(GirlEntity.AnimationParameters.AUTOHEAD, true);
            this.put(GirlEntity.AnimationParameters.AUTOEYES, true);
            this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, true);
            this.put(GirlEntity.AnimationParameters.ISDOINGACTION, false);
            this.put(GirlEntity.AnimationParameters.STARTSTRIP, false);
            this.put(GirlEntity.AnimationParameters.STARTBLOWJOB, false);
            this.put(GirlEntity.AnimationParameters.SUCKBLOWJOB, false);
            this.put(GirlEntity.AnimationParameters.CUMBLOWJOB, false);
            this.put(GirlEntity.AnimationParameters.THRUSTBLOWJOB, false);
            this.put(GirlEntity.AnimationParameters.STARTPAYMENT, false);
            this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, false);
            this.put(GirlEntity.AnimationParameters.STARTDOGGY, false);
            this.put(GirlEntity.AnimationParameters.WAITDOGGY, false);
            this.put(GirlEntity.AnimationParameters.DOGGYSTART, false);
            this.put(GirlEntity.AnimationParameters.DOGGYSLOW, false);
            this.put(GirlEntity.AnimationParameters.DOGGYFAST, false);
            this.put(GirlEntity.AnimationParameters.DOGGYCUM, false);
            this.put(GirlEntity.AnimationParameters.DASH, false);
            this.put(GirlEntity.AnimationParameters.HUG, false);
         }
      };
   }

   protected boolean func_70692_ba() {
      return false;
   }

   protected GirlEntity(World worldIn) {
      super(worldIn);
      this.paymentItem = GirlEntity.PaymentItems.DIAMOND;
      this.actionController = new AnimationController(this, "action", 10.0F, this::predicate);
      this.movementController = new AnimationController(this, "movement", 5.0F, this::predicate);
      this.eyesController = new AnimationController(this, "eyes", 10.0F, this::predicate);
      this.animationParameters = this.createAnimationParameters();
      this.preloadTick = 0;
      girlEntities.add(this);
      this.walkSpeed = 0.35D;
   }

   protected GirlEntity(World worldIn, boolean isForPreloading) {
      super(worldIn);
      this.paymentItem = GirlEntity.PaymentItems.DIAMOND;
      this.actionController = new AnimationController(this, "action", 10.0F, this::predicate);
      this.movementController = new AnimationController(this, "movement", 5.0F, this::predicate);
      this.eyesController = new AnimationController(this, "eyes", 10.0F, this::predicate);
      this.animationParameters = this.createAnimationParameters();
      this.preloadTick = 0;
      this.isForPreloading = isForPreloading;
      this.walkSpeed = 0.35D;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(15.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
   }

   protected void func_184651_r() {
      Reference.server = this.func_184102_h();
      this.walkSpeed = 0.35D;
      this.aiWander = new EntityAIWanderAvoidWater(this, this.walkSpeed);
      this.aiLookAtPlayer = new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F);
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new EntityAITempt(this, 0.4D, false, TEMPTATION_ITEMS));
   }

   public void func_70619_bc() {
      if (this.isForPreloading) {
         ++this.preloadTick;
         if (this.preloadTick > 20) {
            this.field_70170_p.func_72900_e(this);
         }
      }

      if (this.shouldBeAtTarget) {
         this.func_180426_a(this.targetPos.field_72450_a, this.targetPos.field_72448_b, this.targetPos.field_72449_c, this.targetYaw, 0.0F, 1, true);
      } else if (this.shouldBeAtTargetYaw) {
         this.func_70101_b(this.targetYaw, this.field_70125_A);
      }

   }

   protected void ChangeTransitionTicks(int ticks) {
      if (this.actionController.transitionLengthTicks != (double)ticks) {
         PacketHandler.INSTANCE.sendToAll(new ChangeTransitionTicks(ticks, this.func_180425_c()));
      }

   }

   protected void TurnPlayerIntoCamera(EntityPlayerMP player, boolean autoMoveCamera) {
      this.playerGameMode = player.field_71134_c.func_73081_b();
      player.func_71033_a(GameType.SPECTATOR);
      Vec3d forward = player.func_189651_aD();
      if (autoMoveCamera) {
         player.func_70634_a(player.field_70165_t + forward.field_72450_a * 0.35D, player.field_70163_u, player.field_70161_v + forward.field_72449_c * 0.35D);
      }

      this.targetYaw = player.field_70759_as + 180.0F;
   }

   protected void TurnPlayerIntoCamera(EntityPlayerMP player) {
      this.playerGameMode = player.field_71134_c.func_73081_b();
      player.func_71033_a(GameType.SPECTATOR);
      Vec3d forward = player.func_189651_aD();
      player.func_70634_a(player.field_70165_t + forward.field_72450_a * 0.35D, player.field_70163_u, player.field_70161_v + forward.field_72449_c * 0.35D);
      this.targetYaw = player.field_70759_as + 180.0F;
   }

   protected void prepareAction() {
      this.field_70714_bg.func_85156_a(this.aiLookAtPlayer);
      this.field_70714_bg.func_85156_a(this.aiWander);
      this.func_70661_as().func_75499_g();
      this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
         {
            this.put(GirlEntity.AnimationParameters.AUTOHEAD, false);
            this.put(GirlEntity.AnimationParameters.AUTOEYES, false);
         }
      });
      this.TurnPlayerIntoCamera(((MinecraftServer)Objects.requireNonNull(this.func_184102_h())).func_184103_al().func_177451_a(this.playerSheHasSexWith.getPersistentID()));
      this.targetPos = this.getInFrontOfPlayer();
   }

   public static ArrayList getGirlsByPos(BlockPos pos) {
      ArrayList girlList = new ArrayList();
      Iterator var2 = girlEntities.iterator();

      while(var2.hasNext()) {
         GirlEntity sophi = (GirlEntity)var2.next();
         if (sophi.func_180425_c().func_177958_n() == pos.func_177958_n() && sophi.func_180425_c().func_177956_o() == pos.func_177956_o() && sophi.func_180425_c().func_177952_p() == pos.func_177952_p()) {
            girlList.add(sophi);
         }
      }

      int amountOfGirls;
      try {
         amountOfGirls = ((GirlEntity)girlList.get(0)).field_70170_p.field_73010_i.size() + 1;
      } catch (IndexOutOfBoundsException var6) {
         amountOfGirls = ((GirlEntity)girlEntities.get(0)).field_70170_p.field_73010_i.size();
      }

      if (girlList.size() != amountOfGirls) {
         girlList.clear();
         Iterator var8 = girlEntities.iterator();

         while(var8.hasNext()) {
            GirlEntity girl = (GirlEntity)var8.next();
            BlockPos distance = girl.func_180425_c().func_177973_b(pos);
            distance = new BlockPos(Math.abs(distance.func_177958_n()), Math.abs(distance.func_177956_o()), Math.abs(distance.func_177952_p()));
            if (distance.func_177958_n() + distance.func_177956_o() + distance.func_177952_p() <= 2) {
               girlList.add(girl);
            }
         }
      }

      return girlList;
   }

   protected BlockPos findBlock(BlockPos pos) {
      int step = 1;
      int dir = -1;
      BlockPos searchPos = pos;

      while(step < 22) {
         for(int move = 0; move < 2; ++move) {
            dir *= -1;

            int stepTaken;
            int y;
            for(stepTaken = 0; stepTaken < step; ++stepTaken) {
               searchPos = searchPos.func_177982_a(0, 0, dir);

               for(y = -3; y < 4; ++y) {
                  if (this.field_70170_p.func_180495_p(searchPos.func_177982_a(0, y, dir)).func_177230_c() == Blocks.field_150324_C) {
                     return searchPos.func_177982_a(0, y, dir);
                  }
               }
            }

            for(stepTaken = 0; stepTaken < step; ++stepTaken) {
               searchPos = searchPos.func_177982_a(dir, 0, 0);

               for(y = -3; y < 4; ++y) {
                  if (this.field_70170_p.func_180495_p(searchPos.func_177982_a(dir, y, 0)).func_177230_c() == Blocks.field_150324_C) {
                     return searchPos.func_177982_a(dir, y, 0);
                  }
               }
            }

            ++step;
         }
      }

      return null;
   }

   protected ResourceLocation func_184647_J() {
      return LootTableHandler.GIRL;
   }

   public void changeOutfit(BlockPos pos) {
      PacketHandler.INSTANCE.sendToAll(new ChangeSkin(this.currentModel ^ 1, pos));
   }

   public abstract void startAnimation(String var1);

   protected abstract PlayState predicate(AnimationEvent var1);

   protected void createAnimation(String path, boolean looped, AnimationEvent event) {
      event.getController().setAnimation((new AnimationBuilder()).addAnimation(path, looped));
   }

   protected void ChangeAnimationParameter(GirlEntity.AnimationParameters parameterName, boolean parameterValue) {
      if ((Boolean)this.animationParameters.get(parameterName) != parameterValue) {
         PacketHandler.INSTANCE.sendToAll(new ChangeAnimationParameter(this.func_180425_c(), parameterName, parameterValue));
         PacketHandler.INSTANCE.sendToServer(new ChangeAnimationParameter(this.func_180425_c(), parameterName, parameterValue));
      }

   }

   protected void ChangeAnimationParameters(EnumMap unfilteredParameters) {
      EnumMap filteredParameters = new EnumMap(GirlEntity.AnimationParameters.class);
      Iterator var3 = unfilteredParameters.entrySet().iterator();

      while(var3.hasNext()) {
         Entry parameter = (Entry)var3.next();
         if (this.animationParameters.get(parameter.getKey()) != parameter.getValue()) {
            filteredParameters.put((Enum)parameter.getKey(), parameter.getValue());
         }
      }

      if (filteredParameters.size() != 0) {
         PacketHandler.INSTANCE.sendToAll(new ChangeAnimationParameter(this.func_180425_c(), filteredParameters));
         PacketHandler.INSTANCE.sendToServer(new ChangeAnimationParameter(this.func_180425_c(), filteredParameters));
      }

   }

   public void registerControllers(AnimationData data) {
      ISoundListener movementSoundListener = new ISoundListener() {
         public void playSound(SoundKeyframeEvent event) {
            if ("idle".equals(event.sound)) {
               GirlEntity.this.ChangeTransitionTicks(10);
            }

         }
      };
      this.movementController.registerSoundListener(movementSoundListener);
      data.addAnimationController(this.movementController);
      data.addAnimationController(this.eyesController);
   }

   protected void resetPlayer() {
      this.playerCamPos = null;
      HandlePlayerMovement.active = true;
      PacketHandler.INSTANCE.sendToServer(new ResetGirl(this.func_180425_c(), true));
   }

   protected void resetGirl() {
      this.playerCamPos = null;
      this.playerIsThrusting = false;
      this.playerIsCumming = false;
      HandlePlayerMovement.active = true;
      this.func_189654_d(false);
      this.ChangeAnimationParameters(new EnumMap(GirlEntity.AnimationParameters.class) {
         {
            this.put(GirlEntity.AnimationParameters.ISDOINGACTION, false);
            this.put(GirlEntity.AnimationParameters.AUTOHEAD, true);
            this.put(GirlEntity.AnimationParameters.AUTOEYES, true);
            this.put(GirlEntity.AnimationParameters.MOVEMENTACTIVE, true);
            this.put(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED, false);
         }
      });
      PacketHandler.INSTANCE.sendToServer(new ChangeGirlMovement(0.5D, this.func_180425_c()));
      PacketHandler.INSTANCE.sendToServer(new ResetGirl(this.func_180425_c()));
   }

   @SideOnly(Side.CLIENT)
   public static void sendThrust(UUID playerUUID) {
      Iterator var1 = girlEntities.iterator();

      while(var1.hasNext()) {
         GirlEntity girl = (GirlEntity)var1.next();
         if (girl.playerSheHasSexWith != null && girl.playerSheHasSexWith.getPersistentID().equals(playerUUID) && !girl.playerIsThrusting) {
            girl.thrust();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public static void sendCum(UUID playerUUID) {
      Iterator var1 = girlEntities.iterator();

      while(var1.hasNext()) {
         GirlEntity girl = (GirlEntity)var1.next();
         if (girl.playerSheHasSexWith != null && girl.playerSheHasSexWith.getPersistentID().equals(playerUUID) && !girl.playerIsCumming) {
            girl.cum();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   protected abstract void thrust();

   @SideOnly(Side.CLIENT)
   protected abstract void cum();

   protected void moveCamera(double x, double y, double z, float yaw, float pitch) {
      if (this.playerCamPos == null) {
         this.playerCamPos = this.playerSheHasSexWith.func_174791_d();
      }

      Vec3d newPos = this.playerCamPos;
      newPos = newPos.func_72441_c(-Math.sin((double)(this.playerYaw + 90.0F) * 0.017453292519943295D) * x, 0.0D, Math.cos((double)(this.playerYaw + 90.0F) * 0.017453292519943295D) * x);
      newPos = newPos.func_72441_c(0.0D, y, 0.0D);
      newPos = newPos.func_72441_c(-Math.sin((double)this.playerYaw * 0.017453292519943295D) * z, 0.0D, Math.cos((double)this.playerYaw * 0.017453292519943295D) * z);
      if (this.field_70170_p.field_72995_K) {
         PacketHandler.INSTANCE.sendToServer(new TeleportPlayer(this.playerSheHasSexWith.getPersistentID().toString(), newPos, this.playerYaw + yaw, pitch));
      } else {
         this.playerSheHasSexWith.func_70080_a(newPos.field_72450_a, newPos.field_72448_b, newPos.field_72449_c, this.playerYaw + yaw, pitch);
         this.playerSheHasSexWith.func_70634_a(newPos.field_72450_a, newPos.field_72448_b, newPos.field_72449_c);
         this.playerSheHasSexWith.func_70016_h(0.0D, 0.0D, 0.0D);
      }

   }

   protected abstract void checkFollowUp();

   protected void say(String msg) {
      PacketHandler.INSTANCE.sendToAllAround(new SendChatMessage("<" + this.girlName + "> " + msg), new TargetPoint(this.field_71093_bK, (double)this.func_180425_c().func_177958_n(), (double)this.func_180425_c().func_177956_o(), (double)this.func_180425_c().func_177952_p(), 40.0D));
   }

   protected void say(String msg, boolean noPrefix) {
      if (noPrefix) {
         PacketHandler.INSTANCE.sendToAllAround(new SendChatMessage(msg), new TargetPoint(this.field_71093_bK, (double)this.func_180425_c().func_177958_n(), (double)this.func_180425_c().func_177956_o(), (double)this.func_180425_c().func_177952_p(), 40.0D));
      }

   }

   protected void playSoundAroundHer(SoundEvent sound) {
      if (this.field_70170_p.field_72995_K) {
         this.field_70170_p.func_184134_a((double)this.func_180425_c().func_177958_n(), (double)this.func_180425_c().func_177956_o(), (double)this.func_180425_c().func_177952_p(), sound, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
      }

      this.field_70170_p.func_184133_a((EntityPlayer)null, this.func_180425_c(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
   }

   protected void playSoundAroundHer(SoundEvent sound, float volume) {
      if (this.field_70170_p.field_72995_K) {
         this.field_70170_p.func_184134_a((double)this.func_180425_c().func_177958_n(), (double)this.func_180425_c().func_177956_o(), (double)this.func_180425_c().func_177952_p(), sound, SoundCategory.NEUTRAL, volume, 1.0F, false);
      } else {
         this.field_70170_p.func_184133_a((EntityPlayer)null, this.func_180425_c(), sound, SoundCategory.PLAYERS, volume, 1.0F);
      }
   }

   protected Vec3d getInFrontOfPlayer() {
      float playerYaw = this.playerSheHasSexWith.field_70177_z;
      return this.playerSheHasSexWith.func_174791_d().func_72441_c(-Math.sin((double)playerYaw * 0.017453292519943295D), 0.0D, Math.cos((double)playerYaw * 0.017453292519943295D));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   static {
      TEMPTATION_ITEMS = Sets.newHashSet(new Item[]{Items.field_151166_bC, Items.field_151045_i, Items.field_151043_k, Items.field_151079_bi, Items.field_151100_aR});
   }

   public static enum AnimationParameters {
      AUTOHEAD,
      AUTOEYES,
      MOVEMENTACTIVE,
      ISDOINGACTION,
      STARTSTRIP,
      STARTBLOWJOB,
      SUCKBLOWJOB,
      CUMBLOWJOB,
      THRUSTBLOWJOB,
      STARTPAYMENT,
      PLAYERSHOULDBERENDERED,
      DRAWITEMS,
      STARTDOGGY,
      WAITDOGGY,
      DOGGYSTART,
      DOGGYSLOW,
      DOGGYFAST,
      DOGGYCUM,
      DASH,
      HUG;
   }

   public static enum PaymentItems {
      DIAMOND,
      GOLD,
      EMERALD;
   }
}
