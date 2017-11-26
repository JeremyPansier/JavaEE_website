function applyStyle() {
	var $ = document;
	if (window.chrome) {
		var head = $.getElementsByTagName('head')[0];
		var link = $.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = 'css/variablesForChrome.css';
		head.appendChild(link);
	} else {
		var head = $.getElementsByTagName('head')[0];
		var link = $.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = 'css/variables.css';
		head.appendChild(link);
	}
}
