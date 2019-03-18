Vue.http.interceptors.push(function(request) {
	return function(response) {
		if (response.body.code != 200) {
			response.ok = false;
			layer.alert(response.body.msg, {
				title : '提示',
				icon : 7,
				time : 3000
			});
		}
	};
});