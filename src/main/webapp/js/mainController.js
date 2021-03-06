/**
 * 
 */

var dbExplorerApp = angular.module("dbExplorerApp", ['ngRoute']);

var HTTPFactory = dbExplorerApp.factory("HTTPFactory", function($http) {
	var fact = {};

	fact.get = function(uri, submitData) {
		return $http({
			method : 'GET',
			url : uri,
			data : submitData
		});
	}

	fact.post = function(uri, submitData, mediaType) {
		return $http({
			method : 'POST',
			url : uri,
			data : submitData,
			headers: {
            			'Content-Type': mediaType
        		}
		});
	}

	return fact;
});

dbExplorerApp.config(function($routeProvider) {
    $routeProvider.when("/login", {
        templateUrl : "html/login.html"
    }).when("/dbexplorer", {
        templateUrl : "html/explorer.html"
    }).when("/profile", {
        templateUrl : "html/profile.html"
    }).otherwise({
    	templateUrl : "html/login.html"
    });
});

dbExplorerApp.controller("mainController", function(HTTPFactory, $scope, $location) {
	//$scope.isConnected = false;
	$scope.db = {
		//"dbType" : "DERBY",
		//"host" : "localhost",
		//"port" : "1527",
		//"name" : "root",
		//"username" : "admin",
		//"password" : "admin"
	};

	$scope.init = function() {
		HTTPFactory.get('rest/db/dbTypes', '').then(function(data) {
			$scope.dbTypes = data.data;
		});
	}

	$scope.connect = function() {
		HTTPFactory.post('rest/db/connect', $scope.db, 'application/json').then(
				function successCallback(data) {
					if (data.data.message === "Connected") {
						//$scope.isConnected = true;
						$location.url('/dbexplorer');
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
	
	$scope.selectedSql = "";
	$scope.queryResult = {};
	
	$scope.executeSql = function() {
		$scope.queryResult = {};
		$scope.errorMessage = "";
		var textComponent = document.getElementById('sqlQueryTextArea');
		var startPos = textComponent.selectionStart;
	    	var endPos = textComponent.selectionEnd;
	    	$scope.selectedSql = textComponent.value.substring(startPos, endPos);
		
		if ($scope.selectedSql !== null && $scope.selectedSql !== "") {
			HTTPFactory.post('rest/db/executeSql', $scope.selectedSql, 'text/plain').then(
					function successCallback(data) {
						$scope.queryResult = data.data;
					}, function errorCallback(data) {
						$scope.errorMessage = data.data.message;
				});
		}else{
			$scope.errorMessage = "NO query selected to Execute!!!";
		}
	}

});


// ******************  General Functions ********************

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
	// Get the Sidebar
	var mySidebar = document.getElementById("mySidebar");

	// Get the DIV with overlay effect
	var overlayBg = document.getElementById("myOverlay");

	if (mySidebar.style.display === 'block') {
		mySidebar.style.display = 'none';
		overlayBg.style.display = "none";
	} else {
		mySidebar.style.display = 'block';
		overlayBg.style.display = "block";
	}
}

// Close the sidebar with the close button
function w3_close() {
	// Get the Sidebar
	var mySidebar = document.getElementById("mySidebar");

	// Get the DIV with overlay effect
	var overlayBg = document.getElementById("myOverlay");

	mySidebar.style.display = "none";
	overlayBg.style.display = "none";
}
