package cg.zz.wf.mvc.view;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.io.VelocityWriter;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.MvcConstants;

/**
 * 
 * 支持Velocity的ActionResult对象
 * 
 * @author chengang
 *
 */
public class VelocityViewResult extends ActionResult {
	
	//private static RuntimeInstance rtInstance = new RuntimeInstance();
	
	/**
	 * 必须放到views目录下
	 */
    private String prefix = MvcConstants.VIEW_PREFIX;
    
    /**
     * 后缀名必须是html
     */
    private String suffix = ".html";
    
    /**
     * 模版名称
     */
    private String viewName;

    public VelocityViewResult(String viewName2) {
        this.viewName = viewName2;
    }

    public String getViewName() {
        return this.viewName;
    }

    public void render(BeatContext beat) throws Exception {
        Template template = Velocity.getTemplate(this.prefix + "\\" + this.viewName + this.suffix);
        HttpServletResponse response = beat.getResponse();
        response.setContentType("text/html;charset=\"UTF-8\"");
        response.setCharacterEncoding("UTF-8");
        VelocityWriter vw = new VelocityWriter(response.getWriter());
        
        try {
            template.merge(new VelocityContext(beat.getModel().getModel()), vw);
            vw.flush();
        } finally {
            vw.recycle(null);
        }
    }

}
