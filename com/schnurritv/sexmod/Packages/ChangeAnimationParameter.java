package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.GirlEntity;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeAnimationParameter implements IMessage {
   boolean messageValid;
   EnumMap parameters = new EnumMap(GirlEntity.AnimationParameters.class);
   BlockPos entityPos;

   public ChangeAnimationParameter() {
      this.messageValid = false;
   }

   public ChangeAnimationParameter(BlockPos entityPos, GirlEntity.AnimationParameters parameterName, boolean parameterValue) {
      this.parameters.put(parameterName, parameterValue);
      this.entityPos = entityPos;
      this.messageValid = true;
   }

   public ChangeAnimationParameter(BlockPos entityPos, EnumMap parameters) {
      this.parameters = parameters;
      this.entityPos = entityPos;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      int amount = buf.readInt();

      for(int i = 0; i < amount; ++i) {
         String key = ByteBufUtils.readUTF8String(buf);
         boolean value = buf.readBoolean();
         this.parameters.put(GirlEntity.AnimationParameters.valueOf(key), value);
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeInt(this.entityPos.func_177958_n());
         buf.writeInt(this.entityPos.func_177956_o());
         buf.writeInt(this.entityPos.func_177952_p());
         buf.writeInt(this.parameters.size());
         Iterator var2 = this.parameters.entrySet().iterator();

         while(var2.hasNext()) {
            Entry parameter = (Entry)var2.next();
            ByteBufUtils.writeUTF8String(buf, ((GirlEntity.AnimationParameters)parameter.getKey()).name());
            buf.writeBoolean((Boolean)parameter.getValue());
         }

      }
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ChangeAnimationParameter message, MessageContext ctx) {
         if (message.messageValid) {
            if (!GirlEntity.getGirlsByPos(message.entityPos).isEmpty()) {
               ArrayList girlList = GirlEntity.getGirlsByPos(message.entityPos);
               Iterator var4 = girlList.iterator();

               while(var4.hasNext()) {
                  GirlEntity girl = (GirlEntity)var4.next();
                  Iterator var6 = message.parameters.entrySet().iterator();

                  while(var6.hasNext()) {
                     Entry parameter = (Entry)var6.next();
                     girl.animationParameters.replace(parameter.getKey(), parameter.getValue());
                  }
               }
            }
         } else {
            System.out.println("recieved an unvalid message @ChangeAnimationParameter :(");
         }

         return null;
      }
   }
}
