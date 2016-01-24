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
			$log.error(method + " request failed for " + url);
			
			if(response.status == 401) {
				return window.location.href = "/authenticate.jsp?type=login&session=expired";
			}
			
			callback(method + " request failed");
		});
	}
}

function MainCtrl(HttpService, $stateParams) {
	var mainCtrl = this;
	mainCtrl.params = $stateParams;
	getProfile();
	
	function getProfile() {
		HttpService.get("/message/data?context=user&type=profile", function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			mainCtrl.profile = result;
		});
	}
	
	mainCtrl.logout = function() {
		HttpService.post("/authenticator?type=logout", null, function(err) {
			if(err) {
				// TODO: handle error
			}
			
			window.location.href = "/authenticate.jsp?type=logout";
		});
	};
}