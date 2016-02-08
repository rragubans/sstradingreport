'use strict'

sstradingreport.controller('TradingReportCtrl', function TradingReportCtrl($scope, $http, $upload, $modal, $filter,
                                                                          ngTableParams, $location, $timeout) {

    $scope.tradingReportData = [];
    $scope.loading = false;
    $scope.focus = false;
    $scope.blur = false;
    $scope.format = 'M/d/yy hh:mm:ss a';

    $http.get('/api/tradingreports?all=true')
        .success(function(data) {
            $scope.tradingReportData = data;
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 100,
                sorting: {
                    name: 'asc'}
            },
            {
                total: $scope.tradingReportData.length,
                getData: function($defer, params) {
                    var orderedData = params.sorting() ? $filter('orderBy')(data, params.orderBy()) : data;
                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                },
                $scope: {$data: {}}
            });
            $scope.loading = false;
            });

        $scope.getTradingData = function() {
            return $scope.tableParams.data;
        }

        $scope.$watch(function() {
            return $scope.tradingReportData.length;
        }, function(newValue, oldValue) {
            if (angular.isDefined($scope.tableParams)) {
                $scope.tableParams.reload();
            }
        });
    }

});