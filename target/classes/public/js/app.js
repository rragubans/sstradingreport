'use strict';

/* App Module */

Array.prototype.randomize = function() {
    var i = this.length, j, temp;
    if(i===0)
	return this;
    while (--i) {
	j = Math.floor(Math.random() * (i - 1));
	temp = this[i];
	this[i] = this[j];
	this[j] = temp;
    }
    return this;
};

var sstradingreport = angular
	.module('sstradingreport', [
	    'ngRoute',
	    'highcharts-ng',
	    'angularFileUpload',
	    'ui.bootstrap',
	    'ngAnimate',
	    'ngTable'
	])

	.directive('ngActiveTab', function($location) {
	    return {
		link: function postLink(scope, element, attrs) {
		    scope.$on("$routeChangeSuccess", function(event, current, previous) {
			// this var grabs the tab-level off the attribute, or defaults to 1
			var pathLevel = attrs.activeTab || 1,
				// this var finds what the path is at the level specified
				pathToCheck = $location.path().split('/')[pathLevel],
				// this var finds grabs the same level of the href attribute
				tabLink = attrs.href.split('/')[pathLevel];

			if (pathToCheck === tabLink) {
			    element.parent().addClass("active");
			} else {
			    element.parent().removeClass("active");
			}
		    });
		}
	    };
	})
	.config(
		function($routeProvider, $locationProvider) {
		    $locationProvider.html5Mode(true);

		    $routeProvider.
			    when('/tradereports', {
				templateUrl: '/partials/tradingreport/_tradingreport.html',
				controller: 'TradeReportCtrl'
			    }).
			    when('/addnewproject', {
			    	templateUrl: '/partials/project/_add_new_short_project.html',
			    	controller: 'AddNewProjectCtrl'
			    }).
			    when('/updateproject/:param1/:param2', {
                   	templateUrl: '/partials/project/_projectupdate.html',
                   	controller: 'UpdateProjecttrl'
                }).
			    otherwise({
				redirectTo: '/'
			    });
		}
	)

	.run(function($rootScope, $location) {
	    $rootScope.currentYear = new Date().getFullYear();
	    if ($location.path() == "/tradereport") {
           $rootScope.hideIframeBody = true;
	    }
	});
