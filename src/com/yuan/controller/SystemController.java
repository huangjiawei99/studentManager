package com.yuan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yuan.bean.User;
import com.yuan.service.UserService;
import com.yuan.util.CpachaUtil;

/**
 * 系统主页控制器
 */
@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		model.setViewName("system/index");
		return model;
	}
	
	/**
	 * 登录页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model){
		model.setViewName("system/login");
		return model;
	}
	
	/**
	 * 登录表单提交
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> login(
			@RequestParam(value="username",required=true) String username,
			@RequestParam(value="password",required=true) String password,
			@RequestParam(value="vcode",required=true) String vcode,
			@RequestParam(value="type",required=true) int type,
			HttpServletRequest request
			){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(username)){
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(password)){
			ret.put("type", "error");
			ret.put("msg", "密码不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(vcode)){
			ret.put("type", "error");
			ret.put("msg", "验证码不能为空!");
			return ret;
		}
		String loginCpacha = (String)request.getSession().getAttribute("loginCpacha");
		if(StringUtils.isEmpty(loginCpacha)){
			ret.put("type", "error");
			ret.put("msg", "长时间未操作，会话已失效，请刷新后重试!");
			return ret;
		}
		if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "验证码错误!");
			return ret;
		}
		//验证码正确，清空，节省内存
		request.getSession().setAttribute("loginCpacha", null);
		//从数据库中去查找用户
		if(type == 1){
			//管理员
			User user = userService.findUserByName(username);
			if(user == null){
				ret.put("type", "error");
				ret.put("msg", "不存在该用户!");
				return ret;
			}
			if(!password.equals(user.getPassword())){
				ret.put("type", "error");
				ret.put("msg", "密码错误!");
				return ret;
			}
			request.getSession().setAttribute("user", user);
		}
		/*if(type == 2){
			//学生
			Student student = studentService.findByUserName(username);
			if(student == null){
				ret.put("type", "error");
				ret.put("msg", "不存在该学生!");
				return ret;
			}
			if(!password.equals(student.getPassword())){
				ret.put("type", "error");
				ret.put("msg", "密码错误!");
				return ret;
			}
			request.getSession().setAttribute("user", student);
		}*/
		ret.put("type", "success");
		ret.put("msg", "登录成功!");
		return ret;
	}
	
	/**
	 * 验证码实现
	 * @param request
	 * @param vl
	 * @param w
	 * @param h
	 * @param response
	 */
	@RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
	public void getCpacha(HttpServletRequest request,
				@RequestParam(value="vl",defaultValue="4",required=false) Integer vl,
				@RequestParam(value="w",defaultValue="98",required=false) Integer w,
				@RequestParam(value="h",defaultValue="33",required=false) Integer h,
				HttpServletResponse response){
		System.out.println("获取验证码-------");
		CpachaUtil cpachaUtil = new CpachaUtil(4, 98, 33);
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute("loginCpacha", generatorVCode);
		//true表示画干扰线
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			//放到response.getOutputStream()里面，response会自动返回给前端页面
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
}
