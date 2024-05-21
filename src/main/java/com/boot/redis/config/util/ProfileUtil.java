//package com.boot.redis.config.util;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.collections4.IterableUtils;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import javax.inject.Inject;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProfileUtil {
//    private static Environment env;
//
//    private static List<String> profileList;
//
//    @Inject
//    public void setEnv(Environment env) {
//        this.env = env;
//        if(env.getActiveProfiles().length > 0) {
//            profileList = Arrays.asList(env.getActiveProfiles());
//        }else {
//            profileList = Arrays.asList(env.getDefaultProfiles());
//        }
//    }
//
//
//    public static boolean hasAnyProfile(String... profile) {
//        return CollectionUtils.containsAny(profileList, Arrays.asList(profile));
//    }
//
//    public static String find(String defaultValue, String... profile) {
//        List profiles = Arrays.asList(profile);
//        String p = IterableUtils.find(profileList, it -> profiles.contains(it));
//        if(p == null) {
//            return defaultValue;
//        }
//        return p;
//    }
//
//
//}
