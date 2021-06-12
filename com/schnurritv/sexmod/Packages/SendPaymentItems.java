package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendPaymentItems implements IMessage {
   boolean messageValid;
   String itemName;
   int amount;
   BlockPos girlPos;

   public SendPaymentItems() {
      this.messageValid = false;
   }

   public SendPaymentItems(String itemName, int amount, BlockPos girlPos) {
      this.itemName = itemName;
      this.amount = amount;
      this.girlPos = girlPos;
      this.messageValid = true;
   }

   public SendPaymentItems(int amount, BlockPos girlPos) {
      this.amount = amount;
      this.itemName = "diamond";
      this.girlPos = girlPos;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.itemName = ByteBufUtils.readUTF8String(buf);
      this.amount = buf.readInt();
      this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.itemName);
      buf.writeInt(this.amount);
      buf.writeInt(this.girlPos.func_177958_n());
      buf.writeInt(this.girlPos.func_177956_o());
      buf.writeInt(this.girlPos.func_177952_p());
      this.messageValid = true;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SendPaymentItems message, MessageContext ctx) {
         if (message.messageValid) {
            ArrayList girls = GirlEntity.getGirlsByPos(message.girlPos);

            GirlEntity girl;
            for(Iterator var4 = girls.iterator(); var4.hasNext(); girl.paymentItemsAmount = message.amount) {
               girl = (GirlEntity)var4.next();
               girl.paymentItem = GirlEntity.PaymentItems.valueOf(message.itemName);
            }
         } else {
            System.out.println("received an invalid message @SendPaymentItems :(");
         }

         return null;
      }
   }
}
