/**
 * 
 */

angular
    .module('app.route', ['ui.router', 'message.controllers'])
    .config(function($stateProvider, $urlRouterProvider) {
    	// For any unmatched url, redirect to /state1
    	$urlRouterProvider.otherwise("/messages/received");
	  
    	// Now set up the states
    	$stateProvider
	    	.state('messages', {
	    		url: "/messages/:type",
	    		templateUrl: "message/list-messages.html",
    			controller: "MessageListCtrl",
    			controllerAs: "messageListCtrl"
	    	})
	    	.state('messages.new', {
	    		url: "/new",
	    		views: {
		    		"@": {
		    			templateUrl: "message/new-message.html",
			    		controller: "MessageNewCtrl",
		    			controllerAs: "messageNewCtrl"
		    		}
	    		}
	    	})
	    	.state('messages.open', {
	    		url: "/:id",
	    		views: {
		    		"@": {
		    			templateUrl: "message/open-message.html",
			    		controller: "MessageOpenCtrl",
		    			controllerAs: "messageOpenCtrl"
		    		}
	    		}
	    	});
	});