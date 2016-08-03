package com.zc7h.test;

import com.zc7h.model.dao.DBUtil;
import com.zc7h.model.domain.UserBean;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by BinLaden on 2016.07.29.
 */
public class TestDBUtil {
    @Test
    public void excuteQuery() throws Exception {
        /*
        String sql = "select * from users where regdate < ?";
        Object[] params = {Timestamp.valueOf("2017-07-20 11:10:00")};
        */

        String sql = "select * from users where regdate <  to_date(? , ?)";
        Object[] params = {"2017-07-20","yyyy-mm-dd"};

        List<Map<String, String>> l = DBUtil.excuteQuery(sql, params);
        Iterator<Map<String, String>> it = l.iterator();
        UserBean[] users = new UserBean[l.size()];

        for(int count = 0; it.hasNext(); count++) {
            UserBean user = new UserBean();
            Map<String, String> entity = it.next();
            user.setId(Integer.valueOf(entity.get("id")));
            user.setUsername(entity.get("username"));
            user.setPassword(entity.get("password"));
            user.setEmail(entity.get("email"));
            user.setGrade(Integer.valueOf(entity.get("grade")));
            users[count] = user;
        }

        for(int i = 0; i < users.length; i++) {
            System.out.println(
                    users[i].getId() + " " +
                    users[i].getUsername() + " " +
                    users[i].getPassword() + " " +
                    users[i].getEmail() + " " +
                    users[i].getGrade()
            );
        }
    }

    @Test
    public void excuteUpdateSingle() throws Exception {
        String sql = "insert into users values (?,?,?,?,?,?)";
        Object[] params = {100,"阿虎","abcdef","zc7h@163.com",3,Timestamp.valueOf("2015-02-07 11:10:00")};
        DBUtil.excuteUpdateSingle(sql, params);
    }

    @Test
    public void excuteUpdateMulti() throws Exception {

    }

    @Test
    public void callProcedure() throws Exception {

    }

}