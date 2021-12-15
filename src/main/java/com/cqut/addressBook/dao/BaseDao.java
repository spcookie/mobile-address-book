package com.cqut.addressBook.dao;

import com.alibaba.fastjson.JSON;
import com.cqut.addressBook.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public class BaseDao {

    protected List<User> open() {
        String resourcePath = Objects.requireNonNull(BaseDao.class.getClassLoader().getResource("data/book.json")).getPath();
        FileReader fileReader = null;
        StringBuilder jsonUsers = new StringBuilder();
        try {
            fileReader = new FileReader(resourcePath, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(fileReader);
            String string;
            do {
                string = reader.readLine();
                if (string != null) {
                    jsonUsers.append(string);
                }
            } while (string != null);
            if (!"".equals(jsonUsers.toString())) {
                return new LinkedList<>(JSON.parseArray(jsonUsers.toString(), User.class));
            } else {
                return new LinkedList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
