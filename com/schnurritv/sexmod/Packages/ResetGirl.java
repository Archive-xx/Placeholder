package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.util.Reference;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ResetGirl implements IMessage {
   boolean messageValid;
   BlockPos girlPos;
   boolean onlyDoPlayerPart;

   public ResetGirl() {
      this.messageValid = false;
   }

   public ResetGirl(BlockPos girlPos) {
      this.girlPos = girlPos;
      this.onlyDoPlayerPart = false;
      this.messageValid = true;
   }

   public ResetGirl(BlockPos girlPos, boolean onlyDoPlayerPart) {
      this.girlPos = girlPos;
      this.onlyDoPlayerPart = onlyDoPlayerPart;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.onlyDoPlayerPart = buf.readBoolean();
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.girlPos.func_177958_n());
      buf.writeInt(this.girlPos.func_177956_o());
      buf.writeInt(this.girlPos.func_177952_p());
      buf.writeBoolean(this.onlyDoPlayerPart);
      this.messageValid = true;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ResetGirl message, MessageContext ctx) {
         if (message.messageValid && ctx.side == Side.SERVER) {
            ArrayList girls = GirlEntity.getGirlsByPos(message.girlPos);
            Iterator var4 = girls.iterator();

            while(var4.hasNext()) {
               GirlEntity girl = (GirlEntity)var4.next();
               if (!girl.field_70170_p.field_72995_K) {
                  try {
                     EntityPlayerMP player = Reference.server.func_184103_al().func_177451_a(girl.playerSheHasSexWith.getPersistentID());
                     if (girl.playerGameMode != null) {
                        player.func_71033_a(girl.playerGameMode);
                     } else {
                        player.func_71033_a(GameType.SURVIVAL);
                        System.out.println("couldn't find the players Gamemode, so i set it to SURVIVAL");
                     }
                  } catch (NullPointerException var7) {
                  }

                  if (!message.onlyDoPlayerPart) {
                     girl.field_70714_bg.func_75776_a(3, girl.aiWander);
                     girl.field_70714_bg.func_75776_a(3, girl.aiLookAtPlayer);
                     girl.shouldBeAtTarget = false;
                     girl.playerIsInPosition = false;
                     girl.playerSheHasSexWith = null;
                     girl.playerIsCumming = false;
                     girl.playerIsThrusting = false;
                     girl.playerCamPos = null;
                     girl.func_189654_d(false);
                     girl.field_70145_X = false;
                     girl.func_70634_a(girl.func_174791_d().field_72450_a, (double)girl.func_180425_c().func_177956_o(), girl.func_174791_d().field_72449_c);
                  }
               }
            }
         } else {
            System.out.println("recieved an unvalid message @SendChatMessage :(");
         }

         return null;
      }
   }
}
