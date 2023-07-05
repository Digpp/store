package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.UserService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 接收数据的方式1请求处理方法的参数设置为pojo类型来接收前端的数据
 * SpringBoot会将前端的url地址中的参数名和pojo类的属性名进行比较，如果
 * 这俩个名称相同，则将值注入到pojo类中对应的属性上
 *
 * 接收数据的方式2:请求处理方法的参数设置为非pojo类型
 * SpringBoot会将前端的url地址中的参数名和pojo类的属性名进行比较，如果
 * 这俩个名称相同则自动完成值的依赖注入
 * */

@RestController()
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**用户注册控制层*/
    @RequestMapping("reg")
    public JsonResult<Void> reg(User user){
        userService.reg(user);
        return new JsonResult<>(OK);
    }

    /**用户登入控制层*/
    @RequestMapping("login")
    public JsonResult<User> login(String username, String password, HttpSession session){
        User data = userService.login(username,password);
        //session是全局的
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());

        System.out.println("登入用户"+getUsernameFromSession(session));
        return new JsonResult<User>(OK,data);
    }

    /**更新用户密码控制层*/
    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,String newPassword,HttpSession session){
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changePassword(uid,username,oldPassword,newPassword);
        return new JsonResult<>(OK);
    }

    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session){
        User data = userService.getByUid(getuidFromSession(session));
        return new JsonResult<>(OK,data);
    }

    /**更新用户信息控制层*/
    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user,HttpSession session){
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid,username,user);
        return new JsonResult<>(OK);
    }

    /**
     * MultipartFile接口是SpringMVC提供的一个接口，这个接口为我们包装了获取文件类型的数据(任何类型的file都可以接收),
     * SpringBoot它会整合了SpringMVC,只需要在处理请求的方法参数列表声明一个参数类型为MultipartFile的参数,
     * 然后SpringBoot自动将传递给服务的文件数据赋值给这个参数
     *
     * @RequestParam 可以将的参数注入请求处理方法的某个参数上，如果参数名不一致则可以使用@RequestParam注解,与Mybatis中的@Param注解相似
     * @param session
     * @param file
     * @return 头像路径
     */

    /**设置上传文件的最大限制*/
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    /**设置上传文件的类型限制*/
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }

    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session, @RequestParam("file")MultipartFile file){
        if (file == null){
            throw new FileEmptyException("文件为空");
        }
        if (file.getSize()>AVATAR_MAX_SIZE){
            throw new FileSizeException("文件大小超出限制");
        }
        //判断文件是否符合限制的类型
        String fileType = file.getContentType();
        if (!AVATAR_TYPE.contains(fileType)){
            throw new FileTypeException("文件类型不支持");
        }
        //上传的文件存放位置 /upload/文件.png
        String parent = session.getServletContext().getRealPath("upload");
        File dir = new File(parent);
        if (!dir.exists()){  //检查文件是否存在
            dir.mkdir();  //创建当前目录
        }
        //获取到文件名称,UUID工具来将生成一个新的字符串作为文件名
        String originalFilename = file.getOriginalFilename();  //获取文件名称
        int index = originalFilename.lastIndexOf(".");  //拿到.最后出现的索引
        String suffix = originalFilename.substring(index);  //从.出现的索引开始截取,为了拿到文件后缀名
        //对文件名进行随机生成，再与后缀名进行拼接 SID2-KICKS2OP-KSM4-SILK45.pnj
        String fileName = UUID.randomUUID().toString().toUpperCase()+suffix;   //toUpperCase()将字符转为大写

        File dest = new File(dir , fileName);  //在dir目录下创建一个fileName文件名的空文件
        try {
            file.transferTo(dest);  //将file文件写入到目标文件中
        } catch (FileStateException e){
            throw new FileStateException("文件状态异常");
        }catch (IOException e) {
            throw new FileUploadIOException("文件读写异常");
        }
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        //返回值为头像的路径,给前端用于做头像的展示使用
        String avatar = "/upload/" + fileName;
        userService.changAvatar(uid , avatar , username);
        return new JsonResult<>(OK,avatar);
    }
}