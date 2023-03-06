package com.abnamro.recipe.api.constant;

public class Validation {

    public static final String NAME_PATTERN = "^(?:\\p{L}\\p{M}*|[',. \\-]|\\s)*$";
    public static final int MAX_LENGTH_NAME = 100;
    public static final String FREE_TEXT_PATTERN = "^(?:\\p{L}\\p{M}*|[0-9]*|[\\/\\-+.,?!*();\"]|\\s)*$";
    public static final int MAX_LENGTH_FREE_TEXT = 255;
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

}
