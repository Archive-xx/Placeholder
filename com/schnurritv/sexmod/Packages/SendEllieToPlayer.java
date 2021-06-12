package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import com.schnurritv.sexmod.girls.ellie.EllieEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendEllieToPlayer implements IMessage {
   boolean messageValid;
   BlockPos girlPos;

   public SendEllieToPlayer() {
      this.messageValid = false;
   }

   public SendEllieToPlayer(BlockPos girlPos) {
      this.girlPos = girlPos;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.girlPos.func_177958_n());
      buf.writeInt(this.girlPos.func_177956_o());
      buf.writeInt(this.girlPos.func_177952_p());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SendEllieToPlayer message, MessageContext ctx) {
         if (message.messageValid) {
            ArrayList girlList = GirlEntity.getGirlsByPos(message.girlPos);
            Iterator var4 = girlList.iterator();

            while(var4.hasNext()) {
               GirlEntity girl = (GirlEntity)var4.next();
               if (!girl.field_70170_p.field_72995_K && girl instanceof EllieEntity) {
                  girl.targetPos = ((EllieEntity)girl).getBehindOfPlayer(girl.playerSheHasSexWith);
                  girl.targetYaw = girl.playerSheHasSexWith.field_70177_z;
                  girl.shouldBeAtTarget = true;
               }
            }
         } else {
            System.out.println("received an invalid message @SendEllieToPlayer :(");
         }

         return null;
      }
   }
}
