package com.roncoo.education.course.service.api.auth;

import com.roncoo.education.course.service.NonStaticResourceHttpRequestHandler;
import com.roncoo.education.course.service.api.auth.biz.AuthApiCourseBiz;
import com.roncoo.education.util.config.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roncoo.education.course.common.bo.auth.AuthCourseSignBO;
import com.roncoo.education.course.common.bo.auth.AuthCourseViewBO;
import com.roncoo.education.course.common.dto.auth.AuthCourseSignDTO;
import com.roncoo.education.course.common.dto.auth.AuthCourseViewDTO;
import com.roncoo.education.util.base.BaseController;
import com.roncoo.education.util.base.Result;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 课程信息-审核
 *
 * @author wujing
 */
@RestController
@RequestMapping(value = "/course/auth/course")
public class AuthApiCourseController extends BaseController {
	@Autowired
	NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

	@Autowired
	private AuthApiCourseBiz biz;

	/**
	 * 课程详情接口
	 */
	@ApiOperation(value = "课程详情接口", notes = "用户登录后获取课程详情信息接口")
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public Result<AuthCourseViewDTO> view(@RequestBody AuthCourseViewBO authCourseViewBO) {
		return biz.view(authCourseViewBO);
	}

	/**
	 * 课时播放获取sign值接口
	 */
	@ApiOperation(value = "课时播放获取sign值接口", notes = "课时播放获取sign值接口")
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public Result<AuthCourseSignDTO> sign(@RequestBody AuthCourseSignBO authCourseSignBO) {
		return biz.sign(authCourseSignBO);
	}


	@RequestMapping(value = "/play", method = RequestMethod.GET)
	public void videoPreview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filename = request.getParameter("filename");
		Path filePath = Paths.get(SystemUtil.PERIOD_VIDEO_PATH + filename );
		if (Files.exists(filePath)) {
			String mimeType = Files.probeContentType(filePath);
			if (!StringUtils.isEmpty(mimeType)) {
				response.setContentType(mimeType);
			}
			request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
			nonStaticResourceHttpRequestHandler.handleRequest(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		}
	}
}
