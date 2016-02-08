var sstradingReportControllers = angular
	.module('sstradingReportControllers', ['highcharts-ng'])
	.constant('emptyChart', {
	    options: {credits: {enabled: false}},
	    title: {text: ''},
	    loading: true
	});

sstradingReportControllers.controller('IndexCtrl', ['$scope', '$http', '$interval', function($scope, $http, $interval) {

}]);

sstradingReportControllers.controller('TradeReportCtrl', ['$scope', '$http', '$modal', function($scope, $http, $modal) {
    $http.get('/api/bikes?all=true').success(function(data) {
	$scope.bikes = data;
    });

    $scope.openNewBikeDlg = function() {
	var modalInstance = $modal.open({
	    templateUrl: '/partials/_new_bike.html',
	    controller: 'AddNewBikeCtrl',
	    scope: $scope
	});

	modalInstance.result.then(
		function(newBike) {
		    $scope.bikes.push(newBike);
		},
		function() {
		}
	);
    };
}]);
