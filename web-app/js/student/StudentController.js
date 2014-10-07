app.controller("StudentController", ['$scope','$http', '$log', function($scope, $http, $log){
    console.log("StudentController: this", this);
    console.log("StudentController: $scope", $scope);

    $scope.understand = "I now understand how the scope works!";

    $log.info("$http: ", $http);
    $log.info($http.get)

    $http.get('/demo/student/all')
        .success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $log.info("success - data: ", data);
           // debugger;
        })
        .error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            $log.error("error - data: ", data);
            //debugger;
        });
}] );