package cg.zz.wf.mvc;

import org.springframework.beans.factory.annotation.Autowired;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;
import cg.zz.wf.mvc.annotation.Ignored;

/**
 * 
 * 所有的Controller类必须继承此类才能被正确解析
 * 
 * @author chengang
 *
 */
public abstract class MvcController {

	protected Log log = LogFactory.getLog(getClass());
	
	@Autowired
	protected BeatContext beat;

	void setBeatContext(BeatContext beat) {
		this.beat = beat;
	}

	@Ignored
	protected BeatContext getBeatContext() {
		return this.beat;
	}

}
