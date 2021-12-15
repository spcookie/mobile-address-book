package com.cqut.addressBook.controller;

import com.cqut.addressBook.annotation.Api;
import com.cqut.addressBook.annotation.Para;
import com.cqut.addressBook.dto.MessageDto;
import com.cqut.addressBook.entity.User;
import com.cqut.addressBook.server.BookServer;
import com.cqut.addressBook.server.BookServerBean;

import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
@WebServlet(name = "server", value = "/server/*")
public class BookController extends BaseServlet {
    private final BookServer bookServerBean = new BookServerBean();

    @Api(name = "/getUsers")
    public Map<Character, List<User>> getUsers() {
        return bookServerBean.getBook();
    }

    @Api(name = "/addUsers")
    public MessageDto addUsers(User user) {
        return bookServerBean.addBook(user);
    }

    @Api(name = "/modifyUsers")
    public MessageDto modifyUsers(User user) {
        return bookServerBean.modifyUsers(user);
    }

    @Api(name = "/deleteUser")
    public MessageDto deleteUser(@Para(value = "id") Long id) {
        return bookServerBean.deleteUser(id);
    }

    @Api(name = "/fuzzyQuery")
    public List<User> fuzzyQuery(@Para(value = "query") String query) {
        return bookServerBean.fuzzyQuery(query);
    }
}
