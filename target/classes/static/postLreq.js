POST: $(document).ready(
	function() {
			

		// SUBMIT FORM
		$("#sendMesL").submit(function(event) {
			// Prevent the form from submitting via the browser.
			event.preventDefault();
			ajaxPost();
		});

		function ajaxPost() {

			// PREPARE FORM DATA
			var formData = {
				textMes: $("#addingMessageL").val(),
				idLWith: $("#IdLearner").val()
			}

			// DO POST
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "addMessageFromL",
				data: JSON.stringify(formData),
				dataType: 'json',
				success: function(result) {
					if (result.status == "success") {
						$("#addingMessageL").val('');
						$('#showMesL').empty();
						$.each(result.data,
							function(i, mesRole) {							
								var div1 = document.createElement("div"); 
								div1.id="showMes"+i;
								div1.style.height="auto";
								div1.style.width="400px"
								div1.style.float="right";
								document.getElementById("showMesL").appendChild(div1);
								
								if(mesRole.nameRole=="LEARNER"){
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
							document.getElementById("showMesL").scrollTop=document.getElementById("showMesL").scrollHeight;
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