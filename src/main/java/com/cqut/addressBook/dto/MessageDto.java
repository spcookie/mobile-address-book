package com.cqut.addressBook.dto;

/**
 * @author Augenstern
 * @date 2021/12/6
 */
public class MessageDto {
    private String message;
    private Boolean state;

    public MessageDto(String message, Boolean state) {
        this.message = message;
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
