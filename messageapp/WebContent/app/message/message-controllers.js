/**
 * 
 */

angular
    .module('message.controllers', ['app'])
    .controller('MessageListCtrl', MessageListCtrl)
	.controller('MessageNewCtrl', MessageNewCtrl)
	.controller('MessageOpenCtrl', MessageOpenCtrl);

function MessageListCtrl(HttpService, $stateParams) { 
	var messageListCtrl = this;
	messageListCtrl.type = $stateParams.type;
	getMessage();
	
	function getMessage() {
		HttpService.get('/message/data?context=message&type=' + messageListCtrl.type, function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageListCtrl.messages = result;
		});
	}
}

function MessageNewCtrl(HttpService) { 
	var messageNewCtrl = this;
	messageNewCtrl.message = {};
	getUsers();
	
	function getUsers() {
		HttpService.get('/message/data?context=user&type=users', function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.users = result;
		});
	}
	
	messageNewCtrl.send = function(recipient) {
		messageNewCtrl.message.recipientIDs = [recipient.id];
		HttpService.post('/message/data?context=message&type=create', messageNewCtrl.message, function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.message = result;
			messageNewCtrl.onSuccess = true;
		});
	};
}

function MessageOpenCtrl(HttpService, $stateParams) { 
	var messageOpenCtrl = this;
	getMessage();
	
	function getMessage() {
		var messageId = $stateParams.id;
		HttpService.get('/message/data?context=message&type=open&id=' + messageId, function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageOpenCtrl.message = result;
		});
	}
}