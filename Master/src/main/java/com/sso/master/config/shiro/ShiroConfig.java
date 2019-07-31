package com.sso.master.config.shiro;

import com.sso.master.realm.MyCredentialsMatcher;
import com.sso.master.realm.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author:xjk 2019/7/19 12:04
 * com.yy.router.config.shiro
 */
@Configuration
public class ShiroConfig {

    @Value("${shiro.jessionid}")
    private String jessionId;

    @Value("${shiro.loginUrl}")
    private String masterLoginUrl;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setLoginUrl(masterLoginUrl);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/master/doLogin", "anon");
        filterChainDefinitionMap.put("/master/login", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return filterFactoryBean;
    }

    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //12小时
        sessionManager.setGlobalSessionTimeout(43200000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(getRedisSession());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(getSessionIdCookie());
        return sessionManager;
    }

    @Bean
    public RedisSessionDao getRedisSession(){
        return new RedisSessionDao();
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(defaultWebSessionManager());
        return securityManager;
    }

    /**
     * 给shiro的sessionId默认的JSSESSIONID名字改掉
     * @return
     */
    @Bean(name="sessionIdCookie")
    public SimpleCookie getSessionIdCookie(){
        return new SimpleCookie(jessionId);
    }

    /**
     * @see UserRealm--->AuthorizingRealm
     * @return
     */
    @Bean
    @DependsOn(value="lifecycleBeanPostProcessor")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher());
        return userRealm;
    }

    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    /**
     * 该类如果不设置为static，@Value注解就无效，原因未知
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
