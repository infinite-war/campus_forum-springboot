# campus_forum-springboot
校园论坛项目(后端部分)


## 使用
1. 使用`resources/database/schema.sql`文件执行建表操作
2. 修改`application-x.yml`中的配置内容。测试项目时使用`application-dev.yml`配置文件，项目打算发行时改用`application-prod.yml`文件，选择哪个配置文件可以再`application.yml`中设置
3. 本地启动后端项目后，通过浏览器输入`localhost:你配置的端口/swagger-ui.html`可以进入接口测试页面
