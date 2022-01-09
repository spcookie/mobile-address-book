package com.cqut.addressBook.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cqut.addressBook.BookException;
import com.cqut.addressBook.constant.BookConstant;
import com.cqut.addressBook.entity.User;
import com.cqut.addressBook.util.SessionUtil;

import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public class BookDao extends BaseDao {

    public List<User> getUsers() {
        HttpSession httpSession = SessionUtil.get();
        if (httpSession.getAttribute(BookConstant.USERS_LIST.val()) == null) {
            List<User> users = this.open();
            httpSession.setAttribute(BookConstant.USERS_LIST.val(), users);
        }
        return (List<User>) httpSession.getAttribute(BookConstant.USERS_LIST.val());
    }

    public Map<Character, LinkedList<User>> getUsersForLabelMap() {
        HttpSession httpSession = SessionUtil.get();
        if (httpSession.getAttribute(BookConstant.BOOK_MAP.val()) == null) {
            List<User> users = this.getUsers();
            HashMap<Character, LinkedList<User>> usersMap = new HashMap<>(26);
            users.forEach((user) -> {
                Character label = user.getLabel();
                LinkedList<User> list;
                if (usersMap.containsKey(label)) {
                    list = usersMap.get(label);
                } else {
                    list = new LinkedList<>();
                    usersMap.put(label, list);
                }
                list.add(user);
                Collections.sort(list);
            });
            httpSession.setAttribute(BookConstant.BOOK_MAP.val(), usersMap);
        }
        return (Map<Character, LinkedList<User>>) httpSession.getAttribute(BookConstant.BOOK_MAP.val());
    }

    public TreeMap<Long, User> getUsersForPhone() {
        HttpSession httpSession = SessionUtil.get();
        if (httpSession.getAttribute(BookConstant.PHONE_MAP.val()) == null) {
            List<User> users = this.getUsers();
            TreeMap<Long, User> map = new TreeMap<>();
            users.forEach((val) -> {
                ArrayList<String> phone = val.getPhone();
                for (String s : phone) {
                    map.put(Long.parseLong(s), val);
                }
            });
            httpSession.setAttribute(BookConstant.PHONE_MAP.val(), map);
        }
        return (TreeMap<Long, User>) httpSession.getAttribute(BookConstant.PHONE_MAP.val());
    }

    public Boolean addUser(User user) {
        System.out.println("add: " + user);
        List<User> users = this.getUsers();
        users.add(user);
        Map<Character, LinkedList<User>> map = this.getUsersForLabelMap();
        List<User> lu = map.computeIfAbsent(user.getLabel(), k -> new LinkedList<>());
        lu.add(user);
        Collections.sort(lu);
        TreeMap<Long, User> forPhone = this.getUsersForPhone();
        user.getPhone().forEach((val) -> forPhone.put(Long.valueOf(val), user));
        return this.save();
    }

    public Boolean modifyUsers(User user) {
        int index = this.findUser(user.getId());
        if (index != -1) {
            List<User> users = this.getUsers();
            User set = users.get(index);
            if (!set.getName().equals(user.getName())) {
                set.setName(user.getName());
                Map<Character, LinkedList<User>> labelMap = this.getUsersForLabelMap();
                LinkedList<User> labelUsers = labelMap.get(set.getLabel());
                labelUsers.remove(set);
                labelUsers.add(user);
                Collections.sort(labelUsers);
            }
            ArrayList<String> newPhone = user.getPhone();
            ArrayList<String> oldPhone = set.getPhone();
            if (!oldPhone.equals(newPhone)) {
                ArrayList<String> clone = (ArrayList<String>) oldPhone.clone();
                TreeMap<Long, User> forPhone = this.getUsersForPhone();
                oldPhone.retainAll(newPhone);
                clone.removeAll(oldPhone);
                newPhone.removeAll(oldPhone);
                for (String s : clone) {
                    forPhone.remove(Long.parseLong(s));
                }
                for (String s : newPhone) {
                    forPhone.put(Long.parseLong(s), set);
                    oldPhone.add(s);
                }
            }
            if (!set.getAddress().equals(user.getAddress())) {
                set.setAddress(user.getAddress());
            }
            this.save();
            return true;
        }
        return false;
    }

    private int findUser(long id) {
        List<User> users = this.getUsers();
        int size = users.size();
        int index = size / 2;
        while (index >= 0) {
            User t = users.get(index);
            if (id < t.getId()) {
                index /= 2;
            } else if (id > t.getId()){
                index = (size + index) / 2;
            } else {
                return index;
            }
        }
        return -1;
    }

    public Boolean deleteUser(Long id) {
        List<User> users = this.getUsers();
        int index = this.findUser(id);
        if (index != -1) {
            User removeUser = users.remove(index);
            System.out.println("delete: " + removeUser);
            Map<Character, LinkedList<User>> usersForLabelMap = this.getUsersForLabelMap();
            List<User> list = usersForLabelMap.get(removeUser.getLabel());
            list.remove(removeUser);
            if  (list.isEmpty()) {
                usersForLabelMap.remove(removeUser.getLabel());
            }
            TreeMap<Long, User> usersForPhone = this.getUsersForPhone();
            removeUser.getPhone().forEach((phone) -> usersForPhone.remove(Long.parseLong(phone)));
            this.save();
            return true;
        }
        return false;
    }

    //TODO:待优化查询效率
    public List<User> findUserWithName(String name) {
        ArrayList<User> users = new ArrayList<>();
        this.getUsers().forEach((val) -> {
            String valName = val.getName();
            for (int l = name.length() - 1; l < valName.length(); l++) {
                int start = l - (name.length() - 1), end = l + 1;
                if (name.equals(valName.substring(start, end))) {
                    users.add(val);
                }
            }
        });
        return users;
    }

    public List<User> findUserWithPhone(Long lower, Long higher) {
        List<User> users = new ArrayList<>();
        TreeMap<Long, User> usersForPhone = this.getUsersForPhone();
        System.out.println("phone-map -> " + usersForPhone);
        Long key = usersForPhone.ceilingKey(lower);
        if (key != null && key <= higher) {
            SortedMap<Long, User> tailMap = usersForPhone.tailMap(key);
            tailMap.forEach((integer, user) -> {
                if (integer < higher) {
                    users.add(user);
                }
            });
        }
        return users.stream().distinct().collect(Collectors.toList());
    }

    public Boolean save() {
        String resourceAsStream = Objects.requireNonNull(BaseDao.class.getClassLoader().getResource(BookConstant.RESOURCE_PATH.val())).getPath();
        FileWriter writer = null;
        List<User> users = this.getUsers();
        System.out.println("save -> " + users);
        try {
            writer = new FileWriter(resourceAsStream, StandardCharsets.UTF_8);
            String val = JSON.toJSONString(users, SerializerFeature.WriteMapNullValue);
            writer.write(val);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BookException("保存数据出错");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
