package com.trilogyed.levelqueueconsumer;

import com.trilogyed.levelqueueconsumer.util.feign.LevelUpClient;
import com.trilogyed.levelqueueconsumer.util.messages.LevelUp;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {
    @Autowired
    private LevelUpClient client;

    @RabbitListener(queues = LevelQueueConsumerApplication.QUEUE_NAME)
    public void receiveMessage(LevelUp msg) {

        // Let the presence of a valid ID determine create or update
        System.out.println(msg.toString());
        if(msg.getLevelUpId() != 0){
            client.updateLevelUp(msg.getLevelUpId(), msg);
            System.out.println("level updated");
        }else {
            client.addLevelUp(msg);
            System.out.println("new message");
        }
    }
}
