package cg.zz.wf.mvc.view;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.codec.Charsets;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.thread.BeatContextBean;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 
 * 支持Freemarker的ActionResult
 * 
 * @author chengang
 *
 */
public class FreemarkerViewResult extends ActionResult {

	static Configuration cfg = null;
	static final String ftlViewPath = "ftlviews";
	static final String suffix = ".ftl";
	private String viewName;

	static {
		cfg = null;
		try {
			String webAppPath = BeatContextBean.servletContext.getRealPath("/");
			cfg = new Configuration(Configuration.VERSION_2_3_29);
			cfg.setDirectoryForTemplateLoading(new File(webAppPath+"/"+ftlViewPath+"/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
			cfg.setDefaultEncoding(Charsets.UTF_8.name());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ActionResult view(String viewName2) {
		return new FreemarkerViewResult(viewName2);
	}

	public FreemarkerViewResult(String viewName2) {
		this.viewName = viewName2;
	}

	public String getViewName() {
		return this.viewName;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		Template temp = cfg.getTemplate(this.viewName + suffix, Charsets.UTF_8.name());
		Writer out = new OutputStreamWriter(beat.getResponse().getOutputStream(), Charsets.UTF_8.name());
		temp.process(beat.getModel().getModel(), out);
		out.flush();
	}

}
