package com.dropbox.client.constant;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    AUTH("auth"),
    INFO("info"),
    LIST("list"),
    ;

    private String code;
    private static final Map<String,Command> commands = new HashMap<>();

    static {
        for (Command c : Command.values() ) {
            commands.put(c.code,c);
        }
    }

    Command(String code) {
        this.code = code;
    }

    public static Command getCommand(String code){
        return commands.get(code);
    }
}
