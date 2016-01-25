/**
 * 
 */

angular.module('register', [])
	.controller('RegisterController', function($http) {
		var registerCtrl = this;
		registerCtrl.user = {};
		
		registerCtrl.remind = function() {
			registerCtrl.userExists = false;
			$http({
				method: 'POST',
				url: '/register?type=remind',
				data: registerCtrl.user
			}).then(function successCallback(response) {
				registerCtrl.onSuccess = true;
			}, function errorCallback(response) {
				registerCtrl.error = true;
			});
		};
		
		registerCtrl.validateAndInvite = function(invalidEmail, password1, password2) {
			if(invalidEmail || password1 != password2) {
				registerCtrl.invalidEmail = (invalidEmail) ? true : false;
				registerCtrl.passwordMismatch = (password1 != password2) ? true : false;
				return registerCtrl.invalidForm = true;
			}
			else {
				registerCtrl.invalidForm = registerCtrl.invalidEmail = registerCtrl.passwordMismatch = false;
				registerCtrl.error = registerCtrl.userExists = false;
			}
			
			invite(password1);
		};
		
		function invite(password) {
			registerCtrl.user.password = password;
			$http({
				method: 'POST',
				url: '/register?type=invite',
				data: registerCtrl.user
			}).then(function successCallback(response) {
				registerCtrl.onSuccess = true;
			}, function errorCallback(response) {
				if(response.status == 530) {
					registerCtrl.userExists = true;
				}
				else {
					registerCtrl.error = true;
				}
			});
		}
	});