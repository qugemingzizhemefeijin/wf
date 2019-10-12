package cg.zz.wf.mvc.view;

import javax.servlet.http.HttpServletRequest;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.Model;
import cg.zz.wf.mvc.MvcConstants;

/**
 * 
 * 支持jsp页面显示的ActionResult<br/>
 * 看代码好像jsp必须以 views+viewName+".jsp"的文件名，如：viewsindex.jsp
 * 
 * @author chengang
 *
 */
public class JspViewResult extends ActionResult {

	private String prefix = MvcConstants.VIEW_PREFIX;
	private String suffix = ".jsp";
	private String viewName;

	public JspViewResult(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return this.viewName;
	}

	public void render(BeatContext beat) throws Exception {
		String path = this.prefix + this.viewName + this.suffix;

		HttpServletRequest request = beat.getRequest();
		Model model = beat.getModel();
		for (String key : model.getModel().keySet()) {
			request.setAttribute(key, model.getAttributeValue(key));
		}
		request.setAttribute("errors", beat.getBindResults().getErrors());

		request.getRequestDispatcher(path).forward(request, beat.getResponse());
	}

}
