CFINAC.index = new Object();

$(document).ready(
		function() {		
			
			CFINAC.index.allInvites = function(){
                var cfg = {
                    type: "GET",
                    url: "../rest/familia/getInvitesInfo",
                    success: function(invites) {
                    	CFINAC.index.showInvites(invites);
                    },
                    error: function(e) {
                    	CFINAC.Message(e.responseText, "error");
                    }
                };
                CFINAC.ajax.post(cfg);
            };

            CFINAC.index.showInvites = function(invites) {
            	$("#ModalLabel").html("Convites");
                var html = "<div class='table-responsive' id='details'>";
                html += "<table class='table table-hover table-striped'>";
                html += "<tr>";
                html += "<th>Nome Família</th>";
                html += "<th>Dono</th>";
                html += "<th>Ações</th>";
                html += "</tr>";
                if (invites == null || invites == "") {
                    html += "<tr>";
                    html += "<td colspan = 3>Você não tem nenhum convite pendente!!</td>";
                    html += "</tr>";
                } else {
                    for (var x = 0; x < invites.length; x++) {
                        html += "<tr>";
                        html += "<td>"+invites[x].familyName+"</td>";
                        html += "<td>"+invites[x].ownerName+"</td>";
                        html += "<td colspan='2'><button type='reset' onclick='CFINAC.index.declineInvite("+invites[x].familyId+")' class='btn btn-danger buttom'>Recusar</button><button type='button' class='btn btn-success buttom' onclick='CFINAC.index.acceptInvite("+invites[x].familyId+")'>Aceitar</button></td>";
                        html += "</tr>";
                    }
                }
                html += "</table>"
                html += "</div>";
                $("#content").html(html);
            };
			
            CFINAC.index.declineInvite = function(id){
				var cfg = {
						type : "POST",
						url : "../rest/familia/declineInvite/"+id,
						data: "id="+id,
						success : function(msg) {
							CFINAC.index.allInvites();
						},
						error : function(e) {
                        	CFINAC.Message(e.responseText, "error");
						}
					};
					CFINAC.ajax.post(cfg);	
            }
            
            CFINAC.index.acceptInvite = function(id){
				var cfg = {
						type : "POST",
						url : "../rest/familia/acceptInvite/"+id,
						data: "id="+id,
						success : function(msg) {
							CFINAC.index.allInvites();
							CFINAC.index.getUserInfo();
						},
						error : function(e) {
                        	CFINAC.Message(e.responseText, "error");
						}
					};
					CFINAC.ajax.post(cfg);
            }
            
            
			CFINAC.familia.getInvites = function(){
					var cfg = {
						type : "GET",
						url : "../rest/familia/getInvites",
						success : function(invites) {
								$("#invites").html(" "+invites.length);
						},
						error : function(e) {
                        	CFINAC.Message(e.responseText, "error");
						}
					};
					CFINAC.ajax.post(cfg);
				}
				
			CFINAC.familia.getInvites();
			setInterval(CFINAC.familia.getInvites, 30000);
			
			$("#refresh").on("click", function(){
				CFINAC.index.getUserInfo();
				CFINAC.familia.getInvites();
			})
			
			CFINAC.index.getUserInfo = function() {
				var cfg = {
					type : "POST",
					url : "../rest/usuario/getUserInfo",
					success : function(userInfo) {
						$("#username").html(userInfo.usuario);
						$("#actualbalance").html(
								"Saldo Atual: " + userInfo.currentBallanceFormated);
						$("#family").html(userInfo.nomeFamilia);
						$("#next").html("Próx. conta: " + userInfo.next);
						$("#nextbalance").html(
								"Saldo posterior: " + userInfo.nextBallanceFormated)
					},
					error : function(e) {
                    	CFINAC.Message(e.responseText, "error");
						window.location="../Login.html?invalid";
					}
				};
				CFINAC.ajax.post(cfg);
			}
			
			CFINAC.index.getUserInfo();
			setInterval(CFINAC.index.getUserInfo, 30000);

			CFINAC.index.endSession = function() {
				var cfg = {
					type : "POST",
					url : "EndSession",
					success : function(userInfo) {
						$("#username").html(userInfo.usuario);
						$("#actualbalance").html(
								"Saldo Atual(R$): " + userInfo.saldoAtual);
						$("#family").html(userInfo.nomeFamilia);
						$("#next").html("Próx. fatura: " + userInfo.next);
						$("#nextbalance").html(
								"Saldo próx. mês(R$): " + userInfo.saldoProx)
					},
					error : function(e) {
                    	CFINAC.Message(e.responseText, "error");
					}
				};
				CFINAC.ajax.post(cfg);
			}

			// # sourceURL=sourceWTF.js
		})
		
		/*function ajax(config){
			return new Promise(function(resolve, reject){
				var xhttp = new Xhttpre();
				
				xhttp.open(config.url, config.type);
				
				chhtp.onload = funcitnon(e){
					e.type==200;
						resolve(e.resonposeText);
				};
				
				http.onerro = funcitn(e){
					reject(e);
				}
				
				http.send(config.data);
			});
		}
		
		ajax({url:""}).then(function(r){}).catch(function(e){})*/
