package com.example.post;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

/**
 * MybatisPlus代码生成器
 * 已经运行过就不要重复运行了
 */
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/forum?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai",
                        "root",
                        "liaojingpu")
                .globalConfig(builder -> {
                    builder.author("廖菁璞") // 设置作者
//                            .fileOverride() // 是否开启覆盖已生成文件（慎用）
                            .outputDir(System.getProperty("user.dir") + "/post/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.post"); // 设置父包名
//                            .moduleName("system") // 在上述包名下面新建一个文件夹（选用）
                })
                .strategyConfig(builder -> {
                    builder.addInclude("comment") // 设置需要生成的表名
                            .entityBuilder().enableLombok() //开启实体类Lombok
                            .naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel); //开启驼峰命名转换
                })
                .templateEngine(new VelocityTemplateEngine()) // 默认采用Velocity引擎模板
                .execute();
    }

}
