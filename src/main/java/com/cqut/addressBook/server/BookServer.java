package com.cqut.addressBook.server;

import com.cqut.addressBook.dto.MessageDto;
import com.cqut.addressBook.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public interface BookServer {
    Map<Character, List<User>> getBook();
    MessageDto addBook(User user);
    MessageDto modifyUsers(User user);
    MessageDto deleteUser(Long id);
    List<User> fuzzyQuery(String query);
}