package com.cqut.addressBook.server;

import com.cqut.addressBook.dao.BookDao;
import com.cqut.addressBook.dto.MessageDto;
import com.cqut.addressBook.entity.User;
import com.cqut.addressBook.util.IDUtil;
import com.cqut.addressBook.util.PinYinUtil;

import java.util.*;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public class BookServerBean implements BookServer {
    private final BookDao bookDao = new BookDao();

    @Override
    public Map<Character, LinkedList<User>> getBook() {
        Map<Character,LinkedList<User>> map = bookDao.getUsersForLabelMap();
        System.out.println("label-map -> " + map);
        return map;
    }

    @Override
    public MessageDto addBook(User user) {
        List<User> users = bookDao.getUsers();
        String pinyin = PinYinUtil.pinyin(user.getName());
        user.setId(IDUtil.id());
        user.setPinYin(pinyin);
        if (users.contains(user)) {
            return new MessageDto("已存在该联系人", false);
        } else {
            bookDao.addUser(user);
            return new MessageDto("添加成功", true);
        }
    }

    @Override
    public MessageDto modifyUsers(User user) {
        user.setPinYin(PinYinUtil.pinyin(user.getName()));
        Boolean result = bookDao.modifyUsers(user);
        return result
                ? new MessageDto("修改成功", true)
                : new MessageDto("修改失败", false);
    }

    @Override
    public MessageDto deleteUser(Long id) {
        Boolean result = bookDao.deleteUser(id);
        return result
                ? new MessageDto("删除成功", true)
                : new MessageDto("不存在该该用户", false);
    }

    @Override
    public List<User> fuzzyQuery(String query) {
        if (query != null) {
            int type = query.charAt(0);
            if ('0' <= type && type <= '9') {
                int length = 11 - query.length();
                if (length >= 0) {
                    String buffer = query + "0".repeat(length);
                    long lower = Long.parseLong(buffer);
                    buffer = Long.parseLong(query) + 1 + "0".repeat(length);
                    long higher = Long.parseLong(buffer);
                    return bookDao.findUserWithPhone(lower, higher);
                }
                return null;
            } else {
                return bookDao.findUserWithName(query);
            }
        } else {
            return null;
        }
    }
}
