/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.stockmgmt.dao;

import in.stockmgmt.dbutil.DBConnection;
import in.stockmgmt.pojo.EmployeePojo;
import in.stockmgmt.pojo.UserPojo;
import in.stockmgmt.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public class UserDAO {

    public static boolean validateUser(UserPojo user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from users where userid=? and password=? and usertype=?");
        ps.setString(1, user.getUserid());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getUsertype());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String username = rs.getString(5);
            UserProfile.setUsername(username);
            return true;
        }
        return false;
    }

    public static boolean isUserPresent(String empid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select 1 from users where empid=?");
        ps.setString(1, empid);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static Map<String,ArrayList<String>> getNonRegisteredUsers() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("select * from employees where job in ('Receptionist','Manager','Helper') and empid not in (select empid from users where usertype in ('Receptionist','Manager','Helper'))");
        Map<String,ArrayList<String>> userdetails=new HashMap<>();
        while (rs.next()) {
            ArrayList<String> list=new ArrayList();
            String empid=rs.getString(1);
            String empname=rs.getString(2);
            String usertype=rs.getString(3);
            list.add(empname);
            list.add(usertype);
            userdetails.put(empid, list);
        }
        return userdetails;
    }

    public static boolean addUsers(UserPojo user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into users values(?,?,?,?,?)");
        ps.setString(1, user.getUserid());
        ps.setString(2, user.getEmpid());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getUsertype());
        ps.setString(5, user.getUsername());

        int result = ps.executeUpdate();
        return result == 1;
    }

    public static UserPojo getUserById(String userid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from users where userid=?");
        ps.setString(1, userid);
        UserPojo user = new UserPojo();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            user.setUserid(rs.getString(1));
            user.setEmpid(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setUsertype(rs.getString(4));
            user.setUsername(rs.getString(5));
        }
        return user;
    }

    public static boolean updateUsers(String userid, String pwd) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update users set password=? where userid=?");
        ps.setString(1, pwd);
        ps.setString(2, userid);
        return ps.executeUpdate() == 1;
    }

    public static boolean deleteUsers(String rcpid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from users where userid=?");
        ps.setString(1, rcpid);
        int x = ps.executeUpdate();
        return x == 1;
    }

    public static List<UserPojo> getAllUsers() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select users.empid,empname,userid,job,salary from users,employees where users.empid=employees.empid order by empid");
        ArrayList<UserPojo> userList = new ArrayList<>();
        while (rs.next()) {
            UserPojo user = new UserPojo();
            user.setEmpid(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setUserid(rs.getString(3));
            user.setUsertype(rs.getString(4));
            user.setSalary(rs.getDouble(5));
            userList.add(user);
        }
        return userList;
    }

    public static Map<String,ArrayList<String>> getAllUserId() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("select userid,username,usertype from users order by empid");
        Map<String,ArrayList<String>> userdetails=new HashMap<>();
        while (rs.next()) {
            ArrayList<String> list=new ArrayList();
            String empid=rs.getString(1);
            String empname=rs.getString(2);
            String usertype=rs.getString(3);
            list.add(empname);
            list.add(usertype);
            userdetails.put(empid, list);
        }
        return userdetails;
    }

//    public static List<String> getAllReceptionistUserId() throws SQLException {
//        Connection conn = DBConnection.getConnection();
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("select userid from users where usertype='Receptionist' order by empid");
//        List<String> receptionistList = new ArrayList<>();
//        while (rs.next()) {
//            String id = rs.getString(1);
//            receptionistList.add(id);
//        }
//        return receptionistList;
//    }
}
