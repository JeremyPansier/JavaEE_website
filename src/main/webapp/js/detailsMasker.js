function switchVisibility(id) {
	if (document.getElementById(id).style.display == "none") {
		document.getElementById(id).style.display = "block";
		document.getElementById('button_' + id).innerHTML = 'Masquer la description';
	} else {
		document.getElementById(id).style.display = "none";
		document.getElementById('button_' + id).innerHTML = 'Afficher la description';
	}
	return true;
}

function hideIfEmpty(id, size) {
	if (size == 0) {
		document.getElementById(id).style.display = "none";
	} else {
		document.getElementById(id).style.display = "block";
	}
	return true;
}