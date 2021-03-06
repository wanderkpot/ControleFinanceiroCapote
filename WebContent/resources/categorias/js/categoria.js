CFINAC.categorias = new Object();

iniciaCategoria = function() {

    $(document)
        .ready(
            function() {
                CFINAC.categorias.add = function() {
                    var cfg;
                    var msg, categoria = $("#inputCategory").val(),
                        id = $(
                            "#id").val();

                    if (categoria != "") {
                        var newCategory = new Object();
                        newCategory.id = id;
                        newCategory.name = categoria;

                        cfg = {
                            url: "../rest/categoria/add",
                            data: newCategory,
                            success: function(r) {
                            	CFINAC.Message(r, "success")
                                $("#conteudoRegistro .btn-danger")
                                    .click();
                                table.ajax.reload(null, false);
                            },
                            error: function(err) {
                            	CFINAC.Message(err.responseText, "error");
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    }else{
                    	CFINAC.Message("Preencha todos os campos!", "error");
                    }
                }

                var table = $('#example')
                    .DataTable({
                        aLengthMenu: [
                            [5, 10, 100],
                            [5, 10, 100]
                        ],
                        iDisplayLength: 5,
                        sAjaxDataProp: "",
                        language: {
                            url: "js/Portuguese.json"
                        },
                        sPaginationType: "full_numbers",
                        processing: true,
                        ajax: {
                            url: "../rest/categoria/getCategories/" + 0,
                            data: "id=" + 0,
                            type: "GET"
                        },
                        select: {
                            style: 'os',
                            selector: 'td:first-child'
                        },
                        columns: [{
                                data: "id",
                                className: "center"
                            }, {
                                data: "name",
                                className: "center",
                            }, {
                                data: "id",
                                className: "center",
                                bSortable: false,
                                mRender: function(id) {
                                    return "<a class='link' onclick='CFINAC.categorias.editarCategoria(" +
                                        id +
                                        ")'>Editar</a> /" +
                                        " <a class='link' onclick='CFINAC.categorias.deletaCategoria(" +
                                        id +
                                        ")'>Deletar</a>";
                                }
                            }]
                            // # sourceURL=sourcees.coffeee
                    });

                CFINAC.categorias.editarCategoria = function(id) {
                    $("#conteudoRegistro .btn-danger").click();
                    var cfg = {
                        type: "GET",
                        url: "../rest/categoria/getCategories/" + id,
                        data: "id=" + id,
                        success: function(categoryData) {
                            $("#id").val(categoryData[0].id);
                            $("#inputCategory").val(categoryData[0].name);
                        },
                        error: function(rest) {
                        	CFINAC.Message(rest.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };
                
                CFINAC.categorias.deletaCategoria = function(id) {
                	var cfg = {
							type : "POST",
							url : "../rest/categoria/deletaCategoria/" + id,
							data : "id=" + id,
							success : function(msg) {
		                    	CFINAC.Message(msg, "success");
								table.ajax.reload(null, false);
							},
							error : function(e) {
				            	CFINAC.Message(e.responseText, "error");
							}
						};	
                	CFINAC.Dialog("Deseja realmente excluir essa categoria?", cfg);						
                }
            })
}