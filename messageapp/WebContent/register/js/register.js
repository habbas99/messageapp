/**
 * 
 */

angular.module('register', [])
	.controller('RegisterController', function($http) {
		var registerCtrl = this;
		registerCtrl.user = {};
		
		registerCtrl.invite = function() {
			$http({
				method: 'POST',
				url: '/register?type=invite',
				data: registerCtrl.user
			}).then(function successCallback(response) {
				registerCtrl.onSuccess = true;
			}, function errorCallback(response) {
				
			});
		};
	});