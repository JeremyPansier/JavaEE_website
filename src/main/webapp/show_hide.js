function show_hide(id) {
	if (document.getElementById(id).style.display == "none") {
		document.getElementById(id).style.display = "block";
		document.getElementById('button_' + id).innerHTML = 'Masquer la description';
	} else {
		document.getElementById(id).style.display = "none";
		document.getElementById('button_' + id).innerHTML = 'Afficher la description';
	}
	return true;
}