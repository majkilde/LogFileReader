package dk.majkilde.logreader.files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.majkilde.logreader.files.Filter.Scope;
import dk.xpages.utils.XML;

public class Filters implements Serializable {

	private static final long serialVersionUID = 1L;

	List<Filter> filters = new ArrayList<Filter>();
	private final Filter defaultFilter;

	public Filters(XML config) {
		if (config != null) {
			for (XML filter : config.children("filter")) {
				addFilter(new Filter(filter));
			}
		}

		defaultFilter = new Filter("default").setClass("default-filter").setRegex(".").setScope(Scope.LINE).setVisible(true);
	}

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	public String apply(final String source) {
		String result = source;
		boolean lineFilterApplied = false;

		for (Filter filter : filters) {
			if (filter.getScope() == Filter.Scope.LINE && !lineFilterApplied) {
				if (filter.matches(result)) {
					result = filter.applyFilter(result);
					lineFilterApplied = true;
				}
			}
			if (filter.getScope() != Filter.Scope.LINE) {
				result = filter.applyFilter(result);
			}
		}

		if (!lineFilterApplied) {
			result = defaultFilter.applyFilter(result);
		}

		return result;
	}

	public List<String> apply(List<String> source) {
		for (int i = 0; i < source.size(); i++) {
			source.set(i, apply(source.get(i)));
		}

		return source;
	}

}
