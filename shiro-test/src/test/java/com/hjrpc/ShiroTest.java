package com.hjrpc;

import com.alibaba.druid.pool.DruidDataSource;
import com.hjrpc.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class ShiroTest {

    private IniRealm iniRealm;
    private SimpleAccountRealm simpleAccountRealm;
    private JdbcRealm jdbcRealm;
    private CustomRealm customRealm;
    @Before
    public void initRealm(){
//        iniRealm = new IniRealm("classpath:user.ini");
//        simpleAccountRealm = new SimpleAccountRealm();
//        simpleAccountRealm.addAccount("zhangsan","12345","admin");
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl("jdbc:mysql://192.168.56.102:3306/shiroRealm");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        jdbcRealm = new JdbcRealm();
//        jdbcRealm.setPermissionsLookupEnabled(true);
//        jdbcRealm.setDataSource(dataSource);
        customRealm = new CustomRealm();
        customRealm.setName("customRealm");
    }

    @Test
    public void testSimpleRealm(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(simpleAccountRealm);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","12345");
        subject.login(token);
        subject.checkRoles("admin");
    }

    @Test
    public void testIniRealm(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(iniRealm);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","12345");
        subject.login(token);
        subject.checkRoles("admin");
//        subject.checkRoles("admin","user");
        subject.checkPermissions("user:add","user:update");
//        subject.checkPermissions("user:add","user:update","user:delete");
    }

    @Test
    public void testJdbcRealm() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        securityManager.setRealm(jdbcRealm);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("admin","1");
        subject.login(token);
        subject.checkRoles("admin");
//        subject.checkRoles("admin","user");
//        subject.checkPermissions("user:add","user:update");
        subject.checkPermission("user:delete");
//        subject.checkPermissions("user:add","user:update","user:delete");
    }

    @Test
    public void testCustomRealm() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);


        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);
        securityManager.setRealm(customRealm);


        Subject subject = SecurityUtils.getSubject();
        Md5Hash md5Hash = new Md5Hash("1","randomNum");
        System.out.println(md5Hash.toString());
        UsernamePasswordToken token = new UsernamePasswordToken("admin","1");
        subject.login(token);
//        subject.checkRoles("admin");
//        subject.checkRoles("admin","user");
//        subject.checkPermissions("user:add","user:update");
//        subject.checkPermission("user:delete");
//        subject.checkPermissions("user:add","user:update","user:delete");
    }
}
