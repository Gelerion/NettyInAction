package com.gelerion.apps.udp.log.events.broadcaster.model;

import java.net.InetSocketAddress;

public class LogEvent {
    public static final byte SEPARATOR = (byte) ':';
    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    //outgoing message
    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    //incoming message
    public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    //inet address of the source that sent the event
    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceivedTimestamp() {
        return received;
    }
}
