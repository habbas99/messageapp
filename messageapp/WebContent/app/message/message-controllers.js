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
				// TODO: handle error
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
				// TODO: handle error
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
	messageNewCtrl.invitedUsers = [];
	getUsers();
	
	function getUsers() {
		HttpService.get("/message/data?context=user&type=users", function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.users = result;
		});
	}
	
	messageNewCtrl.invite = function(email) {
		HttpService.post("/message/data?context=user&type=invite", {"email": email}, function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.userNotFound = false;
			messageNewCtrl.invitedUsers.push(email);
		});
	};
	
	messageNewCtrl.send = function(recipient) {
		if(!recipient.id) {
			for(user in messageNewCtrl.users) {
				if(user.email == recipient) {
					return messageNewCtrl.send(user);
				}
			}
			
			if(messageNewCtrl.invitedUsers.indexOf(messageNewCtrl.recipient) < 0) {
				messageNewCtrl.userNotFound = true;
			}
			
			return;
		}
		
		messageNewCtrl.message.recipientIDs = [recipient.id];
		messageNewCtrl.message.recipientEmails = [recipient.email];
		var messageReq = {"message" : messageNewCtrl.message};
		HttpService.post("/message/data?context=message&type=create", messageReq, function(err, result) {
			if(err) {
				// TODO: handle error
			}
			
			messageNewCtrl.message = result;
			messageNewCtrl.invitedUsers = [];
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
				// TODO: handle error
			}
			
			messageOpenCtrl.message = result;
		});
	}
}