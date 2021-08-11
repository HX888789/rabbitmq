package rabbit.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import rabbit.util.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 1.单个确认 使用的时间 比较那种取人方法是是最好的
 * 2.批量确认
 * 3，异步批量确认
 */
public class ConfirmMessage {
    /**
     * 批量发消息的个数
     */
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        publishMessageIndividually();
        //2.批量确认
        publishMessageBatch();
        //3.异步批量确认
        publishMessageAsync();
    }
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //拿到开始时间
        long begin = System.currentTimeMillis();
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis() - begin;
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + end +"ms");
    }

    /**
     * 批量确认
     * @throws Exception 抛出异常
     */
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //拿到开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;

        //批量发送消息，批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //发布确认
            if(i % batchSize == 0){
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis() - begin;
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + end +"ms");
    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /*
         * 线程安全有序的hash表 适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松的批量删除条目 只要给到序号
         * 3.支持高并发(多线程)
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息成功回调的函数
        ConfirmCallback ackCallBack = (t1,t2) ->{
            //判断是否是批量的
            if (t2){
                //删除掉已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmd = outstandingConfirms.headMap(t1);
                confirmd.clear();
            }else {
                outstandingConfirms.remove(t1);
            }

            System.out.println("确认的消息" + t1);
        };
        //消息失败回调的函数
        ConfirmCallback nackCallback = (t1,t2) ->{
            //打印一下未确认的消息
            String s = outstandingConfirms.get(t1);
            System.out.println("未确认的消息是" + s + "未确认的序号是" + t1);
        };
        //准备消息的监听器 监听哪些消息成功了 哪些消息失败了
        //1.监听成功的消息 2.监听失败的消息 (异步)
        channel.addConfirmListener(ackCallBack,nackCallback);
        //拿到开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //此处记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis() - begin;
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + end +"ms");
    }
}
