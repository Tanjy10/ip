package tanjy.parser;

public enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, SAVE, FIND, UNKNOWN;

    public static CommandType from(String input) {
        try {
            return CommandType.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

