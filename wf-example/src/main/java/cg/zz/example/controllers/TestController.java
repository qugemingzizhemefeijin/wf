package cg.zz.example.controllers;

import java.util.HashMap;
import java.util.Map;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.Model;
import cg.zz.wf.mvc.MvcController;
import cg.zz.wf.mvc.actionresult.JsonActionResult;
import cg.zz.wf.mvc.annotation.Path;
import cg.zz.wf.mvc.view.RedirectResult;

/**
 * 
 * 测试
 * 
 * @author chengang
 *
 */
@Path("/api/test")
public class TestController extends MvcController {
	
	/**
	 * 测试输出JSON
	 * @return ActionResult
	 */
	@Path("/json")
	public ActionResult testJson() {
		Map<String , Object> dataMap = new HashMap<>();
		dataMap.put("name", "sm");
		dataMap.put("value", "sd");
		
		return new JsonActionResult(dataMap);
	}
	
	/**
	 * 测试302跳转
	 * @return ActionResult
	 */
	@Path("/redirect")
	public ActionResult testRedirect() {
		return new RedirectResult("/api/test/json");
	}
	
	/**
	 * 测试Velocity模版页面解析功能
	 * @return ActionResult
	 */
	@Path("/velocity")
	public ActionResult testVelocity() {
		Model model = beat.getModel();
		model.add("name", "58");
		model.add("value", "WF");
		
		return ActionResult.view("index");
	}

}
