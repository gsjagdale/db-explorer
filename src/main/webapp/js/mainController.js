/**
 * 
 */

var dbExplorerApp = angular.module("dbExplorerApp", []);

var HTTPFactory = dbExplorerApp.factory("HTTPFactory", function($http) {
	var fact = {};

	fact.get = function(uri, submitData) {
		return $http({
			method : 'GET',
			url : uri,
			data : submitData
		});
	}

	fact.post = function(uri, submitData) {
		return $http({
			method : 'POST',
			url : uri,
			data : submitData
		});
	}

	return fact;
});

dbExplorerApp.controller("mainController", function(HTTPFactory, $scope) {
	$scope.isConnected = false;
	$scope.db = {
		"dbType" : "DERBY",
		"host" : "localhost",
		"port" : "1527",
		"name" : "root",
		"username" : "admin",
		"password" : "admin"
	};

	$scope.init = function() {
		HTTPFactory.get('rest/db/dbTypes', '').then(function(data) {
			$scope.dbTypes = data.data;
		});
	}

	$scope.connect = function() {
		HTTPFactory.post('rest/db/connect', $scope.db).then(
				function successCallback(data) {
					if (data.data.message === "Connected") {
						$scope.isConnected = true;
						$scope.message = "Fetching details, please wait ...";
						HTTPFactory.get('rest/db/listSchemas').then(
								function(data) {
									$scope.schemas = data.data;
									$scope.message = "";
								});
					}
				}, function errorCallback(data) {
					$scope.message = data.data.message;
				});
	}

	$scope.changeTables = function() {
		if ($scope.selectedSchema !== null) {
			HTTPFactory.get('rest/db/listTables/' + $scope.selectedSchema)
					.then(function(data) {
						$scope.tables = data.data;
					});
		}
	}

	$scope.changeColumns = function() {
		if ($scope.selectedTable !== null) {
			HTTPFactory.get(
					'rest/db/listColumns/' + $scope.selectedSchema + '/'
							+ $scope.selectedTable).then(function(data) {
				$scope.columns = data.data;
			});
		}
	}
});
