POST: $(document).ready(
	function() {

		// SUBMIT FORM
		$("#sendMes").submit(function(event) {
			// Prevent the form from submitting via the browser.
			event.preventDefault();
			ajaxPost();
		});

		function ajaxPost() {

			// PREPARE FORM DATA
			var formData = {
				textMes: $("#addingMessage").val(),
				idLWith: $("#idLWith").val()
			}

			// DO POST
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "addMessageFromT",
				data: JSON.stringify(formData),
				dataType: 'json',
				success: function(result) {
					if (result.status == "success") {
						$("#addingMessage").val('');
						$('#showMesMain').empty();
						$.each(result.data,
							function(i, mesRole) {
								/*var diV = document.createElement("div"); 
								diV.id="inclMes"+i;*/
								var div1 = document.createElement("div"); 
								div1.id="showMes"+i;
								div1.style.height="auto";
								div1.style.width="390px";
								div1.style.float="right";
								document.getElementById("showMesMain").appendChild(div1);
								//document.getElementById("inclMes"+i).appendChild(div1);
								
								if(mesRole.nameRole=="TEACHER"){
								var div = document.createElement("div"); 
								div.style.border = "1px solid black";
								div.style.margin = "5px";
								div.style.padding = "3px";
								div.style.float = "right";
								div.style.borderRadius = "5px";
								div.style.backgroundColor="LightSalmon";
								div.style.wordBreak="break-all";
								div.innerHTML = mesRole.mes.text;
								document.getElementById("showMes"+i).appendChild(div);
								
								var div = document.createElement("div"); 
								div.style.margin = "5px";
								div.style.color="LightGrey";
								div.style.padding = "3px";
								div.style.float = "right";
								div.style.fontSize="10px";
								div.innerHTML = mesRole.mes.dateTime;
								document.getElementById("showMes"+i).appendChild(div);					
								}
								
								else{
									
								var div = document.createElement("div"); 
								div.style.border = "1px solid black";
								div.style.margin = "5px";
								div.style.padding = "3px";
								div.style.float = "left";
								div.style.borderRadius = "5px";
								div.style.backgroundColor="LightSalmon";
								div.style.wordBreak="break-all";
								div.innerHTML = mesRole.mes.text;
								document.getElementById("showMes"+i).appendChild(div);
								
								var div = document.createElement("div"); 
								div.style.margin = "5px";
								div.style.color="LightGrey";
								div.style.float = "left";
								div.style.fontSize="10px";
								div.innerHTML = mesRole.mes.dateTime;
								document.getElementById("showMes"+i).appendChild(div);								
								}
							});
							document.getElementById("showMesMain").scrollTop=document.getElementById("showMesMain").scrollHeight;	
						console.log("Success: ", result);
					} else {
						$("#postResultDiv").html("<strong>Error</strong>");
					}
					console.log(result);
				},
				error: function(e) {
					alert("Error!")
					console.log("ERROR: ", e);
				}
			});

		}

	})