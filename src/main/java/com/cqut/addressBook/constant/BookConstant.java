package com.cqut.addressBook.constant;

/**
 * @author Augenstern
 * @date 2021/12/10
 */
public enum BookConstant {
    USERS_LIST("users"),
    BOOK_MAP("bookMap"),
    PHONE_MAP("phoneMap"),
    RESOURCE_PATH("data/book.json");

    private final String value;

    BookConstant(String value) {
        this.value = value;
    }

    public String val() {
        return value;
    }
}
