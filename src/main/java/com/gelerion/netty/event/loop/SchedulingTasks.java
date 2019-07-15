package com.gelerion.netty.event.loop;


import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/*
Occasionally you’ll need to schedule a task for later (deferred) or periodic execution. For example, you might want
to register a task to be fired after a client has been connected for five minutes. A common use case is to send a
heartbeat message to a remote peer to check whether the connection is still alive. If there is no response, you know
you can close the channel.
 */
@SuppressWarnings("all")
public class SchedulingTasks {

    public static void main(String[] args) {
        Channel ch = null;
        ScheduledFuture<?> future = ch.eventLoop()
                .schedule(() -> System.out.println("60 seconds later"), 60, TimeUnit.SECONDS);

        future.cancel(false);


    }

}
