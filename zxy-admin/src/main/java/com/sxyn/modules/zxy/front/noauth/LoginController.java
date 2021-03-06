/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sxyn.modules.zxy.front.noauth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.sxyn.common.config.Global;
import com.sxyn.common.security.shiro.session.SessionDAO;
import com.sxyn.common.servlet.ValidateCodeServlet;
import com.sxyn.common.utils.CacheUtils;
import com.sxyn.common.utils.CookieUtils;
import com.sxyn.common.utils.IdGen;
import com.sxyn.common.utils.StringUtils;
import com.sxyn.common.web.BaseController;
import com.sxyn.modules.sys.security.FormAuthenticationFilter;
import com.sxyn.modules.sys.security.Principal;
import com.sxyn.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller(value="zxyLoginController")
@RequestMapping(value = "/zxy/")
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	@ResponseBody
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		System.out.println(request.getSession().getId());
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			return renderString(response, principal);
		}

		return null;
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		
		Map<String,Object> resultMap = new HashMap<String, Object>();

		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				resultMap.put("redirect", adminPath + "/login");
				return JSONObject.fromObject(resultMap).toString();
			}
		}
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				resultMap.put("redirect", "modules/sys/sysIndex");
				
				return JSONObject.fromObject(resultMap).toString();
			}
			resultMap.put("redirect", adminPath + "/login");
			return JSONObject.fromObject(resultMap).toString();
		}
		
//		// 登录成功后，获取上次登录的当前站点ID
//		UserUtils.putCache("siteId", StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

//		System.out.println("==========================a");
//		try {
//			byte[] bytes = com.sxyn.common.utils.FileUtils.readFileToByteArray(
//					com.sxyn.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
//			UserUtils.getSession().setAttribute("kkk", bytes);
//			UserUtils.getSession().setAttribute("kkk2", bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		for (int i=0; i<1000000; i++){
////			//UserUtils.getSession().setAttribute("a", "a");
////			request.getSession().setAttribute("aaa", "aa");
////		}
//		System.out.println("==========================b");
		
		resultMap.put("redirect", "modules/sys/sysIndex");
		return JSONObject.fromObject(resultMap).toString();
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		
		resultMap.put("redirect", request.getParameter("url"));
		return JSONObject.fromObject(resultMap).toString();
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
}
