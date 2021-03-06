CFINAC = new Object();

CFINAC.ajax = new Object();

toastr.options = {
		"closeButton" : true,
		"debug" : false,
		"newestOnTop" : false,
		"progressBar" : false,
		"positionClass" : "toast-top-right",
		"preventDuplicates" : false,
		"onclick" : null,
		"showDuration" : "300",
		"hideDuration" : "1000",
		"timeOut" : "5000",
		"extendedTimeOut" : "1000",
		"showEasing" : "swing",
		"hideEasing" : "linear",
		"showMethod" : "fadeIn",
		"hideMethod" : "fadeOut"
	}

CFINAC.Message = function(msg, type){
	Command: toastr[type](msg, CFINAC.MessageTitle(type));	
}

CFINAC.MessageTitle = function(title){
	switch(title){
	case "error":
		return "Erro!";
	case "warning":
		return "Cuidado!";
	default:
		return "Sucesso!";
	}
}

CFINAC.Dialog = function(sMsg, cfg){
	bootbox.confirm(sMsg, function(result){
		!result || CFINAC.ajax.post(cfg);
	}); 
}

function ajaxRequestDefault() {
	var def = {
			url: null,
			dataType: "json",
			contentType: "application/json; charset=UTF-8",
			type: "POST",
			success: function () {},
			error: function(err) {
				alert("error = "+ err.responseText);
			}
	};
	return def;
};

function verifyObjectData(cfg) {
	if (cfg.data) {
		if (isObject(cfg.data)) {
			cfg.data = JSON.stringify(cfg.data);
		}
	}
	return cfg;
};

function isObject(o) {
	return $.isArray(o) | $.isPlainObject(o) | $.isFunction(o);
};

CFINAC.ajax.post = function(cfg) {
	var def = new ajaxRequestDefault();
	cfg = verifyObjectData(cfg);
	var config = $.extend(def, cfg);

	$.ajax(config);
};

CFINAC.ajax.get = function(cfg) {
	var def = new ajaxRequestDefault();
	cfg.type = "GET";
	cfg = verifyObjectData(cfg);
	var config = $.extend(def, cfg);
	$.ajax(config);
};