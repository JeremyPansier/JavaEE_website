<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div alink="#ee0000" link="#0000ee" vlink="#551a8b"
	style="background-color: rgb(255, 255, 255); color: rgb(0, 0, 0)">
	<big style="font-family: Arial Black"> <br>
	</big>
	<table
		style="text-align: left; margin-left: auto; margin-right: auto; height: 246px; width: 827px"
		cellspacing="2" cellpadding="2" border="0">
		<tbody>
			<tr>
				<td style="background-color: rgb(220, 220, 220)">
					<div style="text-align: center">
						<div style="text-align: left">
							<big style="font-family: Comic Sans MS; color: rgb(204, 0, 0)">
								JavengersEvents <br> <br>
							</big>
						</div>
						<big style="font-family: Arial Black">Vous avez été invité à
							l'événement:</big> <br> <big style="font-family: Arial Black">
							<span style="color: rgb(102, 0, 204)">${title}</span>
						</big> <br>
					</div> <big style="font-family: Arial Black"> <br>
				</big>
					<div style="text-align: center">
						<big style="font-family: Arial Black">Cliquez sur le lien
							pour accépter ou refuser l'invitation:</big> <br> <big
							style="font-family: Arial Black"> <a
							href="${link}"
							target="_blank">${link}</a>
						</big>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	${imageLink}
</div>
</body>
</html>