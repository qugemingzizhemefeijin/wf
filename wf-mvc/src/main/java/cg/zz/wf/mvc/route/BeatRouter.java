package cg.zz.wf.mvc.route;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 请求URL路由<br/>
 * 此类维护
 * 
 * @author chengang
 *
 */
public class BeatRouter {

	/**
	 * 所有的ActionInfo集合
	 */
	private Set<ActionInfo> actions = new LinkedHashSet<>();
	
	/**
	 * URL正则表达式匹配器
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 将ActionInfo添加到维护的集合中
	 * @param action - ActionInfo
	 */
	public void addMapping(ActionInfo action) {
		this.actions.add(action);
	}

	/**
	 * 根据请求的信息获取RouteResult，里面维护的是ActionInfo对象用来反射调用真实的类方法<br/>
	 * NOTE:个人觉得这个地方还可以将所有的URL维护到一个HashSet中，这样做快速匹配要比两个for循环要快多了
	 * @param beat - BeatContext
	 * @return RouteResult
	 */
	public RouteResult route(BeatContext beat) {
		//获取本次请求的相对地址
		String url = beat.getClient().getRelativeUrl();
		String bestPathMatch = null;
		ActionInfo bestAction = null;
		//第一步，简单的判断equals以及请求类型是否一致
		for (ActionInfo action : this.actions) {
			for (String registeredPath : action.getMappedPatterns()) {
				if (Objects.equals(url, registeredPath) && action.matchRequestMethod(beat)) {
					bestPathMatch = registeredPath;
					bestAction = action;
				}
			}
		}
		//如果简单匹配未匹配到，则使用正则匹配模式，当然性能也会差一点
		//这个地方还用到了最佳匹配，尽量批量正则长的URL
		if (bestPathMatch == null) {
			for (ActionInfo action : this.actions) {
				for (String registeredPath : action.getMappedPatterns()) {
					if (this.pathMatcher.match(registeredPath, url)
							&& (bestPathMatch == null || bestPathMatch.length() < registeredPath.length() && action.matchRequestMethod(beat))) {
						bestPathMatch = registeredPath;
						bestAction = action;
					}
				}
			}
		}
		//未匹配到直接返回空
		if (bestPathMatch == null) {
			return null;
		}
		
		//将匹配到的结果封装到RouteResult对象中
		RouteResult match = new RouteResult();
		match.action = bestAction;
		match.beat = beat;
		if (match.action.getParamNames().length <= 0) {
			return match;
		}
		//提取里面的动态参数
		match.urlParams = this.pathMatcher.extractUriTemplateVariables(bestPathMatch, url);
		return match;
	}

}
