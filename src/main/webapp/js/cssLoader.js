function applyStyle() {
	var head = document.getElementsByTagName('head')[0];
	var link = document.createElement('link');
	link.rel = 'stylesheet';
	link.type = 'text/css';
	if (window.chrome) {
		link.href = 'css/variablesForChrome.css';
	} else {
		link.href = 'css/variables.css';
	}
		head.appendChild(link);
}
