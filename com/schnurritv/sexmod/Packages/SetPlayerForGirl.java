package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.util.Reference;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerForGirl implements IMessage {
   boolean messageValid;
   BlockPos girlPos;
   UUID playerUUID;

   public SetPlayerForGirl() {
      this.messageValid = false;
   }

   public SetPlayerForGirl(BlockPos girlPos, UUID playerUUID) {
      this.girlPos = girlPos;
      this.playerUUID = playerUUID;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.girlPos.func_177958_n());
      buf.writeInt(this.girlPos.func_177956_o());
      buf.writeInt(this.girlPos.func_177952_p());
      ByteBufUtils.writeUTF8String(buf, this.playerUUID.toString());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetPlayerForGirl message, MessageContext ctx) {
         if (message.messageValid && ctx.side == Side.SERVER) {
            ArrayList girlList = GirlEntity.getGirlsByPos(message.girlPos);
            Iterator var4 = girlList.iterator();

            while(true) {
               GirlEntity girl;
               while(true) {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  girl = (GirlEntity)var4.next();
                  PlayerList playerList = Reference.server.func_184103_al();

                  try {
                     playerList.func_177451_a(message.playerUUID).func_70005_c_();
                     break;
                  } catch (NullPointerException var10) {
                     System.out.println("couldn't find player with UUID: " + message.playerUUID);
                     System.out.println("could only find players with thsese UUID's:");
                     Iterator var8 = playerList.func_181057_v().iterator();

                     while(var8.hasNext()) {
                        EntityPlayerMP player = (EntityPlayerMP)var8.next();
                        System.out.println(player.func_70005_c_() + " " + player.func_110124_au());
                     }
                  }
               }

               girl.playerSheHasSexWith = Reference.server.func_184103_al().func_177451_a(message.playerUUID);
            }
         } else {
            System.out.println("received an invalid message @SetPlayerForGirl :(");
            return null;
         }
      }
   }
}
