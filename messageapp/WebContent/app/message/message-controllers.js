/**
 * 
 */

angular
    .module("message.controllers", ["app"])
    .controller("MessageListCtrl", MessageListCtrl)
	.controller("MessageNewCtrl", MessageNewCtrl)
	.controller("MessageOpenCtrl", MessageOpenCtrl);

function MessageListCtrl(HttpService, $stateParams) { 
	var messageListCtrl = this;
	messageListCtrl.type = $stateParams.type;
	messageListCtrl.selectAll = false;
	messageListCtrl.selection = {};
	messageListCtrl.selectionList = [];
	getMessage();
	
	// user mode
	var mode = null;
	if(messageListCtrl.type == "received") {
		mode = "recipient";
	}
	else {
		mode = "sender";
	}
	
	function getMessage() {
		HttpService.get("/message/data?context=message&type=" + messageListCtrl.type, function(err, result) {
			if(err) {
				return messageListCtrl.error = true;
			}
			
			messageListCtrl.messages = result;
		});
	}
	
	messageListCtrl.toggleAll = function() {
		messageListCtrl.selectingAll = true;
		messageListCtrl.selectAll = !messageListCtrl.selectAll;
		messageListCtrl.messages.forEach(function(message) {
			messageListCtrl.toggleSelection(message);
		});
		
		messageListCtrl.selectingAll = false;
	};
	
	messageListCtrl.toggleSelection = function(message) {
	    if (messageListCtrl.selection[message.id]) {
	    	delete messageListCtrl.selection[message.id];
	    	var index = messageListCtrl.selectionList.indexOf(message.id);
	    	messageListCtrl.selectionList.splice(index, 1);
	    }
	    else {
	    	messageListCtrl.selection[message.id] = message;
	    	messageListCtrl.selectionList.push(message.id);
	    }
	};
	
	messageListCtrl.deleteSelected = function() {
		messageListCtrl.deleting = true;
		var messageReq = {"selectedMessageIDs" : messageListCtrl.selectionList};
		HttpService.post("/message/data?context=message&type=delete&mode=" + mode, messageReq, function(err, result) {
			if(err) {
				return messageListCtrl.error = true;
			}
			
			for(key in messageListCtrl.selection) {
				var messageListIndex = messageListCtrl.messages.indexOf(messageListCtrl.selection[key]);
				messageListCtrl.messages.splice(messageListIndex, 1);
			}
			
			messageListCtrl.selectAll = false;
			messageListCtrl.selection = {};
			messageListCtrl.selectionList = [];
			messageListCtrl.deleting = false;
		});
	};
}

function MessageNewCtrl(HttpService) {
	var messageNewCtrl = this;
	messageNewCtrl.message = {};
	
	/*
	 * TODO: re-use to fetch contacts and show inside a modal
	 * 
	getUsers();
	
	function getUsers() {
		HttpService.get("/message/data?context=user&type=users", function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.users = result;
		});
	}
	*/
	
	messageNewCtrl.invite = function(email) {
		messageNewCtrl.invalidForm = messageNewCtrl.recipientNotFound = false;
		HttpService.post("/message/data?context=user&type=invite", {"email": email}, function(err, result) {
			if(err) {
				return messageNewCtrl.error = true;
			}
			
			messageNewCtrl.userNotFound = false;
		});
	};
	
	function getRecipient(email, callback) {
		HttpService.get("/message/data?context=user&type=find&email=" + encodeURIComponent(email), function(err, result) {
			if(err) {
				return callback(err);
			}
			
			callback(null, result);
		});
	};
	
	messageNewCtrl.validateAndSend = function(invalidEmail, recipientEmail) {
		if(invalidEmail) {
			messageNewCtrl.invalidEmail = (invalidEmail) ? true : false;
			return messageNewCtrl.invalidForm = true;
		}
		else {
			messageNewCtrl.invalidForm = messageNewCtrl.invalidEmail = messageNewCtrl.recipientNotFound = false;
		}
		
		getRecipient(recipientEmail, function(err, result) {
			if(err) {
				return messageNewCtrl.error = true;
			}
			
			if(result && result != "null") {
				send(result);
			}
			else {
				messageNewCtrl.invalidForm = messageNewCtrl.recipientNotFound = true;
			}
		});
	};
	
	function send(recipient) {
		messageNewCtrl.message.recipientIDs = [recipient.id];
		messageNewCtrl.message.recipientEmails = [recipient.email];
		var messageReq = {"message" : messageNewCtrl.message};
		HttpService.post("/message/data?context=message&type=create", messageReq, function(err, result) {
			if(err) {
				return messageNewCtrl.error = true;
			}
			
			messageNewCtrl.message = result;
			messageNewCtrl.userNotFound = false;
			messageNewCtrl.onSuccess = true;
		});
	};
}

function MessageOpenCtrl(HttpService, $stateParams) { 
	var messageOpenCtrl = this;
	messageOpenCtrl.type = $stateParams.type;
	getMessage();
	
	function getMessage() {
		var messageId = $stateParams.id;
		HttpService.get("/message/data?context=message&type=open&id=" + messageId, function(err, result) {
			if(err) {
				return messageOpenCtrl.error = true;
			}
			
			messageOpenCtrl.message = result;
		});
	}
}