/**
 * 
 */

angular
    .module('app', ['app.route', 'ui.bootstrap'])
    .service('HttpService', HttpService)
    .controller('MainCtrl', MainCtrl);

function HttpService($http, $log) {
	this.get = function(url, callback) {
		invoke("GET", url, null, callback);
    };
	
	this.post = function(url, data, callback) {
		invoke("POST", url, data, callback);
	};
	
	function invoke(method, url, data, callback) {
		$http({
			'method': method,
			'url': url,
			'data': data
		}).then(function successCallback(response) {
			callback(null, response.data);
		}, function errorCallback(response) {
			// TODO: log error
			$log.error(method + " request failed for " + url);
			callback(method + " request failed");
		});
	}
}

function MainCtrl(HttpService, $stateParams) {
	var mainCtrl = this;
	mainCtrl.params = $stateParams;
	
	mainCtrl.logout = function() {
		HttpService.post("/authenticator?type=logout", null, function(err) {
			if(err) {
				// TODO: handle error
			}
			
			window.location.href = "/authenticate.jsp?type=logout";
		});
	};
}