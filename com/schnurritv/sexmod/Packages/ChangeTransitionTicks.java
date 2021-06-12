package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeTransitionTicks implements IMessage {
   boolean messageValid;
   int newValue;
   BlockPos girlPos;

   public ChangeTransitionTicks() {
      this.messageValid = false;
   }

   public ChangeTransitionTicks(int newValue, BlockPos girlPos) {
      this.newValue = newValue;
      this.girlPos = girlPos;
      this.messageValid = true;
   }

   public ChangeTransitionTicks(int newValue, int x, int y, int z) {
      this.newValue = newValue;
      this.girlPos = new BlockPos(x, y, z);
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.newValue = buf.readInt();
         this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
         this.messageValid = true;
      } catch (IndexOutOfBoundsException var3) {
         System.out.println("couldn't read bytes @ChangeTransitionTicks :(");
      }
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeInt(this.newValue);
         buf.writeInt(this.girlPos.func_177958_n());
         buf.writeInt(this.girlPos.func_177956_o());
         buf.writeInt(this.girlPos.func_177952_p());
      }
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ChangeTransitionTicks message, MessageContext ctx) {
         if (message.messageValid) {
            ArrayList girlList = GirlEntity.getGirlsByPos(message.girlPos);

            GirlEntity girl;
            for(Iterator var4 = girlList.iterator(); var4.hasNext(); girl.actionController.transitionLengthTicks = (double)message.newValue) {
               girl = (GirlEntity)var4.next();
            }
         } else {
            System.out.println("received an invalid message @ChangeTransitionTicks :(");
         }

         return null;
      }
   }
}
