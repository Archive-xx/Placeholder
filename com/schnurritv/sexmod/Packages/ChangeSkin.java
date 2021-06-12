package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeSkin implements IMessage {
   boolean messageValid;
   int skinIndex;
   BlockPos girlPos;

   public ChangeSkin() {
      this.messageValid = false;
   }

   public ChangeSkin(int skinIndex, BlockPos girlPos) {
      this.skinIndex = skinIndex;
      this.girlPos = girlPos;
      this.messageValid = true;
   }

   public ChangeSkin(int skinIndex, GirlEntity sophi) {
      this.skinIndex = skinIndex;
      this.girlPos = sophi.func_180425_c();
      this.messageValid = true;
   }

   public ChangeSkin(int skinIndex, int x, int y, int z) {
      this.skinIndex = skinIndex;
      this.girlPos = new BlockPos(x, y, z);
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.skinIndex = buf.readInt();
         this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
         this.messageValid = true;
      } catch (IndexOutOfBoundsException var3) {
         System.out.println("couldn't read bytes @ChangeSkin :(");
      }
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeInt(this.skinIndex);
         buf.writeInt(this.girlPos.func_177958_n());
         buf.writeInt(this.girlPos.func_177956_o());
         buf.writeInt(this.girlPos.func_177952_p());
      }
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ChangeSkin message, MessageContext ctx) {
         if (message.messageValid) {
            ArrayList girlList = GirlEntity.getGirlsByPos(message.girlPos);

            GirlEntity girl;
            for(Iterator var4 = girlList.iterator(); var4.hasNext(); girl.currentModel = message.skinIndex) {
               girl = (GirlEntity)var4.next();
            }
         } else {
            System.out.println("recieved an unvalid message @ChangeSkin :(");
         }

         return null;
      }
   }
}
