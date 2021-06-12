package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetAnimationFollowUp implements IMessage {
   boolean messageValid;
   String followUp;
   BlockPos entityPos;

   public SetAnimationFollowUp() {
      this.messageValid = false;
   }

   public SetAnimationFollowUp(String followUp, BlockPos entityPos) {
      this.followUp = followUp;
      this.entityPos = entityPos;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         int stringLength = buf.readInt();
         byte[] bytes = new byte[stringLength];

         for(int i = 0; i < stringLength; ++i) {
            bytes[i] = buf.readByte();
         }

         this.followUp = new String(bytes);
         this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
         this.messageValid = true;
         this.messageValid = true;
      } catch (IndexOutOfBoundsException var5) {
         this.messageValid = false;
         System.out.println("couldn't read bytes @SetPaymentFollowUp :(");
      }
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.followUp.getBytes().length);
      buf.writeBytes(this.followUp.getBytes());
      buf.writeInt(this.entityPos.func_177958_n());
      buf.writeInt(this.entityPos.func_177956_o());
      buf.writeInt(this.entityPos.func_177952_p());
      this.messageValid = true;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetAnimationFollowUp message, MessageContext ctx) {
         if (message.messageValid) {
            if (!GirlEntity.getGirlsByPos(message.entityPos).isEmpty()) {
               ArrayList girlList = GirlEntity.getGirlsByPos(message.entityPos);

               GirlEntity girl;
               for(Iterator var4 = girlList.iterator(); var4.hasNext(); girl.animationFollowUp = message.followUp) {
                  girl = (GirlEntity)var4.next();
               }
            }
         } else {
            System.out.println("received an invalid message @ChangeAnimationParameter :(");
         }

         return null;
      }
   }
}
