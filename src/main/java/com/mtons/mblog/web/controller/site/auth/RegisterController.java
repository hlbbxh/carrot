/**
 * 
 */
package com.mtons.mblog.web.controller.site.auth;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.data.UserVO;
import com.mtons.mblog.modules.service.SecurityCodeService;
import com.mtons.mblog.modules.service.UserService;
import com.mtons.mblog.web.controller.BaseController;
import com.mtons.mblog.web.controller.site.Views;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 *
 */
@Controller
@ConditionalOnProperty(name = "site.controls.register", havingValue = "true", matchIfMissing = true)
public class RegisterController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityCodeService securityCodeService;
	
	/**
	 * 这是get请求 用于跳转到注册页面的控制器
	 * @return
	 */
	@GetMapping("/register")
	public String view() {
		AccountProfile profile = getProfile();//获取登录信息
		if (profile != null) {//不等于空 为获取到了登录信息
			return String.format(Views.REDIRECT_USER_HOME, profile.getId());//直接定向到当前用信息
		}
		return view(Views.REGISTER);//为空返回注册界面 的fmk路径
	}
	
	/**
	 * 这个是   用来处理注册请求的    是一个表单的 post 方法
	 * @param post
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/register")
	public String register(UserVO post, HttpServletRequest request, ModelMap model) {
		String view = view(Views.REGISTER);
		try {
			if (siteOptions.getControls().isRegister_email_validate()) {
				String code = request.getParameter("code");
				Assert.state(StringUtils.isNotBlank(post.getEmail()), "请输入邮箱地址");
				Assert.state(StringUtils.isNotBlank(code), "请输入邮箱验证码");
				securityCodeService.verify(post.getEmail(), Consts.CODE_REGISTER, code);
			}
			post.setAvatar(Consts.AVATAR);
			userService.register(post);
			Result<AccountProfile> result = executeLogin(post.getUsername(), post.getPassword(), false);
			view = String.format(Views.REDIRECT_USER_HOME, result.getData().getId());
			//注册成功后 直接  定向到个人主页  地址 在上面拼接
		} catch (Exception e) {
            model.addAttribute("post", post);
			model.put("data", Result.failure(e.getMessage()));//异常
		}
		return view;
	}

}