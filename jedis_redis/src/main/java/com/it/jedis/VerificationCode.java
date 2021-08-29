package com.it.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

public class VerificationCode {

    public static void main(String[] args) {
        //模拟验证码发送
        setCode("13812345678");

        //模拟验证码校验
//        verifyCode("13812345678", "ERROR");
    }

    //1、输入手机号，点击发送后随机生成6位数字码
    public static String getCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10);
            code += num;
        }
        return code;
    }

    //2、发送验证码，每个手机号每天只能输入3次验证码，2分钟有效
    public static void setCode(String mobileNumber) {
        //连接Redis
        Jedis jedis = new Jedis("192.168.52.129", 6379);
        //拼接key
        String countKey = "VerifyCode" + mobileNumber + ":count"; //手机发送次数key
        String codeKey = "VerifyCode" + mobileNumber + ":code"; //系统验证码key
        //手机每天发送三次
        String count = jedis.get(countKey);
        if (count == null) { //未曾发送，第一次发送
            //Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
            jedis.setex(countKey, 24*60*60, "1");
        } else if (Integer.parseInt(count) <= 2) {
            //发送次数+1
            jedis.incr(countKey);
        } else if (Integer.parseInt(count) > 2) {
            //发送次数超过三次
            System.out.println("当日发送次数已经超过三次");
            jedis.close();
        }

        //发送系统验证码到Redis中
        String vcode = getCode();
        jedis.setex(codeKey, 120, vcode);
        jedis.close();
    }

    //3、点击验证，返回成功或失败
    public static void verifyCode(String mobileNumber, String code) {
        //连接Redis
        Jedis jedis = new Jedis("192.168.52.129", 6379);
        //系统的验证码
        String codeKey = "VerifyCode" + mobileNumber + ":code";
        String verificationCode = jedis.get(codeKey);
        //判断验证码
        if (verificationCode == null) {
            System.out.println("验证码已过期");
        } else {
            if (verificationCode.equals(code)) {
                System.out.println("验证码正确");
            } else {
                System.out.println("验证码错误");
            }
        }

        jedis.close();
    }
}
