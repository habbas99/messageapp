/**
 * 
 */

angular.module('register', [])
	.controller('RegisterController', function($http) {
		var registerCtrl = this;
		registerCtrl.user = {};
		
		registerCtrl.validateAndInvite = function(invalidEmail, password1, password2) {
			if(password1 != password2 || invalidEmail) {
				registerCtrl.invalidEmail = (invalidEmail) ? true : false;
				registerCtrl.passwordMismatch = (password1 != password2) ? true : false;
				return registerCtrl.invalidForm = true;
			}
			else {
				registerCtrl.invalidForm = registerCtrl.invalidEmail = registerCtrl.passwordMismatch = false;
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
				
			});
		}
	});