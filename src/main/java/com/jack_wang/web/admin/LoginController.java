package com.jack_wang.web.admin;

import com.jack_wang.po.User;
import com.jack_wang.service.UserService;
import com.jack_wang.util.CaptchaCodeUtil;
import com.jack_wang.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @ResponseBody
    @GetMapping("/code")
    public void code(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String ip = request.getRemoteAddr();
        String code = new CaptchaCodeUtil().randomStr(4);
        redisUtil.set("code" + ip, code,60*2);
        CaptchaCodeUtil captchaCodeUtil = new CaptchaCodeUtil(116, 36, 4, 15, code);
        captchaCodeUtil.write(response.getOutputStream());
    }

    @GetMapping
    public String loginPage() {

        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String code,
                        HttpSession session,
                        RedirectAttributes attributes,
                        HttpServletRequest request) {

        String ip = request.getRemoteAddr();

        String checkCode = (String)redisUtil.get("code" + ip);

        if (code != null && code.equalsIgnoreCase(checkCode)){
            redisUtil.del("code" + ip);
            User user = userService.checkUser(username, password);
            if (user != null) {
                user.setPassword(null);
                session.setAttribute("user", user);
                return "admin/index";
            } else {
                attributes.addFlashAttribute("message", "用户名或密码错误");
                return "redirect:/admin";
            }
        }else {
            return "redirect:/admin";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
